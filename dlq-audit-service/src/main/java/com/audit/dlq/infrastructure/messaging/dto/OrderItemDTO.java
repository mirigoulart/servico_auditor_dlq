package com.audit.dlq.infrastructure.messaging.dto;

public class OrderItemDTO {

    private Integer sku;
    private Integer amount;

    public Integer getSku()              { return sku; }
    public void setSku(Integer sku)      { this.sku = sku; }

    public Integer getAmount()           { return amount; }
    public void setAmount(Integer amount){ this.amount = amount; }
}
