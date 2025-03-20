package com.onseju.orderservice.company.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CompanyTest {

	private final Company company = Company.builder()
			.isuCd("005930")
			.isuSrtCd("005930")
			.isuNm("삼성전자")
			.closingPrice(new BigDecimal(1000))
			.build();

	@Nested
	@DisplayName("엔티티 생성 및 조회 테스트")
	class createAndGetCompany {

		@Test
		@DisplayName("회사 엔티티 생성 및 조회 테스트")
		void testCompanyCreationAndRetrieval() {
			assertThat(company).isNotNull();
			assertThat(company.getIsuSrtCd()).isEqualTo("005930");
			assertThat(company.getIsuNm()).isEqualTo("삼성전자");
		}
	}


	@Nested
	@DisplayName("종가 금액 관련 테스트")
	class testClosingPrice {

		@Test
		@DisplayName("입력된 가격이 종가 기준 상향 30% 이상일 경우 true를 반환한다.")
		void returnTrueWhenPriceWithinUpperLimit() {
			// given
			BigDecimal price = new BigDecimal(1300);

			// when
			boolean isWithinRange = company.isWithinClosingPriceRange(price);

			// then
			assertThat(isWithinRange).isTrue();
		}

		@Test
		@DisplayName("입력된 가격이 종가 기준 상향 30%를 초과할 경우 false를 반환한다.")
		void returnFalseWhenPriceExceedsUpperLimit() {
			// given
			BigDecimal price = new BigDecimal(1301);

			// when
			boolean isWithinRange = company.isWithinClosingPriceRange(price);

			// then
			assertThat(isWithinRange).isFalse();
		}

		@Test
		@DisplayName("입력된 가격이 종가 기준 하향 30% 이하일 경우 true를 반환한다.")
		void returnTrueWhenPriceWithinLowerLimit() {
			// given
			BigDecimal price = new BigDecimal(700);

			// when
			boolean isWithinRange = company.isWithinClosingPriceRange(price);

			// then
			assertThat(isWithinRange).isTrue();
		}

		@Test
		@DisplayName("입력된 가격이 종가 기준 하향 30% 미만일 경우 false를 반환한다.")
		void returnFalseWhenPriceIsBelowLowerLimit() {
			// given
			BigDecimal price = new BigDecimal(699);

			// when
			boolean isWithinRange = company.isWithinClosingPriceRange(price);

			// then
			assertThat(isWithinRange).isFalse();
		}
	}
}
