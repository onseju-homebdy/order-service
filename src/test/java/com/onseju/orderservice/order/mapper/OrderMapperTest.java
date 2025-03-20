package com.onseju.orderservice.order.mapper;

import com.onseju.orderservice.order.controller.request.OrderRequest;
import com.onseju.orderservice.order.domain.Order;
import com.onseju.orderservice.order.domain.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class OrderMapperTest {

    private final OrderMapper orderMapper = new OrderMapper();

    @Test
    @DisplayName("OrderRequest를 Entity로 변환한다.")
    void toEntity() {
        // given
        String companyCode = "005930";
        OrderRequest orderRequest = new OrderRequest(companyCode, Type.LIMIT_BUY, new BigDecimal("100"), new BigDecimal("1000"), LocalDateTime.now());

        // when
        Order order = orderMapper.toEntity(orderRequest, 1L);

        // then
        assertThat(order).isNotNull();
        assertThat(order.getCompanyCode()).isEqualTo(companyCode);
    }
}