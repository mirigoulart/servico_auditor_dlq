package com.audit.dlq.infrastructure.messaging;

import com.audit.dlq.application.usecase.ProcessDlqMessageUseCase;
import com.audit.dlq.domain.model.AuditRecord;
import com.audit.dlq.infrastructure.config.AuditProperties;
import com.audit.dlq.infrastructure.messaging.mapper.DlqMessageMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Adaptador de entrada (Input Adapter) — camada de infraestrutura.
 *
 * Única responsabilidade: receber a mensagem bruta do SQS/DLQ, converter
 * para o formato do caso de uso e delegar o processamento.
 *
 * GARANTIA DE SEGURANÇA:
 * O acknowledgementMode = MANUAL garante que a mensagem só é removida
 * da DLQ APÓS o banco confirmar o commit. Se o salvamento falhar,
 * o acknowledgement não é chamado e a mensagem permanece na fila.
 */
@Component
public class DlqMessageListener {

    private static final Logger log = LoggerFactory.getLogger(DlqMessageListener.class);

    private final ProcessDlqMessageUseCase processUseCase;
    private final DlqMessageMapper dlqMessageMapper;
    private final AuditProperties auditProperties;

    public DlqMessageListener(ProcessDlqMessageUseCase processUseCase,
                               DlqMessageMapper dlqMessageMapper,
                               AuditProperties auditProperties) {
        this.processUseCase    = processUseCase;
        this.dlqMessageMapper  = dlqMessageMapper;
        this.auditProperties   = auditProperties;
    }

    @SqsListener(value = "${dlq.queue.name}", acknowledgementMode = "MANUAL")
    public void onMessage(String payload,
                          @Headers Map<String, Object> headers,
                          Acknowledgement acknowledgement) {

        log.info("Mensagem recebida da DLQ [{}]. Iniciando auditoria. Payload={}",
                auditProperties.getOriginalQueueName(), payload);

        try {
            List<Map<String, Object>> orderItems = dlqMessageMapper.extractOrderItems(payload);

            AuditRecord saved = processUseCase.execute(
                    auditProperties.getOriginalQueueName(),
                    payload,
                    orderItems
            );

            // Acknowledge somente após commit bem-sucedido no banco
            acknowledgement.acknowledgeAsync().join();

            log.info("Mensagem removida da DLQ com sucesso | errorId={} | severity={}",
                    saved.getErrorId(), saved.getSeverity());

        } catch (Exception e) {
            log.error("Erro ao auditar mensagem da DLQ. "
                    + "A mensagem NÃO será removida — permanecerá na fila.", e);
            throw new RuntimeException("Falha ao auditar mensagem da DLQ", e);
        }
    }
}
