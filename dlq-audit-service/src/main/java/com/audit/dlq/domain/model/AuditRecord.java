package com.audit.dlq.domain.model;

import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

public class AuditRecord {

    private static final ZoneId BRAZIL_ZONE = ZoneId.of("America/Sao_Paulo");

    private final UUID errorId;
    private final String queueName;
    private final String payload;
    private final Instant timestamp;
    private final String status;
    private final ErrorSeverity severity;

    private AuditRecord(UUID errorId, String queueName, String payload,
                        Instant timestamp, String status, ErrorSeverity severity) {
        this.errorId = errorId;
        this.queueName = queueName;
        this.payload = payload;
        this.timestamp = timestamp;
        this.status = status;
        this.severity = severity;
    }

    public static AuditRecord create(String queueName, String payload, ErrorSeverity severity) {
        return new AuditRecord(
                UUID.randomUUID(),
                queueName,
                payload,
                Instant.now().atZone(BRAZIL_ZONE).toInstant(),
                "PENDING_ANALYSIS",
                severity
        );
    }

    public UUID getErrorId()        { return errorId; }
    public String getQueueName()    { return queueName; }
    public String getPayload()      { return payload; }
    public Instant getTimestamp()   { return timestamp; }
    public String getStatus()       { return status; }
    public ErrorSeverity getSeverity() { return severity; }
}
