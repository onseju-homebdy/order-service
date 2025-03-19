package com.onseju.orderservice.tradehistory.service;

import com.onseju.orderservice.tradehistory.domain.TradeHistory;
import com.onseju.orderservice.tradehistory.dto.TradeHistoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 거래 내역 관리 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TradeHistoryService {
	private final TradeHistoryRepository tradeHistoryRepository;

	/**
	 * 거래 내역 저장 (일반 사용자)
	 */
	public void saveTradeHistory(final TradeHistoryResponse tradeHistoryResponse) {
		TradeHistory tradeHistory = TradeHistory.builder().build();

		// DB 저장
		tradeHistoryRepository.save(tradeHistory);
		log.info("거래 내역 저장: {}", tradeHistory);
	}
}
