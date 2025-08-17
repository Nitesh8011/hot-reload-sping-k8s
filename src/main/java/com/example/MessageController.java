package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    // Properties from ConfigMap 1 (hot-reload-cm)
    @Value("${app.cm1.message:No message from ConfigMap 1}")
    private String configMap1Message;
    
    @Value("${app.environment:No environment from ConfigMap 1}")
    private String configMap1Environment;
    
    @Value("${app.refreshCount:0}")
    private int configMap1RefreshCount;

    // Properties from ConfigMap 2 (hot-reload-cm-2)
    @Value("${app.cm2.message:No message from ConfigMap 2}")
    private String configMap2Message;
    
    @Value("${app.database.url:No database URL from ConfigMap 2}")
    private String configMap2DatabaseUrl;
    
    @Value("${app.database.pool.size:No pool size from ConfigMap 2}")
    private String configMap2PoolSize;
    
    @Value("${app.database.timeout:No timeout from ConfigMap 2}")
    private String configMap2Timeout;
    
    @Value("${app.cache.ttl:No cache TTL from ConfigMap 2}")
    private String configMap2CacheTtl;
    
    @Value("${app.cache.max-size:No max size from ConfigMap 2}")
    private String configMap2MaxSize;
    
    @Value("${app.features.advanced-logging:No feature flag from ConfigMap 2}")
    private String configMap2AdvancedLogging;
    
    @Value("${app.features.metrics:No metrics flag from ConfigMap 2}")
    private String configMap2Metrics;

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
    
    @GetMapping("/configmaps")
    public String getBothConfigMaps() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CONFIGMAP 1 (hot-reload-cm) ===\n");
        sb.append("Message: ").append(configMap1Message).append("\n");
        sb.append("Environment: ").append(configMap1Environment).append("\n");
        sb.append("Refresh Count: ").append(configMap1RefreshCount).append("\n");
        
        sb.append("\n=== CONFIGMAP 2 (hot-reload-cm-2) ===\n");
        sb.append("Message: ").append(configMap2Message).append("\n");
        sb.append("Database URL: ").append(configMap2DatabaseUrl).append("\n");
        sb.append("Database Pool Size: ").append(configMap2PoolSize).append("\n");
        sb.append("Database Timeout: ").append(configMap2Timeout).append("\n");
        sb.append("Cache TTL: ").append(configMap2CacheTtl).append("\n");
        sb.append("Cache Max Size: ").append(configMap2MaxSize).append("\n");
        sb.append("Advanced Logging: ").append(configMap2AdvancedLogging).append("\n");
        sb.append("Metrics: ").append(configMap2Metrics).append("\n");
        
        sb.append("\n=== APP CONFIG (Combined) ===\n");
        sb.append("Message: ").append(appConfig.getMessage()).append("\n");
        sb.append("Environment: ").append(appConfig.getEnvironment()).append("\n");
        sb.append("Refresh Count: ").append(appConfig.getRefreshCount()).append("\n");
        
        return sb.toString();
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

    @GetMapping("/debug")
    public String debugConfig() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DEBUG CONFIGURATION ===\n\n");
        
        sb.append("Environment Variables:\n");
        sb.append("CONFIGMAP_NAMES: ").append(System.getenv("CONFIGMAP_NAMES")).append("\n");
        sb.append("NAMESPACE: ").append(System.getenv("NAMESPACE")).append("\n");
        sb.append("SPRING_PROFILES_ACTIVE: ").append(System.getenv("SPRING_PROFILES_ACTIVE")).append("\n\n");
        
        sb.append("ConfigMap 1 Properties:\n");
        sb.append("app.cm1.message: ").append(configMap1Message).append("\n");
        sb.append("app.environment: ").append(configMap1Environment).append("\n");
        sb.append("app.refreshCount: ").append(configMap1RefreshCount).append("\n\n");
        
        sb.append("ConfigMap 2 Properties:\n");
        sb.append("app.cm2.message: ").append(configMap2Message).append("\n");
        sb.append("app.database.url: ").append(configMap2DatabaseUrl).append("\n");
        sb.append("app.database.pool.size: ").append(configMap2PoolSize).append("\n");
        sb.append("app.database.timeout: ").append(configMap2Timeout).append("\n");
        sb.append("app.cache.ttl: ").append(configMap2CacheTtl).append("\n");
        sb.append("app.cache.max-size: ").append(configMap2MaxSize).append("\n");
        sb.append("app.features.advanced-logging: ").append(configMap2AdvancedLogging).append("\n");
        sb.append("app.features.metrics: ").append(configMap2Metrics).append("\n\n");
        
        sb.append("AppConfig Properties:\n");
        sb.append("app.message: ").append(appConfig.getMessage()).append("\n");
        sb.append("app.environment: ").append(appConfig.getEnvironment()).append("\n");
        sb.append("app.refreshCount: ").append(appConfig.getRefreshCount()).append("\n");
        
        return sb.toString();
    }

    @GetMapping("/test-cm1")
    public String testConfigMap1() {
        return "ConfigMap 1 Message: " + configMap1Message;
    }
    
    @GetMapping("/test-cm2")
    public String testConfigMap2() {
        return "ConfigMap 2 Message: " + configMap2Message;
    }

    @GetMapping("/manual-refresh")
    public String manualRefresh() {
        return "Manual refresh endpoint. Use /actuator/refresh instead for proper configuration refresh.";
    }

    @GetMapping("/env-test")
    public String testEnvironment() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ENVIRONMENT TEST ===\n\n");
        
        // Test if ConfigMap properties are available as environment variables
        sb.append("Environment Variables:\n");
        sb.append("CONFIGMAP_NAMES: ").append(System.getenv("CONFIGMAP_NAMES")).append("\n");
        sb.append("NAMESPACE: ").append(System.getenv("NAMESPACE")).append("\n");
        sb.append("SPRING_PROFILES_ACTIVE: ").append(System.getenv("SPRING_PROFILES_ACTIVE")).append("\n\n");
        
        // Test if Spring properties are being loaded
        sb.append("Spring Properties:\n");
        sb.append("app.cm1.message: ").append(configMap1Message).append("\n");
        sb.append("app.database.url: ").append(configMap2DatabaseUrl).append("\n");
        
        return sb.toString();
    }
}