package com.audit.dlq;

import com.audit.dlq.domain.model.ErrorSeverity;
import com.audit.dlq.domain.service.SeverityClassifier;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeverityClassifierTest {

    private final SeverityClassifier classifier = new SeverityClassifier();

    @Test
    void devRetornarHigh_quandoTotalMaiorQue100() {
        List<Map<String, Object>> items = List.of(
                Map.of("sku", 1, "amount", 60),
                Map.of("sku", 2, "amount", 50)
        );
        assertEquals(ErrorSeverity.HIGH, classifier.classify(items));
    }

    @Test
    void devRetornarMedium_quandoTotalEntre50e100Inclusive() {
        List<Map<String, Object>> items = List.of(
                Map.of("sku", 1, "amount", 30),
                Map.of("sku", 2, "amount", 70)
        );
        assertEquals(ErrorSeverity.HIGH, classifier.classify(items)); // 100 -> HIGH

        List<Map<String, Object>> items2 = List.of(
                Map.of("sku", 1, "amount", 50)
        );
        assertEquals(ErrorSeverity.MEDIUM, classifier.classify(items2)); // exatamente 50

        List<Map<String, Object>> items3 = List.of(
                Map.of("sku", 1, "amount", 75)
        );
        assertEquals(ErrorSeverity.MEDIUM, classifier.classify(items3)); // 75 -> MEDIUM
    }

    @Test
    void devRetornarLow_quandoTotalMenorQue50() {
        List<Map<String, Object>> items = List.of(
                Map.of("sku", 1, "amount", 5),
                Map.of("sku", 2, "amount", 3)
        );
        assertEquals(ErrorSeverity.LOW, classifier.classify(items));
    }

    @Test
    void devRetornarLow_quandoListaVaziaOuNula() {
        assertEquals(ErrorSeverity.LOW, classifier.classify(Collections.emptyList()));
        assertEquals(ErrorSeverity.LOW, classifier.classify(null));
    }
}
