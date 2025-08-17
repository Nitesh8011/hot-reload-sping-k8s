package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.kubernetes.commons.config.ConfigMapConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class DynamicConfigMapConfiguration {

    @Value("${CONFIGMAP_NAMES:hot-reload-cm,hot-reload-cm-2}")
    private String configMapNames;

    @Value("${NAMESPACE:default}")
    private String namespace;

    @Bean
    // @Primary  // Temporarily disabled to test hardcoded sources
    public ConfigMapConfigProperties configMapConfigProperties() {
        // Parse CONFIGMAP_NAMES and create sources dynamically
        List<ConfigMapConfigProperties.Source> sources = new ArrayList<>();
        String[] names = configMapNames.split(",");
        
        for (String name : names) {
            String trimmedName = name.trim();
            if (!trimmedName.isEmpty()) {
                // Create Source using the record constructor with all required parameters
                ConfigMapConfigProperties.Source source = new ConfigMapConfigProperties.Source(
                    trimmedName,           // name
                    namespace,             // namespace
                    Map.of(),             // labels (empty map)
                    "",                   // path
                    false,                // useNameAsPrefix
                    false                 // include
                );
                sources.add(source);
            }
        }
        
        // Create ConfigMapConfigProperties using the record constructor with all required parameters
        return new ConfigMapConfigProperties(
            false,                        // enabled
            List.of(),                    // include
            sources,                      // sources
            Map.of(),                     // labels
            false,                        // useNameAsPrefix
            namespace,                    // namespace
            "",                          // path
            false,                       // include
            false,                       // failFast
            false,                       // retry
            null                         // retryProperties
        );
    }
}
