package com.audit.dlq.application.usecase;

import com.audit.dlq.application.port.AuditRecordRepository;
import com.audit.dlq.domain.model.AuditRecord;
import com.audit.dlq.domain.model.ErrorSeverity;
import com.audit.dlq.domain.service.SeverityClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Caso de uso: auditar uma mensagem proveniente da DLQ.
 *
 * Orquestra o fluxo sem conter regras de negócio:
 *  1. Delega o cálculo de severidade ao SeverityClassifier (domínio)
 *  2. Cria o AuditRecord via factory method (domínio)
 *  3. Persiste via porta de saída (infraestrutura)
 *
 * A anotação @Transactional garante que o commit no banco ocorre
 * ANTES de o listener confirmar (acknowledge) a mensagem na DLQ.
 */
@Service
public class ProcessDlqMessageUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessDlqMessageUseCase.class);

    private final SeverityClassifier severityClassifier;
    private final AuditRecordRepository auditRecordRepository;

    public ProcessDlqMessageUseCase(SeverityClassifier severityClassifier,
                                    AuditRecordRepository auditRecordRepository) {
        this.severityClassifier = severityClassifier;
        this.auditRecordRepository = auditRecordRepository;
    }

    @Transactional
    public AuditRecord execute(String queueName, String rawPayload,
                               List<Map<String, Object>> orderItems) {

        log.info("Iniciando auditoria da mensagem. Fila de origem: {}", queueName);

        ErrorSeverity severity = severityClassifier.classify(orderItems);
        log.info("Severidade calculada: {}", severity);

        AuditRecord record = AuditRecord.create(queueName, rawPayload, severity);

        AuditRecord saved = auditRecordRepository.save(record);
        log.info("Registro de auditoria salvo | errorId={} | status={} | severity={}",
                saved.getErrorId(), saved.getStatus(), saved.getSeverity());

        return saved;
    }
}
