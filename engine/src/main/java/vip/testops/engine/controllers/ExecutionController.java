package vip.testops.engine.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.testops.engine.common.Response;
import vip.testops.engine.entities.vto.CaseVTO;
import vip.testops.engine.entities.vto.ExecResultVTO;
import vip.testops.engine.services.ExecutionService;
import vip.testops.engine.utils.StringUtil;

@RestController
@RequestMapping("/execute")
@Slf4j
public class ExecutionController {

    private ExecutionService executionService;

    @Autowired
    public void setExecutionService(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @PostMapping("/try")
    @ResponseBody
    public Response<ExecResultVTO> execCase(@RequestBody CaseVTO caseVTO) {
        Response<ExecResultVTO> response = new Response<>();
        // 参数检查
        if(StringUtil.isEmptyOrNull(caseVTO.getUrl())){
            response.paramMissError("url");
            return response;
        }
        if(StringUtil.isEmptyOrNull(caseVTO.getMethod())){
            response.paramMissError("method");
            return response;
        }
        if(caseVTO.getCaseId() == null){
            response.paramMissError("caseId");
            return response;
        }

        executionService.doExecuteCase(null, caseVTO, response);
        return response;
    }
}
