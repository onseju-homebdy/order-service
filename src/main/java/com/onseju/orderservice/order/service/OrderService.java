package com.onseju.orderservice.order.service;

import com.onseju.orderservice.company.domain.Company;
import com.onseju.orderservice.company.service.CompanyRepository;
import com.onseju.orderservice.order.controller.request.OrderRequest;
import com.onseju.orderservice.holding.domain.Holdings;
import com.onseju.orderservice.order.domain.Order;
import com.onseju.orderservice.order.domain.Type;
import com.onseju.orderservice.holding.exception.HoldingsNotFoundException;
import com.onseju.orderservice.order.exception.PriceOutOfRangeException;
import com.onseju.orderservice.holding.service.HoldingsRepository;
import com.onseju.orderservice.order.service.repository.OrderRepository;
import com.onseju.orderservice.order.service.validator.OrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final CompanyRepository companyRepository;
	private final HoldingsRepository holdingsRepository;

	public void placeOrder(final OrderRequest request) {
		// 지정가 주문 가격 견적 유효성 검증
		final BigDecimal price = request.price();
		final OrderValidator validator = OrderValidator.getUnitByPrice(price);
		validator.isValidPrice(price);

		// 종가 기준 검증
		validateClosingPrice(price, request.companyCode());

		final Order order = createOrder(request);
		orderRepository.save(order);
	}

	// 종가 기준 가격 검증
	private void validateClosingPrice(final BigDecimal price, final String companyCode) {
		final Company company = companyRepository.findByIsuSrtCd(companyCode);

		if (!company.isWithinClosingPriceRange(price)) {
			throw new PriceOutOfRangeException();
		}
	}

	private Order createOrder(final OrderRequest request) {
		// 매도 시 보유 주식 확인 및 보유 주식 수량 검증 후 예약 매도 수량 설정
		if (request.type() == Type.SELL) {
			final Holdings holdings = holdingsRepository.findByAccountIdAndCompanyCode(1L, request.companyCode())
					.orElseThrow(HoldingsNotFoundException::new);
			holdings.validateExistHoldings();
			holdings.validateEnoughHoldings(request.totalQuantity());
			holdings.processReservedOrder(request.totalQuantity());
			holdingsRepository.save(holdings);
		}
		return Order.builder().build();
	}
}
