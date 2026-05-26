package com.audit.dlq.infrastructure.rest.mapper;

import com.audit.dlq.domain.model.AuditRecord;
import com.audit.dlq.infrastructure.rest.dto.AuditRecordResponseDTO;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AuditRecordResponseMapper {

    private static final ZoneId BRAZIL_ZONE = ZoneId.of("America/Sao_Paulo");

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
                    .withZone(BRAZIL_ZONE);

    private AuditRecordResponseMapper() {}

    public static AuditRecordResponseDTO toResponse(AuditRecord record) {
        AuditRecordResponseDTO dto = new AuditRecordResponseDTO();
        dto.setErrorId(record.getErrorId());
        dto.setQueueName(record.getQueueName());
        dto.setPayload(record.getPayload());
        dto.setTimestamp(FORMATTER.format(record.getTimestamp()));
        dto.setStatus(record.getStatus());
        dto.setSeverity(record.getSeverity());
        return dto;
    }
}
