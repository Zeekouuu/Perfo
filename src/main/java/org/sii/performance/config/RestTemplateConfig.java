package org.sii.performance.config;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private final TokenStore tokenStore;

    public RestTemplateConfig(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Bean
    @LoadBalanced

    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            String token = tokenStore.getToken();
            if (token != null) {
                request.getHeaders().add("Authorization", "Bearer " + token);
            }
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
