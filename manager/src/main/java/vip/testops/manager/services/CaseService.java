package vip.testops.manager.services;

import vip.testops.manager.common.Response;
import vip.testops.manager.entities.vto.CaseVTO;

import java.util.List;

public interface CaseService {
    void doQueryTotal(Response<Integer> response);

    void doPassPercent(Response<Double> response);

    void doGetList(String key, Response<List<CaseVTO>> response);

    void doAddCase(CaseVTO caseVTO, Response<?> response);

    void doModifyCase(CaseVTO caseVTO, Response<?> response);

    void doRemoveCase(Long caseId, Response<?> response);
}
