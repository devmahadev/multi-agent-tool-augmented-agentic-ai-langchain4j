package com.agentic.ai.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("schiphol")
public record SchipholApiProperties(
        @NotBlank String baseUrl,
        @NotBlank String appId,
        @NotBlank String appKey,
        @NotBlank String resourceVersion
) {
}