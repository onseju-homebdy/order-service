package com.onseju.orderservice.order.domain;

import com.onseju.orderservice.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Long id;

	@Column(nullable = false, insertable = false, updatable = false)
	private BigDecimal balance;

	@Column(nullable = false, insertable = false, updatable = false)
	private BigDecimal reservedBalance;

	@JoinColumn(nullable = false)
	private Long memberId;
}
