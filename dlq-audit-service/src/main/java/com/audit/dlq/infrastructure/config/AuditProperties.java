package com.audit.dlq.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.audit")
public class AuditProperties {

    private String originalQueueName;

    public String getOriginalQueueName() {
        return originalQueueName;
    }

    public void setOriginalQueueName(String originalQueueName) {
        this.originalQueueName = originalQueueName;
    }
}
