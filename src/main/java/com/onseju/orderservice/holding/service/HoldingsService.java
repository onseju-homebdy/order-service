package com.onseju.orderservice.holding.service;

import com.onseju.orderservice.holding.domain.Holdings;
import com.onseju.orderservice.order.domain.Type;
import com.onseju.orderservice.holding.repository.HoldingsRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class HoldingsService {

	private final HoldingsRepositoryImpl holdingsRepository;

	public void updateHoldingsAfterTrade(final Type type, final Long accountId, final String companyCode,
										 final BigDecimal price, final BigDecimal quantity) {
		final Holdings holdings = getOrCreateHoldings(accountId, companyCode);
		holdings.updateHoldings(type, price, quantity);
		holdingsRepository.save(holdings);
	}

	public Holdings getOrCreateHoldings(final Long accountId, final String companyCode) {
		return holdingsRepository.findByAccountIdAndCompanyCode(accountId, companyCode)
				.orElse(
						Holdings.builder()
						.accountId(accountId)
						.companyCode(companyCode)
						.quantity(BigDecimal.ZERO)
						.reservedQuantity(BigDecimal.ZERO)
						.averagePrice(BigDecimal.ZERO)
						.totalPurchasePrice(BigDecimal.ZERO)
						.build()
				);
	}
}
