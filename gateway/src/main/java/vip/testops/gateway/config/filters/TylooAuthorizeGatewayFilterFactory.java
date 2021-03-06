package vip.testops.gateway.config.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import vip.testops.gateway.api.AccountApi;
import vip.testops.gateway.common.Response;
import vip.testops.gateway.entities.vto.AccountVTO;
import vip.testops.gateway.utils.FilterUtil;
import vip.testops.gateway.utils.StringUtil;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class TylooAuthorizeGatewayFilterFactory extends AbstractGatewayFilterFactory<TylooAuthorizeGatewayFilterFactory.Config> {

    private static final String AUTHORIZE_TOKEN = "Access-Token";

    private AccountApi accountApi;

    @Autowired
    public void setAccountApi(AccountApi accountApi) {
        this.accountApi = accountApi;
    }

    public TylooAuthorizeGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("needAuth");
    }

    @Override
    public GatewayFilter apply(Config config) {
        ObjectMapper objectMapper = new ObjectMapper();
        return ((exchange, chain) -> {
            log.info("--> Starting check access token....");
            ServerHttpRequest serverHttpRequest = exchange.getRequest();
            ServerHttpResponse serverHttpResponse = exchange.getResponse();
            HttpHeaders headers = serverHttpRequest.getHeaders();

            if(!StringUtil.isEmptyOrNull(config.getNeedAuth()) && Boolean.parseBoolean(config.getNeedAuth())){
                String token = headers.getFirst(AUTHORIZE_TOKEN);
                Response<?> response = new Response<>();
                String jsonString;
                try {
                    response.unauthorizedError();
                    jsonString = objectMapper.writeValueAsString(response);
                } catch (JsonProcessingException e) {
                    jsonString = "{\"code\": 4002, \"message\": \"internal error\"}";
                    e.printStackTrace();
                    serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return FilterUtil.failedReturn(jsonString, serverHttpResponse);
                }
                //  如果请求头中的Access-Token为空
                if(StringUtil.isEmptyOrNull(token)){
                    serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
                    // 将用户请求在拦截器这一层直接返回，而不再把请求向后转发
                    return FilterUtil.failedReturn(jsonString, serverHttpResponse);
                }
                // 请求头中的Access-Token不为空
                // 将token的值传给account服务进行验证
                Response<AccountVTO> resp = accountApi.authorize(token);
                log.info("get response from account service: code:{}, message:{}", resp.getCode(), resp.getMessage());
                if(resp.getCode() != 1000){
                    serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return FilterUtil.failedReturn(jsonString, serverHttpResponse);
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config{
        private String needAuth;

        public Config(String needAuth) {
            this.needAuth = needAuth;
        }

        public Config() {
        }

        public String getNeedAuth() {
            return needAuth;
        }

        public void setNeedAuth(String needAuth) {
            this.needAuth = needAuth;
        }
    }
}
