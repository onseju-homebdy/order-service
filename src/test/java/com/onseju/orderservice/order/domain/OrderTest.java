package com.onseju.orderservice.order.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

	private Account account;

	@BeforeEach
	void setUp() {
		account = new Account(1L , new BigDecimal("100000000"), new BigDecimal(0), 1L);
	}

	@Test
	@DisplayName("주문 생성 및 조회 테스트")
	void testOrderCreationAndRetrieval() {
		Long now = Instant.now().getEpochSecond();
		Order order = Order.builder()
				.companyCode("005930")
				.type(Type.LIMIT_BUY)
				.totalQuantity(new BigDecimal("100"))
				.remainingQuantity(new BigDecimal("100"))
				.status(OrderStatus.ACTIVE)
				.price(new BigDecimal("50000"))
				.timestamp(now)
				.createdDateTime(LocalDateTime.now())
				.updatedDateTime(LocalDateTime.now())
				.accountId(account.getId())
				.build();

		assertThat(order).isNotNull();
		assertThat(order.getCompanyCode()).isEqualTo("005930");
		assertThat(order.getType()).isEqualTo(Type.LIMIT_BUY);
		assertThat(order.getTotalQuantity()).isEqualByComparingTo(new BigDecimal("100"));
	}

	@Test
	@DisplayName("주문 수량 업데이트 테스트")
	void testDecreaseRemainingQuantity() {
		Long now = Instant.now().getEpochSecond();
		Order order = Order.builder()
				.companyCode("005930")
				.type(Type.LIMIT_BUY)
				.totalQuantity(new BigDecimal("100"))
				.remainingQuantity(new BigDecimal("100"))
				.status(OrderStatus.ACTIVE)
				.price(new BigDecimal("50000"))
				.timestamp(now)
				.createdDateTime(LocalDateTime.now())
				.updatedDateTime(LocalDateTime.now())
				.accountId(account.getId())
				.build();

		order.decreaseRemainingQuantity(new BigDecimal("20"));

		assertThat(order.getRemainingQuantity()).isEqualByComparingTo(new BigDecimal("80"));
	}

	@Test
	@DisplayName("주문수량 0 테스트")
	void testEdgeCases() {
		Long now = Instant.now().getEpochSecond();
		Order order = Order.builder()
				.companyCode("005930")
				.type(Type.LIMIT_BUY)
				.totalQuantity(BigDecimal.ZERO)
				.remainingQuantity(BigDecimal.ZERO)
				.status(OrderStatus.ACTIVE)
				.price(BigDecimal.ZERO)
				.timestamp(now)
				.createdDateTime(LocalDateTime.now())
				.updatedDateTime(LocalDateTime.now())
				.accountId(account.getId())
				.build();

		assertThat(order.getTotalQuantity()).isEqualByComparingTo(BigDecimal.ZERO);
		assertThat(order.getPrice()).isEqualByComparingTo(BigDecimal.ZERO);
	}

	@Test
	@DisplayName("빌더 toString 메서드 테스트")
	void testBuilderToString() {
		String builderString = Order.builder()
				.companyCode("005930")
				.type(Type.LIMIT_BUY)
				.totalQuantity(new BigDecimal("100"))
				.toString();
		assertThat(builderString).contains("companyCode", "type", "totalQuantity");
	}

	@Test
	@DisplayName("주문의 남은 수량이 0이 될 경우 Order Status를 Complete로 수정한다.")
	void changeStatusComplete() {
	    // given
		BigDecimal quantity = BigDecimal.valueOf(100);
		Long now = Instant.now().getEpochSecond();
		Order order = Order.builder()
				.companyCode("005930")
				.type(Type.LIMIT_BUY)
				.totalQuantity(quantity)
				.remainingQuantity(quantity)
				.status(OrderStatus.ACTIVE)
				.price(BigDecimal.ZERO)
				.timestamp(now)
				.createdDateTime(LocalDateTime.now())
				.updatedDateTime(LocalDateTime.now())
				.accountId(account.getId())
				.build();

	    // when
		order.decreaseRemainingQuantity(quantity);

	    // then
		assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETE);
	}

	@Test
	@DisplayName("주문한 Account의 Id값을 반환한다.")
	void getMemberId() {
	    // given
		Order order = Order.builder()
				.companyCode("005930")
				.accountId(account.getId())
				.build();

	    // when
	    Long accountId = order.getAccountId();

	    // then
		assertThat(accountId).isEqualTo(account.getId());
	}
}

