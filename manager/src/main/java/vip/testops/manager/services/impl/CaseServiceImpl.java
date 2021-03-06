package vip.testops.manager.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.testops.manager.common.Response;
import vip.testops.manager.entities.dto.AssertionDTO;
import vip.testops.manager.entities.dto.CaseDTO;
import vip.testops.manager.entities.dto.HeaderDTO;
import vip.testops.manager.entities.vto.CaseVTO;
import vip.testops.manager.mappers.AssertionsMapper;
import vip.testops.manager.mappers.CaseMapper;
import vip.testops.manager.mappers.HeaderMapper;
import vip.testops.manager.mappers.SuiteMapper;
import vip.testops.manager.services.CaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CaseServiceImpl implements CaseService {

    private CaseMapper caseMapper;
    private SuiteMapper suiteMapper;
    private HeaderMapper headerMapper;
    private AssertionsMapper assertionsMapper;

    @Autowired
    public void setCaseMapper(CaseMapper caseMapper) {
        this.caseMapper = caseMapper;
    }

    @Autowired
    public void setSuiteMapper(SuiteMapper suiteMapper) {
        this.suiteMapper = suiteMapper;
    }

    @Autowired
    public void setHeaderMapper(HeaderMapper headerMapper) {
        this.headerMapper = headerMapper;
    }

    @Autowired
    public void setAssertionsMapper(AssertionsMapper assertionsMapper) {
        this.assertionsMapper = assertionsMapper;
    }

    @Override
    public void doQueryTotal(Response<Integer> response) {
        int total = caseMapper.queryTotal();
        response.dataSuccess(total);
    }

    @Override
    public void doPassPercent(Response<Double> response) {
        int total = suiteMapper.total();
        int pass = suiteMapper.countByStatus(1);
        double percent = total == 0 ? 0.0 : pass / (total * 1.0);
        response.dataSuccess(percent * 100.0);
    }

    @Override
    public void doGetList(String key, Response<List<CaseVTO>> response) {
        List<CaseDTO> caseDTOList = caseMapper.getList();
        List<CaseVTO> caseVTOList = new ArrayList<>();
        if(key != null){
            caseDTOList = caseDTOList.stream().filter((item) ->
                 item.getCaseName().contains(key) || item.getDescription().contains(key)
            ).collect(Collectors.toList());
        }
        caseDTOList.forEach((item) -> {
            CaseVTO caseVTO = new CaseVTO();
            caseVTO.setCaseId(item.getCaseId());
            caseVTO.setCaseName(item.getCaseName());
            caseVTO.setDescription(item.getDescription());
            caseVTO.setUrl(item.getUrl());
            caseVTO.setMethod(item.getMethod());
            caseVTO.setBody(item.getBody());
            List<HeaderDTO> headerDTOList = headerMapper.getHeadersByCaseId(item.getCaseId());
            List<AssertionDTO> assertionDTOList = assertionsMapper.getAssertionsByCaseId(item.getCaseId());
            caseVTO.setHeaders(headerDTOList);
            caseVTO.setAssertions(assertionDTOList);
            caseVTOList.add(caseVTO);
        });
        response.dataSuccess(caseVTOList);
    }

    @Override
    public void doAddCase(CaseVTO caseVTO, Response<?> response) {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setCaseName(caseVTO.getCaseName());
        caseDTO.setDescription(caseVTO.getDescription());
        caseDTO.setUrl(caseVTO.getUrl());
        caseDTO.setMethod(caseVTO.getMethod());
        caseDTO.setBody(caseVTO.getBody());
        if(caseMapper.addCase(caseDTO) != 1){
            response.serviceError("add case failed");
            return;
        }
        if(caseVTO.getHeaders() != null){
            caseVTO.getHeaders().forEach(header -> {
                HeaderDTO headerDTO = new HeaderDTO();
                headerDTO.setName(header.getName());
                headerDTO.setValue(header.getValue());
                headerDTO.setCaseId(caseDTO.getCaseId());
                if(headerMapper.addHeader(headerDTO) != 1){
                    log.warn("add header {} to database failed", headerDTO);
                }
            });

        }
        if(caseVTO.getAssertions() != null){

            caseVTO.getAssertions().forEach(assertion -> {
                AssertionDTO assertionDTO = new AssertionDTO();
                assertionDTO.setActual(assertion.getActual());
                assertionDTO.setOp(assertion.getOp());
                assertionDTO.setExpected(assertion.getExpected());
                assertionDTO.setCaseId(caseDTO.getCaseId());
                if(assertionsMapper.addAssertion(assertionDTO) != 1){
                    log.warn("add assertion {} to database failed", assertionDTO);
                }
            });
        }
        response.commonSuccess();
    }

    @Override
    public void doModifyCase(CaseVTO caseVTO, Response<?> response) {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setCaseId(caseVTO.getCaseId());
        caseDTO.setCaseName(caseVTO.getCaseName());
        caseDTO.setDescription(caseVTO.getDescription());
        caseDTO.setUrl(caseVTO.getUrl());
        caseDTO.setMethod(caseVTO.getMethod());
        caseDTO.setBody(caseVTO.getBody());
        if(caseMapper.modifyCase(caseDTO) != 1){
            response.serviceError("modify case failed");
            return;
        }
        headerMapper.removeHeadersByCaseId(caseDTO.getCaseId());
        caseVTO.getHeaders().forEach(header -> {
            HeaderDTO headerDTO = new HeaderDTO();
            headerDTO.setName(header.getName());
            headerDTO.setValue(header.getValue());
            headerDTO.setCaseId(caseDTO.getCaseId());
            if(headerMapper.addHeader(headerDTO) != 1){
                log.warn("add header {} to database failed", headerDTO);
            }
        });
        assertionsMapper.removeAssertionsByCaseId(caseDTO.getCaseId());
        caseVTO.getAssertions().forEach(assertion -> {
            AssertionDTO assertionDTO = new AssertionDTO();
            assertionDTO.setActual(assertion.getActual());
            assertionDTO.setOp(assertion.getOp());
            assertionDTO.setExpected(assertion.getExpected());
            assertionDTO.setCaseId(caseDTO.getCaseId());
            if(assertionsMapper.addAssertion(assertionDTO) != 1){
                log.warn("add assertion {} to database failed", assertionDTO);
            }
        });
        response.commonSuccess();
    }

    @Override
    public void doRemoveCase(Long caseId, Response<?> response) {
        if(caseMapper.removeCase(caseId) != 1){
            response.serviceError("remove case failed");
            return;
        }
        headerMapper.removeHeadersByCaseId(caseId);
        assertionsMapper.removeAssertionsByCaseId(caseId);
        response.commonSuccess();
    }
}
