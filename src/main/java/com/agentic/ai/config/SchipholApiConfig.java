package com.agentic.ai.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Centralized HTTP client configuration.
 */
@Configuration
@EnableConfigurationProperties(SchipholApiProperties.class)
public class SchipholApiConfig {

//   Root Cause: Certificate Validation Failure: PKIX path building failed: unable to find valid certification path to requested target
//   so I have to use RestTemplate
//    @Bean
//    public RestClient restClient(SchipholApiProperties schipholApiProperties) {
//        var jdkHttpClient = HttpClient
//                .newBuilder()
//                .connectTimeout(Duration.ofSeconds(10))
//                .build();
//
//        var factory = new JdkClientHttpRequestFactory(jdkHttpClient);
//
//        return RestClient.builder()
//                .requestFactory(factory)
//                .baseUrl(schipholApiProperties.baseUrl())
//                //.defaultHeader("Accept", "application/json")
//                .defaultHeader("app_id", schipholApiProperties.appId())
//                .defaultHeader("app_key", schipholApiProperties.appKey())
//                .defaultHeader("ResourceVersion", schipholApiProperties.resourceVersion())
//                .build();
//
//    }

    @Bean
    public RestTemplate restTemplate(SchipholApiProperties schipholApiProperties) {
        RestTemplate restTemplate = new RestTemplate();

        ClientHttpRequestInterceptor headerInterceptor = (request, body, execution) -> {
            request.getHeaders().set("app_id", schipholApiProperties.appId());
            request.getHeaders().set("app_key", schipholApiProperties.appKey());
            request.getHeaders().set("ResourceVersion", schipholApiProperties.resourceVersion());
            request.getHeaders().set("Accept", "application/json");
            return execution.execute(request, body);
        };
        restTemplate.setInterceptors(List.of(headerInterceptor));
        return restTemplate;
    }
}
