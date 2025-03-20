package com.onseju.orderservice.company.controller;

import com.onseju.orderservice.company.controller.response.CompanySearchResponseDto;
import com.onseju.orderservice.company.service.CompanyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CompanyController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
	"spring.jpa.hibernate.ddl-auto=none",
	"spring.datasource.url=jdbc:h2:mem:testdb"
})
class CompanyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CompanyService companyService;

	@MockitoBean
	private JpaMetamodelMappingContext jpaMetamodelMappingContext;

	@Test
	void contextLoads() {
		assertThat(mockMvc).isNotNull();
		assertThat(companyService).isNotNull();
	}

	@Test
	@DisplayName("회사 검색 정상 케이스 테스트")
	void 회사검색_정상케이스() throws Exception {
		String query = "삼성";
		List<CompanySearchResponseDto> mockCompanies = Arrays.asList(
			new CompanySearchResponseDto("삼성전자", "005930", "KOSPI", "주권", "삼성전자"),
			new CompanySearchResponseDto("삼성물산", "028260", "KOSPI", "주권", "삼성물산")
		);

		when(companyService.searchCompanies(query)).thenReturn(mockCompanies);

		mockMvc.perform(get("/api/companies/search")
				.param("query", query))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].isuNm").value("삼성전자"))
			.andExpect(jsonPath("$[0].isuSrtCd").value("005930"))
			.andExpect(jsonPath("$[1].isuNm").value("삼성물산"))
			.andExpect(jsonPath("$[1].isuSrtCd").value("028260"));

		verify(companyService).searchCompanies(query);
	}

	@Test
	@DisplayName("회사 검색 결과 없음 테스트")
	void 회사검색_결과없음() throws Exception {
		String query = "존재하지않는회사";
		when(companyService.searchCompanies(query)).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/companies/search")
				.param("query", query))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));

		verify(companyService).searchCompanies(query);
	}

	@Test
	@DisplayName("회사 검색 데이터 정합성 검증 테스트")
	void TC20_1_2_데이터정합성검증() throws Exception {
		String query = "삼성전자";
		CompanySearchResponseDto mockCompany = new CompanySearchResponseDto("삼성전자", "005930", "KOSPI", "주권", "삼성전자");
		when(companyService.searchCompanies(query)).thenReturn(Collections.singletonList(mockCompany));

		mockMvc.perform(get("/api/companies/search")
						.param("query", query))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].isuNm").value("삼성전자"))
				.andExpect(jsonPath("$[0].isuSrtCd").value("005930"))
				.andExpect(jsonPath("$[0].isuAbbrv").value("KOSPI"))
				.andExpect(jsonPath("$[0].isuEngNm").value("주권"))
				.andExpect(jsonPath("$[0].kindStkcertTpNm").value("삼성전자"));

		verify(companyService).searchCompanies(query);
	}
}
