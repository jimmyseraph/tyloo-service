package vip.testops.engine.api.fallback;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import vip.testops.engine.api.ManagerApi;
import vip.testops.engine.common.Response;

@Component
public class ManagerApiFallbackFactory implements FallbackFactory<ManagerApi> {
    @Override
    public ManagerApi create(Throwable throwable) {
        return new ManagerApi() {
            @Override
            public Response<?> updateSuiteStatus(Long projectId, Long caseId, Long duration, Integer status) {
                Response<?> response = new Response<>();
                response.serviceError("Internal service not available");
                return response;
            }

            @Override
            public Response<?> projectStatusUpdate(Long projectId, Integer status) {
                Response<?> response = new Response<>();
                response.serviceError("Internal service not available");
                return response;
            }
        };
    }
}
