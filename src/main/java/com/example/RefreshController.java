package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class RefreshController {
    
    @Autowired
    private ContextRefresher contextRefresher;
    
    @PostMapping("/refresh")
    public String refresh() {
        Set<String> refreshedKeys = contextRefresher.refresh();
        return String.format("Configuration refreshed. Refreshed keys: %s", refreshedKeys);
    }
    
    @PostMapping("/refresh-config")
    public String refreshConfig() {
        Set<String> refreshedKeys = contextRefresher.refresh();
        return String.format("Configuration refreshed successfully! Refreshed keys: %s", refreshedKeys);
    }
}
