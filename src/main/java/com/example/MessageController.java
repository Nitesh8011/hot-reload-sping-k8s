package com.example;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class MessageController {


    
    @Autowired
    private AppConfig appConfig;
    
    @Autowired
    private ConfigurationChangeListener configListener;

    @GetMapping("/message")
    public String getMessage() {
        return appConfig.getMessage();
    }
    
    @GetMapping("/config")
    public String getConfig() {
        try {
            return String.format("Configuration: message='%s', environment='%s', refreshCount=%d",
                               appConfig.getMessage(), appConfig.getEnvironment(), appConfig.getRefreshCount());
        } catch (Exception e) {
            return "Error accessing configuration: " + e.getMessage();
        }
    }
    
    @GetMapping("/health")
    public String getHealth() {
        return "Application is running with message: " + appConfig.getMessage();
    }
    
    @GetMapping("/reload-info")
    public String getReloadInfo() {
        return String.format("Last reload count: %d, Environment: %s", 
                           appConfig.getRefreshCount(), appConfig.getEnvironment());
    }
    
    @GetMapping("/log-config")
    public String logCurrentConfig() {
        configListener.logCurrentConfig();
        return "Configuration logged to console. Check application logs.";
    }
}