package com.onseju.orderservice.order.controller;

import com.onseju.orderservice.order.controller.request.OrderRequest;
import com.onseju.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<Void> received(
			@RequestBody final OrderRequest request
	) {
		orderService.placeOrder(request);
		return ResponseEntity.ok().build();
	}
}
