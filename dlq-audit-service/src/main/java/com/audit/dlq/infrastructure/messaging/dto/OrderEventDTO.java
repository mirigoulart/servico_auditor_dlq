package com.audit.dlq.infrastructure.messaging.dto;

import java.util.List;

/**
 * DTO de entrada que representa a estrutura da mensagem SQS recebida.
 * Usado exclusivamente para desserialização do payload bruto.
 */
public class OrderEventDTO {

    private String zipCode;
    private Integer customerId;
    private List<OrderItemDTO> orderItems;
    private String origin;
    private String occurredAt;

    public String getZipCode()                    { return zipCode; }
    public void setZipCode(String zipCode)        { this.zipCode = zipCode; }

    public Integer getCustomerId()                { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public List<OrderItemDTO> getOrderItems()              { return orderItems; }
    public void setOrderItems(List<OrderItemDTO> items)    { this.orderItems = items; }

    public String getOrigin()                     { return origin; }
    public void setOrigin(String origin)          { this.origin = origin; }

    public String getOccurredAt()                 { return occurredAt; }
    public void setOccurredAt(String occurredAt)  { this.occurredAt = occurredAt; }
}
