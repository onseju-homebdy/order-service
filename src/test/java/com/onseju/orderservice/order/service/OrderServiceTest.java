package com.onseju.orderservice.order.service;

import com.onseju.orderservice.company.domain.Company;
import com.onseju.orderservice.company.service.CompanyRepository;
import com.onseju.orderservice.holding.repository.HoldingsRepositoryImpl;
import com.onseju.orderservice.order.controller.request.OrderRequest;
import com.onseju.orderservice.order.domain.Account;
import com.onseju.orderservice.order.domain.Type;
import com.onseju.orderservice.order.exception.OrderPriceQuotationException;
import com.onseju.orderservice.order.exception.PriceOutOfRangeException;
import com.onseju.orderservice.order.mapper.OrderMapper;
import com.onseju.orderservice.order.service.repository.AccountRepository;
import com.onseju.orderservice.order.service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@InjectMocks
	OrderService orderService;

	@Mock
	AccountRepository accountRepository;

	@Mock
	CompanyRepository companyRepository;

	@Mock
	HoldingsRepositoryImpl holdingsRepository;

	@Mock
	OrderRepository orderRepository;

	OrderMapper orderMapper = new OrderMapper();

	private final Company company = Company.builder().isuNm("005930").isuCd("005930").closingPrice(new BigDecimal("1000")).build();
	private Account account;

	@BeforeEach
	void setUp() {
		orderService = new OrderService(orderRepository, companyRepository, holdingsRepository, accountRepository, orderMapper);
		account = new Account(1L , new BigDecimal("100000000"), new BigDecimal(0), 1L);
	}

	@Nested
	class CreateOrderTest {

		@Test
		@DisplayName("TC20.2.1 주문 생성 테스트")
		void testPlaceOrder() {
			OrderRequest request = createOrderRequest(Type.LIMIT_BUY, new BigDecimal(1), new BigDecimal(1000));
			when(companyRepository.findByIsuSrtCd(any())).thenReturn(company);
			when(accountRepository.getByMemberId(any())).thenReturn(account);

			orderService.placeOrder(request, 1L);
		}
	}

	@Nested
	class BoundaryTests {
		@Test
		@DisplayName("입력된 가격이 종가 기준 상향 30% 이상일 경우 정상적으로 처리한다.")
		void placeOrderWhenPriceWithinUpperLimit() {
			// given
			BigDecimal price = new BigDecimal(1300);
			OrderRequest request = createOrderRequest(Type.LIMIT_BUY, BigDecimal.valueOf(10), price);
			when(companyRepository.findByIsuSrtCd(any())).thenReturn(company);
			when(accountRepository.getByMemberId(any())).thenReturn(account);

			// when, then
			assertThatNoException().isThrownBy(() -> orderService.placeOrder(request, 1L));
		}

		@Test
		@DisplayName("입력된 가격이 종가 기준 상향 30%를 초과할 경우 예외가 발생한다.")
		void throwExceptionWhenPriceExceedsUpperLimit() {
			// given
			BigDecimal price = new BigDecimal(1301);
			OrderRequest request = createOrderRequest(Type.LIMIT_SELL, BigDecimal.valueOf(10), price);
			when(companyRepository.findByIsuSrtCd(any())).thenReturn(company);

			// when, then
			assertThatThrownBy(() -> orderService.placeOrder(request, 1L)).isInstanceOf(PriceOutOfRangeException.class);
		}

		@Test
		@DisplayName("입력된 가격이 종가 기준 하향 30% 이하일 경우 정상적으로 처리한다.")
		void placeOrderWhenPriceWithinLowerLimit() {
			// given
			BigDecimal price = new BigDecimal(700);
			OrderRequest request = createOrderRequest(Type.LIMIT_BUY, BigDecimal.valueOf(10), price);
			when(companyRepository.findByIsuSrtCd(any())).thenReturn(company);
			when(accountRepository.getByMemberId(any())).thenReturn(account);

			// when, then
			assertThatNoException().isThrownBy(() -> orderService.placeOrder(request, 1L));
		}

		@Test
		@DisplayName("입력된 가격이 종가 기준 하향 30% 미만일 경우 예외가 발생한다.")
		void throwExceptionWhenPriceIsBelowLowerLimit() {
			// given
			BigDecimal price = new BigDecimal(699);
			OrderRequest request = createOrderRequest(Type.LIMIT_BUY, BigDecimal.valueOf(10), price);
			when(companyRepository.findByIsuSrtCd(any())).thenReturn(company);

			// when, then
			assertThatThrownBy(() -> orderService.placeOrder(request, 1L))
					.isInstanceOf(PriceOutOfRangeException.class);
		}

		@Test
		@DisplayName("입력 가격이 음수일 경우 예외가 발생한다.")
		void throwExceptionWhenInvalidPrice() {
			// given
			BigDecimal price = new BigDecimal(-1);
			OrderRequest request = createOrderRequest(Type.LIMIT_BUY, BigDecimal.valueOf(10), price);

			// when, then
			assertThatThrownBy(() -> orderService.placeOrder(request, 1L)).isInstanceOf(OrderPriceQuotationException.class);
		}

		@Test
		@DisplayName("유효하지 않은 단위의 가격이 입력될 경우 예외가 발생한다.")
		void throwExceptionWhenInvalidUnitPrice() {
			// given
			BigDecimal price = new BigDecimal("0.5");
			OrderRequest request = createOrderRequest(Type.LIMIT_BUY, BigDecimal.valueOf(10), price);

			// when, then
			assertThatThrownBy(() -> orderService.placeOrder(request, 1L)).isInstanceOf(OrderPriceQuotationException.class);
		}
	}

	private OrderRequest createOrderRequest(
		Type type,
		BigDecimal totalQuantity,
		BigDecimal price
	) {
		return new OrderRequest(
			"005930",
			type,
			totalQuantity,
			price,
			LocalDateTime.of(2025, 1, 1, 1, 1)
		);
	}
}
