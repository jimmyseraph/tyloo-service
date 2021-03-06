package vip.testops.gateway.api.fallback;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import vip.testops.gateway.api.AccountApi;
import vip.testops.gateway.common.Response;
import vip.testops.gateway.entities.vto.AccountVTO;

@Component
public class AccountApiFallbackFactory implements FallbackFactory<AccountApi> {
    @Override
    public AccountApi create(Throwable throwable) {
        return new AccountApi() {
            @Override
            public Response<AccountVTO> authorize(String token) {
                Response<AccountVTO> response = new Response<>();
                response.serviceError("Internal service is not available");
                return response;
            }
        };
    }
}
