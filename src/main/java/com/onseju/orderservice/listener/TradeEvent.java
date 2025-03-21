package com.onseju.orderservice.listener;

import java.math.BigDecimal;

public record TradeEvent(
        String companyCode,
        Long buyOrderId,
        Long sellOrderId,
        BigDecimal quantity,
        BigDecimal price,
        Long tradeAt
) {
}
