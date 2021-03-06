package vip.testops.manager.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vip.testops.manager.common.Response;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @RequestMapping("/test")
    @ResponseBody
    public Response<?> testDemo(){
        Response<?> response = new Response<>();
        response.commonSuccess();
        return response;
    }
}
