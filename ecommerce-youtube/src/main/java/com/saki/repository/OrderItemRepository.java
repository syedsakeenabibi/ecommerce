package com.saki.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saki.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long>{

}
