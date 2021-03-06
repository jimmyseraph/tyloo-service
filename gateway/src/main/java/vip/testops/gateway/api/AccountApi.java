package vip.testops.gateway.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.testops.gateway.api.fallback.AccountApiFallbackFactory;
import vip.testops.gateway.common.Response;
import vip.testops.gateway.entities.vto.AccountVTO;

@FeignClient(name = "account", fallbackFactory = AccountApiFallbackFactory.class)
public interface AccountApi {
    @GetMapping("/account/authorize")
    Response<AccountVTO> authorize(@RequestParam(value = "token", required = false) String token);
}
