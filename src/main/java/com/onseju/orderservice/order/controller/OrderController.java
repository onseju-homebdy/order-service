package com.onseju.orderservice.order.controller;

import com.onseju.orderservice.order.controller.request.OrderRequest;
import com.onseju.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<Void> received(
			@RequestBody final OrderRequest request
	) {
		orderService.placeOrder(request, 1L);
		return ResponseEntity.ok().build();
	}
}
