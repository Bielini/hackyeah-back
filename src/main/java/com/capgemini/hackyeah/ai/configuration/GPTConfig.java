package com.capgemini.hackyeah.ai.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("gpt")
public class GPTConfig {
    private String secret;
    private String url;
}
