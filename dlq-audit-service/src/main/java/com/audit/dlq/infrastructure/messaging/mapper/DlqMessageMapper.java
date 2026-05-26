package com.audit.dlq.infrastructure.messaging.mapper;

import com.audit.dlq.infrastructure.messaging.dto.OrderEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DlqMessageMapper {

    private static final Logger log = LoggerFactory.getLogger(DlqMessageMapper.class);

    private final ObjectMapper objectMapper;

    public DlqMessageMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String extractOrigin(String payload) {
        try {
            OrderEventDTO event = objectMapper.readValue(payload, OrderEventDTO.class);
            if (event == null || event.getOrigin() == null || event.getOrigin().isBlank()) {
                log.warn("Campo 'origin' ausente ou vazio no payload da DLQ. Usando fallback 'UNKNOWN_QUEUE'.");
                return "UNKNOWN_QUEUE";
            }
            return event.getOrigin();
        } catch (Exception e) {
            log.warn("Não foi possível extrair o campo 'origin' do payload da DLQ. Usando fallback 'UNKNOWN_QUEUE'.", e);
            return "UNKNOWN_QUEUE";
        }
    }

    public List<Map<String, Object>> extractOrderItems(String payload) {
        try {
            OrderEventDTO event = objectMapper.readValue(payload, OrderEventDTO.class);
            if (event == null || event.getOrderItems() == null) {
                return Collections.emptyList();
            }

            return event.getOrderItems().stream()
                    .filter(Objects::nonNull)
                    .map(item -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("sku", item.getSku());
                        map.put("amount", item.getAmount());
                        return map;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.warn("Não foi possível interpretar o payload da DLQ para extrair orderItems. "
                    + "Severidade será LOW por padrão.", e);
            return Collections.emptyList();
        }
    }
}