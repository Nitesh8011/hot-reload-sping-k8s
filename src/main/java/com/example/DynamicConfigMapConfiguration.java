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

    // By default load both sample ConfigMaps; override CONFIGMAP_NAMES to customize
    @Value("${CONFIGMAP_NAMES:hot-reload-cm,hot-reload-cm-2}")
    private String configMapNames;

    @Value("${NAMESPACE:default}")
    private String namespace;

    @Bean
    @Primary
    public ConfigMapConfigProperties configMapConfigProperties() {
        // Parse CONFIGMAP_NAMES and create sources dynamically
        List<ConfigMapConfigProperties.Source> sources = new ArrayList<>();

        if (configMapNames != null && !configMapNames.isBlank()) {
            String[] names = configMapNames.split(",");
            for (String name : names) {
                String trimmedName = name.trim();
                if (!trimmedName.isEmpty()) {
                    ConfigMapConfigProperties.Source source = new ConfigMapConfigProperties.Source(
                        trimmedName,
                        namespace,
                        Map.of(),
                        "",
                        false,
                        false
                    );
                    sources.add(source);
                }
            }
        }

        return new ConfigMapConfigProperties(
            true,
            List.of(),
            sources,
            Map.of(),
            false,
            namespace,
            "",
            false,
            false,
            false,
            null
        );
    }
}
