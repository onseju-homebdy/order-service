package com.onseju.orderservice.listener;

import com.onseju.orderservice.holding.service.HoldingsService;
import com.onseju.orderservice.order.domain.Order;
import com.onseju.orderservice.order.service.repository.OrderRepository;
import com.onseju.orderservice.tradehistory.service.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TradeEventListener {

    private final TradeHistoryRepository tradeHistoryRepository;
    private final HoldingsService holdingsService;
    private final OrderRepository orderRepository;
    private final EventMapper eventMapper;

    @Async
    @TransactionalEventListener
    public void createTradeHistoryEvent(final TradeEvent tradeEvent) {
        // 1. 주문 내역 조회
        Order buyOrder = orderRepository.getById(tradeEvent.buyOrderId());
        Order sellOrder = orderRepository.getById(tradeEvent.sellOrderId());

        // 2. 체결 내역 저장
        tradeHistoryRepository.save(eventMapper.toTradeHistory(tradeEvent));

        // 3. 주문 내역에서 남은 양 차감
        updateRemainingQuantity(buyOrder, sellOrder, tradeEvent.quantity());

        // 4. 보유 주식 처리
        updateHoldings(sellOrder, buyOrder, tradeEvent);
    }

    private void updateRemainingQuantity(final Order buyOrder, final Order sellOrder, final BigDecimal quantity) {
        buyOrder.decreaseRemainingQuantity(quantity);
        sellOrder.decreaseRemainingQuantity(quantity);
    }

    private void updateHoldings(final Order buyOrder, final Order sellOrder, final TradeEvent event) {
        holdingsService.updateHoldingsAfterTrade(eventMapper.toUpdateHoldingsParams(buyOrder, event));
        holdingsService.updateHoldingsAfterTrade(eventMapper.toUpdateHoldingsParams(sellOrder, event));
    }
}