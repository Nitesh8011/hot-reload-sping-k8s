package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class ConfigurationChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationChangeListener.class);

    @Autowired
    private AppConfig appConfig;

    /**
     * This method will be called whenever the configuration is refreshed
     * due to the @RefreshScope annotation
     */
    public void logCurrentConfig() {
        logger.info("📋 Current configuration:");
        logger.info("  - Message: {}", appConfig.getMessage());
        logger.info("  - Environment: {}", appConfig.getEnvironment());
        logger.info("  - Refresh Count: {}", appConfig.getRefreshCount());
    }
}
