package com.onseju.orderservice.holding.service;

import com.onseju.orderservice.holding.domain.Holdings;

import java.util.Optional;

public interface HoldingsRepository {

	Optional<Holdings> findByAccountIdAndCompanyCode(final Long accountId, final String companyCode);

	Holdings save(final Holdings holdings);
}
