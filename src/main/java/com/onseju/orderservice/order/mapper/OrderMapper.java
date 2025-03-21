package com.onseju.orderservice.order.mapper;

import com.onseju.orderservice.order.domain.Order;
import com.onseju.orderservice.order.domain.OrderStatus;
import com.onseju.orderservice.order.service.dto.CreateOrderParams;
import com.onseju.orderservice.order.service.dto.OrderEvent;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class OrderMapper {

    public Order toEntity(final CreateOrderParams params, final Long accountId) {
        return Order.builder()
                .companyCode(params.companyCode())
                .type(params.type())
                .totalQuantity(params.totalQuantity())
                .remainingQuantity(params.totalQuantity())
                .status(OrderStatus.ACTIVE)
                .price(params.price())
                .accountId(accountId)
                .timestamp(params.now().toEpochSecond(ZoneOffset.UTC))
                .build();
    }

    public OrderEvent toEvent(final Order order) {
        return new OrderEvent(
                order.getId(),
                order.getCompanyCode(),
                order.getType(),
                order.getStatus(),
                order.getTotalQuantity(),
                order.getRemainingQuantity(),
                order.getPrice(),
                order.getCreatedDateTime(),
                order.getAccountId()
        );
    }
}
