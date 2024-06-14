package org.sii.performance.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplatee {
    @Bean
    @LoadBalanced   //si il ya plusieur instance du meme microservice il va faire load balancer
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
