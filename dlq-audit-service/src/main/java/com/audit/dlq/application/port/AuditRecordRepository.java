package com.audit.dlq.application.port;

import com.audit.dlq.domain.model.AuditRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuditRecordRepository {

    AuditRecord save(AuditRecord record);

    List<AuditRecord> findAll();

    Optional<AuditRecord> findById(UUID errorId);
}
