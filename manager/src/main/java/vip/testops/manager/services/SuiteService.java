package vip.testops.manager.services;

import vip.testops.manager.common.Response;

import java.util.List;

public interface SuiteService {

    void doUpdateSuite(Long projectId, List<Long> caseIdList, Response<?> response);
}
