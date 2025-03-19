package com.onseju.orderservice.order.controller.request;

import com.onseju.orderservice.order.domain.OrderStatus;
import com.onseju.orderservice.order.domain.Type;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderRequest(
		String companyCode,
		Type type,
		BigDecimal totalQuantity,
		BigDecimal remainingQuantity,
		OrderStatus status,
		BigDecimal price,
		Long accountId,
		String userName
) {

}
