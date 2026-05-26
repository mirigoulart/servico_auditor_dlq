package com.audit.dlq.infrastructure.persistence.repository;

import com.audit.dlq.application.port.AuditRecordRepository;
import com.audit.dlq.domain.model.AuditRecord;
import com.audit.dlq.infrastructure.mapper.AuditRecordMapper;
import com.audit.dlq.infrastructure.persistence.entity.AuditRecordEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaAuditRecordRepository implements AuditRecordRepository {

    private final SpringDataAuditRepository springDataRepo;
    private final AuditRecordMapper mapper;

    public JpaAuditRecordRepository(SpringDataAuditRepository springDataRepo,
                                    AuditRecordMapper mapper) {
        this.springDataRepo = springDataRepo;
        this.mapper         = mapper;
    }

    @Override
    public AuditRecord save(AuditRecord record) {
        AuditRecordEntity entity      = mapper.toEntity(record);
        AuditRecordEntity savedEntity = springDataRepo.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<AuditRecord> findAll() {
        return springDataRepo.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AuditRecord> findById(UUID errorId) {
        return springDataRepo.findById(errorId)
                .map(mapper::toDomain);
    }
}
