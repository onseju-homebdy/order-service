package com.onseju.orderservice.order.service;

import com.onseju.orderservice.company.domain.Company;
import com.onseju.orderservice.company.service.CompanyRepository;
import com.onseju.orderservice.holding.domain.Holdings;
import com.onseju.orderservice.holding.service.HoldingsRepository;
import com.onseju.orderservice.order.controller.request.OrderRequest;
import com.onseju.orderservice.order.domain.Account;
import com.onseju.orderservice.order.exception.PriceOutOfRangeException;
import com.onseju.orderservice.order.mapper.OrderMapper;
import com.onseju.orderservice.order.service.repository.AccountRepository;
import com.onseju.orderservice.order.service.repository.OrderRepository;
import com.onseju.orderservice.order.service.validator.OrderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final CompanyRepository companyRepository;
	private final HoldingsRepository holdingsRepository;
	private final AccountRepository accountRepository;
	private final OrderMapper orderMapper;

	@Transactional
	public void placeOrder(final OrderRequest request, Long memberId) {
		// 지정가 주문 가격 견적 유효성 검증
		final BigDecimal price = request.price();
		final OrderValidator validator = OrderValidator.getUnitByPrice(price);
		validator.isValidPrice(price);

		// 종가 기준 검증
		validateClosingPrice(price, request.companyCode());

		Account account = accountRepository.getByMemberId(memberId);
		reserveForSellOrder(request);
		orderRepository.save(orderMapper.toEntity(request, account.getId()));
	}

	// 종가 기준 가격 검증
	private void validateClosingPrice(final BigDecimal price, final String companyCode) {
		final Company company = companyRepository.findByIsuSrtCd(companyCode);

		if (!company.isWithinClosingPriceRange(price)) {
			throw new PriceOutOfRangeException();
		}
	}

	private void reserveForSellOrder(final OrderRequest request) {
		if (request.type().isSell()) {
			final Holdings holdings = holdingsRepository.getByAccountIdAndCompanyCode(1L, request.companyCode());
			validateHoldings(holdings, request.totalQuantity());
			holdings.processReservedOrder(request.totalQuantity());
		}
	}

	private void validateHoldings(final Holdings holdings, final BigDecimal totalQuantity) {
		holdings.validateExistHoldings();
		holdings.validateEnoughHoldings(totalQuantity);
	}
}
