package vip.testops.manager.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vip.testops.manager.common.Response;
import vip.testops.manager.entities.vto.BugVTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bug")
public class BugController {

    @GetMapping("/overtime")
    @ResponseBody
    public Response<List<BugVTO>> getOverTime(){
        // 并没有真正去实现从第三方bug管理系统上获取bug统计数据，这里仅做演示
        List<BugVTO> bugVTOList = new ArrayList<>();
        bugVTOList.add(new BugVTO("2020-12-01", 45));
        bugVTOList.add(new BugVTO("2020-12-02", 50));
        bugVTOList.add(new BugVTO("2020-12-03", 66));
        bugVTOList.add(new BugVTO("2020-12-04", 42));
        bugVTOList.add(new BugVTO("2020-12-05", 70));
        Response<List<BugVTO>> response = new Response<>();
        response.dataSuccess(bugVTOList);
        return response;
    }

}
