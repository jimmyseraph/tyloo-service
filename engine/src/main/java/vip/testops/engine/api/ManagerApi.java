package vip.testops.engine.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vip.testops.engine.api.fallback.ManagerApiFallbackFactory;
import vip.testops.engine.common.Response;

@FeignClient(name = "manager", fallbackFactory = ManagerApiFallbackFactory.class)
public interface ManagerApi {
    @GetMapping("/suite/{projectId}/{caseId}/update")
    Response<?> updateSuiteStatus(
            @PathVariable Long projectId,
            @PathVariable Long caseId,
            @RequestParam(value = "duration", required = false) Long duration,
            @RequestParam(value = "status", required = false) Integer status
    );

    @GetMapping("/project/{id}/update")
    Response<?> projectStatusUpdate(
            @PathVariable("id") Long projectId,
            @RequestParam(value = "status", required = false) Integer status
    );
}
