package vip.testops.manager.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.testops.manager.common.Response;
import vip.testops.manager.entities.dto.CaseDTO;
import vip.testops.manager.entities.dto.ProjectDTO;
import vip.testops.manager.entities.dto.SuiteDTO;
import vip.testops.manager.entities.enums.SuiteStatues;
import vip.testops.manager.mappers.CaseMapper;
import vip.testops.manager.mappers.ProjectMapper;
import vip.testops.manager.mappers.SuiteMapper;
import vip.testops.manager.services.SuiteService;

import java.util.List;

@Service
@Transactional
public class SuiteServiceImpl implements SuiteService {

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
    public void doUpdateSuite(Long projectId, List<Long> caseIdList, Response<?> response) {
        // 根据projectID从t_project表中取出对应的project记录
        ProjectDTO projectDTO = projectMapper.getProjectById(projectId);
        if(projectDTO == null){
            response.serviceError("project not available");
            return;
        }
        // 将suite表中关于这个projectID的数据全部删除
        suiteMapper.removeSuiteByProjectId(projectId);
        // 添加新的关联关系
        for(Long caseId : caseIdList){
            CaseDTO caseDTO = caseMapper.getCaseById(caseId);
            if(caseDTO != null){
                SuiteDTO suiteDTO = new SuiteDTO();
                suiteDTO.setProjectId(projectId);
                suiteDTO.setCaseId(caseId);
                suiteDTO.setDuration(-1L);
                suiteDTO.setStatus(SuiteStatues.INITIAL.getKey());
                suiteMapper.addSuite(suiteDTO);
            }
        }
        response.commonSuccess();
    }

    @Override
    public void doUpdateSuiteStatus(Long projectId, Long caseId, Long duration, Integer status, Response<?> response) {
        if(suiteMapper.updateStatusByProjectIdAndCaseId(projectId, caseId, duration, status) != 1){
            response.serviceError("suite not exist");
        }else {
            response.commonSuccess();
        }
    }
}
