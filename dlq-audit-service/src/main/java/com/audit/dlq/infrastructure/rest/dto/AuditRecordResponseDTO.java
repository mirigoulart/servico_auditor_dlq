package com.audit.dlq.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.audit.dlq.domain.model.ErrorSeverity;

import java.util.UUID;

@JsonPropertyOrder({"errorId", "queueName", "payload", "timestamp", "status", "severity"})
public class AuditRecordResponseDTO {

    private UUID errorId;
    private String queueName;
    private String payload;
    private String timestamp;
    private String status;
    private ErrorSeverity severity;

    public UUID getErrorId()               { return errorId; }
    public void setErrorId(UUID v)         { this.errorId = v; }

    public String getQueueName()           { return queueName; }
    public void setQueueName(String v)     { this.queueName = v; }

    public String getPayload()             { return payload; }
    public void setPayload(String v)       { this.payload = v; }

    public String getTimestamp()           { return timestamp; }
    public void setTimestamp(String v)     { this.timestamp = v; }

    public String getStatus()              { return status; }
    public void setStatus(String v)        { this.status = v; }

    public ErrorSeverity getSeverity()          { return severity; }
    public void setSeverity(ErrorSeverity v)    { this.severity = v; }
}
