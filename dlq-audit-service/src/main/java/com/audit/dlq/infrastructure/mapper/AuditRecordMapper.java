package com.audit.dlq.infrastructure.mapper;

import com.audit.dlq.domain.model.AuditRecord;
import com.audit.dlq.domain.model.ErrorSeverity;
import com.audit.dlq.infrastructure.persistence.entity.AuditRecordEntity;
import org.springframework.stereotype.Component;

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
