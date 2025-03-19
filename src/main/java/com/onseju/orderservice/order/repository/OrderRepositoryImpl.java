package com.onseju.orderservice.order.repository;


import com.onseju.orderservice.order.domain.Order;
import com.onseju.orderservice.order.service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;

	@Override
	public Order save(final Order order) {
		return orderJpaRepository.save(order);
	}

	@Override
	public Optional<Order> findById(final Long id) {
		return orderJpaRepository.findById(id);
	}

	@Override
	public List<Order> findByCompanyCode(final String number) {
		return null;
	}

	@Override
	public List<Order> findAll() {
		return orderJpaRepository.findAll();
	}
}
