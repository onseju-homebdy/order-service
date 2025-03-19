package com.onseju.orderservice.order.mapper;

import com.onseju.orderservice.order.controller.request.OrderRequest;
import com.onseju.orderservice.order.domain.Order;
import com.onseju.orderservice.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class OrderMapper {

    public Order toEntity(final OrderRequest request, final Long accountId) {
        return Order.builder()
                .companyCode(request.companyCode())
                .type(request.type())
                .totalQuantity(request.totalQuantity())
                .remainingQuantity(request.totalQuantity())
                .status(OrderStatus.ACTIVE)
                .price(request.price())
                .accountId(accountId)
                .timestamp(request.now().toEpochSecond(ZoneOffset.UTC))
                .build();
    }
}
