package vip.testops.manager.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.testops.manager.common.Response;
import vip.testops.manager.entities.dto.*;
import vip.testops.manager.entities.enums.ProjectStatus;
import vip.testops.manager.entities.enums.SuiteStatues;
import vip.testops.manager.entities.vto.CaseVTO;
import vip.testops.manager.entities.vto.ExecutionVTO;
import vip.testops.manager.entities.vto.ProjectVTO;
import vip.testops.manager.entities.vto.SuiteVTO;
import vip.testops.manager.mappers.*;
import vip.testops.manager.mq.Sender;
import vip.testops.manager.services.ProjectService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private ProjectMapper projectMapper;
    private SuiteMapper suiteMapper;
    private CaseMapper caseMapper;
    private HeaderMapper headerMapper;
    private AssertionsMapper assertionsMapper;
    private Sender sender;

    @Autowired
    public void setProjectMapper(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Autowired
    public void setSuiteMapper(SuiteMapper suiteMapper) {
        this.suiteMapper = suiteMapper;
    }

    @Autowired
    public void setCaseMapper(CaseMapper caseMapper) {
        this.caseMapper = caseMapper;
    }

    @Autowired
    public void setHeaderMapper(HeaderMapper headerMapper) {
        this.headerMapper = headerMapper;
    }

    @Autowired
    public void setAssertionsMapper(AssertionsMapper assertionsMapper) {
        this.assertionsMapper = assertionsMapper;
    }

    @Autowired
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    @Override
    public void doGetList(Response<List<ProjectVTO>> response) {
        List<ProjectVTO> projectVTOList = new ArrayList<>();
        projectMapper.getList().forEach(item -> {
            ProjectVTO projectVTO = new ProjectVTO();
            projectVTO.setProjectId(item.getProjectId());
            projectVTO.setProjectName(item.getProjectName());
            projectVTO.setDescription(item.getDescription());
            ProjectStatus status = ProjectStatus.getByKey(item.getStatus());
            projectVTO.setStatus(status == null ? null : status.getValue());
            projectVTOList.add(projectVTO);
        });
        response.dataSuccess(projectVTOList);
    }

    @Override
    public void doAddProject(String projectName, String description, Response<?> response) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectName(projectName);
        projectDTO.setDescription(description);
        projectDTO.setStatus(ProjectStatus.READY.getKey());
        if(projectMapper.addProject(projectDTO) != 1){
            response.serviceError("add project failed");
            return;
        }
        response.commonSuccess();
    }

    @Override
    public void doModifyProject(Long projectId, String projectName, String description, Response<?> response) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(projectId);
        projectDTO.setProjectName(projectName);
        projectDTO.setDescription(description);
        if(projectMapper.modifyProject(projectDTO) != 1){
            response.serviceError("modify project failed");
            return;
        }
        response.commonSuccess();
    }

    @Override
    public void doRemoveProject(Long projectId, Response<?> response) {
        if(projectMapper.removeProject(projectId) != 1){
            response.serviceError("remove project failed");
            return;
        }
        response.commonSuccess();
    }

    @Override
    public void doGetDetail(Long projectId, Response<ProjectVTO> response) {
        ProjectDTO projectDTO = projectMapper.getProjectById(projectId);
        if(projectDTO == null){
            response.serviceError("project not exist");
            return;
        }
        ProjectVTO projectVTO = new ProjectVTO();
        projectVTO.setProjectId(projectId);
        projectVTO.setProjectName(projectDTO.getProjectName());
        projectVTO.setDescription(projectDTO.getDescription());
        ProjectStatus status = ProjectStatus.getByKey(projectDTO.getStatus());
        projectVTO.setStatus(status == null ? null : status.getValue());

        List<DetailDTO> detailDTOList = suiteMapper.getSuiteVTOByProjectId(projectId);
        List<SuiteVTO> suiteVTOList = new ArrayList<>();
        detailDTOList.forEach(item -> {
            SuiteVTO suiteVTO = new SuiteVTO();
            suiteVTO.setCaseId(item.getCaseId());
            suiteVTO.setCaseName(item.getCaseName());
            suiteVTO.setDescription(item.getDescription());
            SuiteStatues suiteStatues = SuiteStatues.getByKey(item.getStatus());
            suiteVTO.setStatus(suiteStatues == null ? null : suiteStatues.getValue());
            suiteVTOList.add(suiteVTO);
        });
        projectVTO.setCaseList(suiteVTOList);
        response.dataSuccess(projectVTO);
    }

    @Override
    public void doExecuteProject(Long projectId, Response<?> response) {
        // 根据projectId获取t_project表中的实体
        ProjectDTO projectDTO = projectMapper.getProjectById(projectId);
        if(projectDTO == null){
            response.serviceError("project not available");
            return;
        }
        // 构造需要发送到消息队列的消息体
        ExecutionVTO executionVTO = new ExecutionVTO();
        executionVTO.setProjectId(projectId);
        executionVTO.setProjectName(projectDTO.getProjectName());
        List<CaseVTO> caseVTOList = new ArrayList<>();
        // 从suite表取出对应projectId的所有CaseId
        List<SuiteDTO> suiteDTOList = suiteMapper.getSuiteByProjectId(projectId);
        suiteDTOList.forEach(item -> {
            CaseDTO caseDTO = caseMapper.getCaseById(item.getCaseId());
            CaseVTO caseVTO = new CaseVTO();
            caseVTO.setCaseId(caseDTO.getCaseId());
            caseVTO.setCaseName(caseDTO.getCaseName());
            caseVTO.setDescription(caseDTO.getDescription());
            caseVTO.setUrl(caseDTO.getUrl());
            caseVTO.setMethod(caseDTO.getMethod());
            caseVTO.setBody(caseDTO.getBody());
            List<HeaderDTO> headerDTOList = headerMapper.getHeadersByCaseId(caseDTO.getCaseId());
            List<AssertionDTO> assertionDTOList = assertionsMapper.getAssertionsByCaseId(caseDTO.getCaseId());
            caseVTO.setHeaders(headerDTOList);
            caseVTO.setAssertions(assertionDTOList);
            caseVTOList.add(caseVTO);
        });
        executionVTO.setCaseList(caseVTOList);
        // 发送消息体到消息队列服务
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String msg = objectMapper.writeValueAsString(executionVTO);
            sender.send(msg);
        } catch (JsonProcessingException e) {
            log.error("system error", e);
            response.serviceError("system error");
        }

        // 将project的状态置为执行中（running）
        projectMapper.updateProjectStatusById(projectId, ProjectStatus.RUNNING.getKey());

        // 将suite表中关于当前project所关联的所有case状态重新置为initial
        suiteMapper.updateStatusByProjectId(projectId, SuiteStatues.INITIAL.getKey());

        response.commonSuccess();
    }

    @Override
    public void doUpdateProjectStatus(Long projectId, Integer status, Response<?> response) {
        projectMapper.updateProjectStatusById(projectId, status);
        response.commonSuccess();
    }

}
