package com.audit.dlq.infrastructure.persistence.repository;

import com.audit.dlq.infrastructure.persistence.entity.AuditRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repositório Spring Data JPA — detalhe técnico de infraestrutura.
 * Nunca exposto diretamente ao domínio.
 */
public interface SpringDataAuditRepository extends JpaRepository<AuditRecordEntity, UUID> {
}
