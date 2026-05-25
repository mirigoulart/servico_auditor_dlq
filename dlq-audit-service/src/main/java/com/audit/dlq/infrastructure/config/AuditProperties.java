package com.audit.dlq.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propriedades de configuração do serviço de auditoria.
 * Lidas do application.properties sob o prefixo "app.audit".
 */
@ConfigurationProperties(prefix = "app.audit")
public class AuditProperties {

    /** Nome fixo que identifica a fila de origem nos registros de auditoria. */
    private String originalQueueName;

    public String getOriginalQueueName() {
        return originalQueueName;
    }

    public void setOriginalQueueName(String originalQueueName) {
        this.originalQueueName = originalQueueName;
    }
}
