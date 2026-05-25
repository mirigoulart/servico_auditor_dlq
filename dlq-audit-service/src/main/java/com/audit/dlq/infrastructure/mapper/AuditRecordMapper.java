package com.audit.dlq.infrastructure.mapper;

import com.audit.dlq.domain.model.AuditRecord;
import com.audit.dlq.domain.model.ErrorSeverity;
import com.audit.dlq.infrastructure.persistence.entity.AuditRecordEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper de infraestrutura: converte entre {@link AuditRecord} (domínio)
 * e {@link AuditRecordEntity} (JPA).
 *
 * Mantido separado para que nem o domínio nem a entidade JPA
 * precisem conhecer a estrutura um do outro.
 */
@Component
public class AuditRecordMapper {

    public AuditRecordEntity toEntity(AuditRecord domain) {
        return new AuditRecordEntity(
                domain.getErrorId(),
                domain.getQueueName(),
                domain.getPayload(),
                domain.getTimestamp(),
                domain.getStatus(),
                domain.getSeverity()
        );
    }

    public AuditRecord toDomain(AuditRecordEntity entity) {
        return AuditRecord.create(
                entity.getQueueName(),
                entity.getPayload(),
                entity.getSeverity()
        );
    }
}
