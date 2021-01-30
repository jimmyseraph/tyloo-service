package vip.testops.engine.services;

import vip.testops.engine.common.Response;
import vip.testops.engine.entities.vto.CaseVTO;
import vip.testops.engine.entities.vto.ExecResultVTO;

public interface ExecutionService {

    void doExecuteCase(Long projectId, CaseVTO caseVTO, Response<ExecResultVTO> response);
}
