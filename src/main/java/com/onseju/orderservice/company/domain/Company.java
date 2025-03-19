package com.onseju.orderservice.company.domain;

import com.onseju.orderservice.order.OrderConstant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20, nullable = false, unique = true)
	private String isuCd; // 표준코드 (ISU_CD)

	@Column(length = 10, nullable = false)
	private String isuSrtCd; // 단축코드 (ISU_SRT_CD)

	@Column(length = 100, nullable = false)
	private String isuNm; // 한글 종목명 (ISU_NM)

	@Column(length = 50, nullable = false)
	private String isuAbbrv; // 한글 종목약명 (ISU_ABBRV)

	@Column(length = 100, nullable = false)
	private String isuEngNm; // 영문 종목명 (ISU_ENG_NM)

	@Column(nullable = false)
	private String listDd; // 상장일 (LIST_DD)

	@Column(length = 20, nullable = false)
	private String mktTpNm; // 시장구분 (MKT_TP_NM)

	@Column(length = 50, nullable = false)
	private String secugrpNm; // 증권구분 (SECUGRP_NM)

	@Column(length = 50)
	private String sectTpNm; // 소속부 (SECT_TP_NM) (nullable)

	@Column(length = 50, nullable = false)
	private String kindStkcertTpNm; // 주식종류 (KIND_STKCERT_TP_NM)

	@Column(nullable = false)
	private String parval; // 액면가 (PARVAL)

	@Column(nullable = false)
	private String listShrs; // 상장주식수 (LIST_SHRS)

	private BigDecimal closingPrice; // 전일 종가

	public boolean isWithinClosingPriceRange(final BigDecimal price) {
		final BigDecimal percentageDivisor = new BigDecimal(100);
		final BigDecimal priceLimit = BigDecimal.valueOf(OrderConstant.CLOSING_PRICE_LIMIT.getValue());

		final BigDecimal lowerBound = calculatePriceLimit(percentageDivisor, priceLimit.negate());
		final BigDecimal upperBound = calculatePriceLimit(percentageDivisor, priceLimit);

		return price.compareTo(lowerBound) >= 0 && price.compareTo(upperBound) <= 0;
	}

	private BigDecimal calculatePriceLimit(BigDecimal percentageDivisor, BigDecimal priceLimit) {
		return closingPrice.multiply(new BigDecimal(100).add(priceLimit))
			.divide(percentageDivisor, RoundingMode.HALF_UP);
	}
}
