package vip.testops.gateway.config.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

public class TylooAuthorizeGatewayFilterFactory extends AbstractGatewayFilterFactory<TylooAuthorizeGatewayFilterFactory.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        return null;
    }

    public static class Config{

    }
}
