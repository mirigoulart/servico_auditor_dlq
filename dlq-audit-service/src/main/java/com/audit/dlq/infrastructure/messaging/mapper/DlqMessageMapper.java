package com.audit.dlq.infrastructure.messaging.mapper;

import com.audit.dlq.infrastructure.messaging.dto.OrderEventDTO;
import com.audit.dlq.infrastructure.messaging.dto.OrderItemDTO;
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

/**
 * Mapper de infraestrutura de mensageria.
 *
 * Converte o payload bruto da DLQ (String JSON) em estruturas que o
 * caso de uso consegue processar, sem expor detalhes de desserialização
 * ao domínio ou à camada de aplicação.
 */
@Component
public class DlqMessageMapper {

    private static final Logger log = LoggerFactory.getLogger(DlqMessageMapper.class);

    private final ObjectMapper objectMapper;

    public DlqMessageMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Extrai a lista de itens do pedido como List<Map<String, Object>>,
     * formato esperado pelo {@code SeverityClassifier}.
     *
     * Se o payload não puder ser interpretado, retorna lista vazia
     * (severidade será LOW por definição).
     */
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
