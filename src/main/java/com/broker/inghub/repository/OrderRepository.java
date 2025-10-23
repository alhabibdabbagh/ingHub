package com.broker.inghub.repository;

import com.broker.inghub.model.Customer;
import com.broker.inghub.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByCustomerAndCreateDateBetween(Customer customer, Instant from, Instant to);
    List<OrderEntity> findByCustomer(Customer customer);
}

