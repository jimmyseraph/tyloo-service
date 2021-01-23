package vip.testops.manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.testops.manager.common.Response;
import vip.testops.manager.entities.vto.CaseVTO;
import vip.testops.manager.entities.vto.CoverageVTO;
import vip.testops.manager.services.CaseService;
import vip.testops.manager.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/case")
public class CaseController {

    private CaseService caseService;

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }

    @GetMapping("/total")
    @ResponseBody
    public Response<Integer> total(){
        Response<Integer> response = new Response<>();
        caseService.doQueryTotal(response);
        return response;
    }

    @GetMapping("/pass")
    @ResponseBody
    public Response<Double> pass(){
        Response<Double> response = new Response<>();
        caseService.doPassPercent(response);
        return response;
    }

    @GetMapping("/coverage_overtime")
    @ResponseBody
    public Response<List<CoverageVTO>> coverage(){
        // 并没有真正去实现从第三方需求或API管理系统上获取覆盖率统计数据，这里仅做演示
        List<CoverageVTO> coverageVTOList = new ArrayList<>();
        coverageVTOList.add(new CoverageVTO("2020-12-01", 74.1));
        coverageVTOList.add(new CoverageVTO("2020-12-02", 84.1));
        coverageVTOList.add(new CoverageVTO("2020-12-03", 81.5));
        coverageVTOList.add(new CoverageVTO("2020-12-04", 80.8));
        coverageVTOList.add(new CoverageVTO("2020-12-05", 94.2));
        Response<List<CoverageVTO>> response = new Response<>();
        response.dataSuccess(coverageVTOList);
        return response;
    }

    @GetMapping("/list")
    @ResponseBody
    public Response<List<CaseVTO>> getList(@RequestParam(value = "key", required = false) String key){
        Response<List<CaseVTO>> response = new Response<>();
        caseService.doGetList(key, response);
        return response;
    }

    @PostMapping("/add")
    @ResponseBody
    public Response<?> addCase(@RequestBody CaseVTO caseVTO){
        Response<?> response = new Response<>();
        // 参数检查
        if(StringUtil.isEmptyOrNull(caseVTO.getCaseName())){
            response.paramMissError("caseName");
            return response;
        }
        if(StringUtil.isEmptyOrNull(caseVTO.getMethod())){
            response.paramMissError("method");
            return response;
        }
        if(StringUtil.isEmptyOrNull(caseVTO.getUrl())){
            response.paramMissError("url");
            return response;
        }
        caseService.doAddCase(caseVTO, response);
        return response;
    }

    @PostMapping("/modify")
    @ResponseBody
    public Response<?> modifyCase(@RequestBody CaseVTO caseVTO){
        Response<?> response = new Response<>();
        // 参数检查
        if(caseVTO.getCaseId() == null){
            response.paramMissError("caseId");
            return response;
        }
        if(StringUtil.isEmptyOrNull(caseVTO.getCaseName())){
            response.paramMissError("caseName");
            return response;
        }
        if(StringUtil.isEmptyOrNull(caseVTO.getMethod())){
            response.paramMissError("method");
            return response;
        }
        if(StringUtil.isEmptyOrNull(caseVTO.getUrl())){
            response.paramMissError("url");
            return response;
        }
        caseService.doModifyCase(caseVTO, response);
        return response;
    }

    @DeleteMapping("/{id}/remove")
    @ResponseBody
    public Response<?> removeCase(@PathVariable("id") Long caseId){
        Response<?> response = new Response<>();
        caseService.doRemoveCase(caseId, response);
        return response;
    }
}
