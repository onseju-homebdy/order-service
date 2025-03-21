package com.onseju.orderservice.listener;

import com.onseju.orderservice.holding.service.dto.UpdateHoldingsParams;
import com.onseju.orderservice.order.domain.Order;
import com.onseju.orderservice.tradehistory.domain.TradeHistory;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public TradeHistory toTradeHistory(final TradeEvent tradeEvent) {
        return TradeHistory.builder()
                .companyCode(tradeEvent.companyCode())
                .sellOrderId(tradeEvent.sellOrderId())
                .buyOrderId(tradeEvent.buyOrderId())
                .price(tradeEvent.price())
                .quantity(tradeEvent.quantity())
                .tradeTime(tradeEvent.tradeAt())
                .build();
    }

    public UpdateHoldingsParams toUpdateHoldingsParams(final Order order, final TradeEvent tradeEvent) {
        return new UpdateHoldingsParams(
                order.getType(),
                order.getAccountId(),
                order.getCompanyCode(),
                tradeEvent.price(),
                tradeEvent.quantity()
        );
    }
}
