package com.audit.dlq;

import com.audit.dlq.infrastructure.config.AuditProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AuditProperties.class)
public class DlqAuditServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DlqAuditServiceApplication.class, args);
    }
}
