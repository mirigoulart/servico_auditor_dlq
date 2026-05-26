package com.audit.dlq.infrastructure.rest.controller;

import com.audit.dlq.application.port.AuditRecordRepository;
import com.audit.dlq.infrastructure.rest.dto.AuditRecordResponseDTO;
import com.audit.dlq.infrastructure.rest.mapper.AuditRecordResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/audit-records")
public class AuditRecordController {

    private final AuditRecordRepository auditRecordRepository;

    public AuditRecordController(AuditRecordRepository auditRecordRepository) {
        this.auditRecordRepository = auditRecordRepository;
    }

    @GetMapping
    public ResponseEntity<List<AuditRecordResponseDTO>> listAll() {
        List<AuditRecordResponseDTO> response = auditRecordRepository.findAll()
                .stream()
                .map(AuditRecordResponseMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{errorId}")
    public ResponseEntity<AuditRecordResponseDTO> findById(@PathVariable UUID errorId) {
        return auditRecordRepository.findById(errorId)
                .map(AuditRecordResponseMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
