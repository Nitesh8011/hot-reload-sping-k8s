package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private String message = "Default message";
    private String environment = "default";
    private int refreshCount = 0;

    // Getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    public int getRefreshCount() { return refreshCount; }
    public void setRefreshCount(int refreshCount) { this.refreshCount = refreshCount; }
}
