package vip.testops.engine.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.testops.engine.common.Response;
import vip.testops.engine.entities.vto.CaseVTO;
import vip.testops.engine.entities.vto.ExecResultVTO;

@Service
@Slf4j
public class ExecutionServiceImpl implements ExecutionService{
    @Override
    public void doExecuteCase(Long projectId, CaseVTO caseVTO, Response<ExecResultVTO> response) {
        ExecResultVTO execResultVTO = new ExecResultVTO();
        // 发送http请求
    }
}
