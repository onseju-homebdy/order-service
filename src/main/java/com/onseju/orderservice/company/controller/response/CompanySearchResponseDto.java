package com.onseju.orderservice.company.controller.response;

import com.onseju.orderservice.company.domain.Company;

/**
 * 회사 검색 결과를 담는 DTO
 */
public record CompanySearchResponseDto(
		String isuNm,
		String isuSrtCd,
		String isuAbbrv,
		String isEngNm,
		String kindstkcertTpNm
) {

	public static CompanySearchResponseDto fromEntity(Company company) {
		return new CompanySearchResponseDto("a", "b", "c", "d", "e");
	}
}
