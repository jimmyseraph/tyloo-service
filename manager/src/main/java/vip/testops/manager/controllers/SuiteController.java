package vip.testops.manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.testops.manager.common.Response;
import vip.testops.manager.services.SuiteService;

import java.util.List;

@RestController
@RequestMapping("/suite")
public class SuiteController {

    private SuiteService suiteService;

    @Autowired
    public void setSuiteService(SuiteService suiteService) {
        this.suiteService = suiteService;
    }

    @PostMapping("/{projectId}/update")
    @ResponseBody
    public Response<?> updateSuite(
            @PathVariable Long projectId,
            @RequestBody List<Long> caseIdList
    ){
        Response<?> response = new Response<>();
        if(caseIdList == null){
            response.paramMissError("caseIdList");
            return response;
        }
        suiteService.doUpdateSuite(projectId, caseIdList, response);
        return response;
    }

    @GetMapping("/{projectId}/{caseId}/update")
    @ResponseBody
    public Response<?> updateSuiteStatus(
            @PathVariable Long projectId,
            @PathVariable Long caseId,
            @RequestParam(value = "status", required = false) Integer status
    ){
        Response<?> response = new Response<>();
        if(status == null) {
            response.paramMissError("status");
            return response;
        }
        suiteService.doUpdateSuiteStatus(projectId, caseId, status, response);
        return response;
    }
}
