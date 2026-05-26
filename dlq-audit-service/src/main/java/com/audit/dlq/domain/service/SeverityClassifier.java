package com.audit.dlq.domain.service;

import com.audit.dlq.domain.model.ErrorSeverity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SeverityClassifier {

    private static final int HIGH_THRESHOLD   = 100;
    private static final int MEDIUM_THRESHOLD = 50;

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
