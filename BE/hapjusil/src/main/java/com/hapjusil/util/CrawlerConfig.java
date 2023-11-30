package com.hapjusil.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "custom.crawler")
public class CrawlerConfig {

    private String path;

    // getter와 setter
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}