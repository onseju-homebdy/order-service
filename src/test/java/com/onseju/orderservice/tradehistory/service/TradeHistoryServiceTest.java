package com.onseju.orderservice.tradehistory.service;

import com.onseju.orderservice.order.domain.Account;
import com.onseju.orderservice.order.domain.Order;
import com.onseju.orderservice.order.domain.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

/**
 * TradeHistoryService 클래스에 대한 단위 테스트
 */

@ExtendWith(MockitoExtension.class)
class TradeHistoryServiceTest {

	@InjectMocks
	private TradeHistoryService tradeHistoryService;

	@Mock
	private TradeHistoryRepository tradeHistoryRepository;

	private Account account;
	private Order order;

	@BeforeEach
	void setUp() {
		account = new Account(1L , new BigDecimal("100000000"), new BigDecimal(0), 1L);
		order = Order.builder().id(1L).remainingQuantity(new BigDecimal(10)).accountId(account.getId()).type(Type.LIMIT_BUY).build();
	}


}
