package com.audit.dlq.application.port;

import com.audit.dlq.domain.model.AuditRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta de saída (Output Port) da Arquitetura Hexagonal.
 *
 * Definida pela camada de aplicação — o domínio dita o contrato,
 * a infraestrutura implementa. Isso garante que detalhes técnicos
 * (JPA, banco de dados) nunca vazem para o núcleo do hexágono.
 */
public interface AuditRecordRepository {

    /**
     * Persiste um registro de auditoria de forma segura.
     * A mensagem da DLQ só deve ser confirmada (acknowledged) APÓS
     * este método retornar com sucesso.
     */
    AuditRecord save(AuditRecord record);

    List<AuditRecord> findAll();

    Optional<AuditRecord> findById(UUID errorId);
}
