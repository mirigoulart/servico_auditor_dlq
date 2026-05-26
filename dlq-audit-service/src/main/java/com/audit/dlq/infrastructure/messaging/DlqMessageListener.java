package com.audit.dlq.infrastructure.messaging;

import com.audit.dlq.application.usecase.ProcessDlqMessageUseCase;
import com.audit.dlq.domain.model.AuditRecord;
import com.audit.dlq.infrastructure.messaging.mapper.DlqMessageMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DlqMessageListener {

    private static final Logger log = LoggerFactory.getLogger(DlqMessageListener.class);

    private final ProcessDlqMessageUseCase processUseCase;
    private final DlqMessageMapper dlqMessageMapper;

    public DlqMessageListener(ProcessDlqMessageUseCase processUseCase,
                              DlqMessageMapper dlqMessageMapper) {
        this.processUseCase   = processUseCase;
        this.dlqMessageMapper = dlqMessageMapper;
    }

    @SqsListener(value = "${dlq.queue.name}", acknowledgementMode = "MANUAL")
    public void onMessage(String payload,
                          @Headers Map<String, Object> headers,
                          Acknowledgement acknowledgement) {

        String queueName = dlqMessageMapper.extractOrigin(payload);

        log.info("Mensagem recebida da DLQ [{}]. Iniciando auditoria. Payload={}",
                queueName, payload);

        try {
            List<Map<String, Object>> orderItems = dlqMessageMapper.extractOrderItems(payload);

            AuditRecord saved = processUseCase.execute(
                    queueName,
                    payload,
                    orderItems
            );

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