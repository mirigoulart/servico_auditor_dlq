package com.audit.dlq.domain.service;

import com.audit.dlq.domain.model.ErrorSeverity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Serviço de domínio responsável pela triagem de severidade.
 *
 * Regras de negócio (imutáveis):
 *  - total > 100  → HIGH
 *  - total >= 50  → MEDIUM
 *  - total < 50   → LOW
 *
 * Esta classe é um POJO puro, testável sem Spring Context, banco ou fila.
 */
@Component
public class SeverityClassifier {

    private static final int HIGH_THRESHOLD   = 100;
    private static final int MEDIUM_THRESHOLD = 50;

    /**
     * Calcula a severidade com base na soma total de 'amount' de todos os itens do pedido.
     *
     * @param orderItems lista de itens do pedido (Map com campo "amount")
     * @return severidade calculada
     */
    public ErrorSeverity classify(List<Map<String, Object>> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            return ErrorSeverity.LOW;
        }

        int totalItems = orderItems.stream()
                .mapToInt(item -> {
                    Object amount = item.get("amount");
                    if (amount instanceof Number) {
                        return ((Number) amount).intValue();
                    }
                    return 0;
                })
                .sum();

        if (totalItems > HIGH_THRESHOLD) {
            return ErrorSeverity.HIGH;
        } else if (totalItems >= MEDIUM_THRESHOLD) {
            return ErrorSeverity.MEDIUM;
        } else {
            return ErrorSeverity.LOW;
        }
    }
}
