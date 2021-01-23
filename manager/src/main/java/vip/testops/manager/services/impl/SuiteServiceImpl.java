package vip.testops.manager.services.impl;

import org.springframework.stereotype.Service;
import vip.testops.manager.common.Response;
import vip.testops.manager.services.SuiteService;

import java.util.List;

@Service
public class SuiteServiceImpl implements SuiteService {
    @Override
    public void doUpdateSuite(Long projectId, List<Long> caseIdList, Response<?> response) {

    }
}
