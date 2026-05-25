package com.audit.dlq.domain.model;

import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

/**
 * Entidade de domínio pura — sem anotações de framework.
 *
 * O factory method {@code create()} encapsula toda a lógica de criação:
 * - errorId sempre gerado como UUID aleatório
 * - status sempre inicializado como "PENDING_ANALYSIS"
 * - timestamp gerado no instante atual, com fuso de Brasília (America/Sao_Paulo)
 * - severity calculada externamente e injetada aqui
 */
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

    /**
     * Factory method que cria um novo registro de auditoria.
     * O timestamp é capturado no fuso America/Sao_Paulo e convertido para Instant (UTC).
     *
     * @param queueName nome fixo da fila de origem ("SQS_QUEUE")
     * @param payload   conteúdo bruto da mensagem em string
     * @param severity  severidade calculada pela regra de negócio
     * @return novo AuditRecord pronto para persistência
     */
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
