package vip.testops.manager.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.testops.manager.common.Response;
import vip.testops.manager.entities.dto.DetailDTO;
import vip.testops.manager.entities.dto.ProjectDTO;
import vip.testops.manager.entities.dto.SuiteDTO;
import vip.testops.manager.entities.enums.ProjectStatus;
import vip.testops.manager.entities.enums.SuiteStatues;
import vip.testops.manager.entities.vto.ProjectVTO;
import vip.testops.manager.entities.vto.SuiteVTO;
import vip.testops.manager.mappers.CaseMapper;
import vip.testops.manager.mappers.ProjectMapper;
import vip.testops.manager.mappers.SuiteMapper;
import vip.testops.manager.services.ProjectService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectMapper projectMapper;
    private SuiteMapper suiteMapper;
    private CaseMapper caseMapper;

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
}
