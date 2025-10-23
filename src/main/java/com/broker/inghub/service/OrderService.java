package com.broker.inghub.service;

import com.broker.inghub.dto.CreateOrderRequest;
import com.broker.inghub.dto.OrderResponse;
import com.broker.inghub.mapper.OrderMapper;
import com.broker.inghub.model.*;
import com.broker.inghub.repository.AssetRepository;
import com.broker.inghub.repository.CustomerRepository;
import com.broker.inghub.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest req) {
        String customerId = req.getCustomerId();
        String assetName = req.getAssetName();
        Side side = req.getSide();
        BigDecimal size = req.getSize();
        BigDecimal price = req.getPrice();

        // Find customer
        Customer customer = customerRepository.findById(Long.parseLong(customerId))
                .orElseThrow(() -> new IllegalStateException("Customer not found with id: " + customerId));

        // Always use TRY asset for cash balance operations
        Asset tryAsset = assetRepository.findByCustomerAndAssetName(customer, "TRY")
                .orElseThrow(() -> new IllegalStateException("Customer does not have TRY asset"));

        BigDecimal totalCost = price.multiply(size);

        if (side == Side.BUY) {
            // Check if customer has enough TRY
            if (tryAsset.getUsableSize().compareTo(totalCost) < 0) {
                throw new IllegalStateException("Insufficient TRY balance for buy order");
            }
            // Deduct TRY from usable balance
            tryAsset.setUsableSize(tryAsset.getUsableSize().subtract(totalCost));
            assetRepository.save(tryAsset);
        } else { // SELL
            // For sell, customer receives TRY
            // First check if they have the asset to sell (optional validation)
            // Then add TRY to their balance
            tryAsset.setUsableSize(tryAsset.getUsableSize().add(totalCost));
            assetRepository.save(tryAsset);
        }

        // Create order
        OrderEntity order = OrderEntity.builder()
                .customer(customer)
                .assetName(assetName)
                .orderSide(side)
                .size(size)
                .price(price)
                .status(Status.PENDING)
                .createDate(Instant.now())
                .build();

        OrderEntity saved = orderRepository.save(order);
        return OrderMapper.toDto(saved);
    }

    public List<OrderEntity> listOrders(String customerId, Instant from, Instant to) {
        if (customerId != null) {
            Customer customer = customerRepository.findById(Long.parseLong(customerId))
                    .orElseThrow(() -> new IllegalStateException("Customer not found with id: " + customerId));

            if (from != null && to != null) {
                return orderRepository.findByCustomerAndCreateDateBetween(customer, from, to);
            }
            return orderRepository.findByCustomer(customer);
        }
        return orderRepository.findAll();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (order.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only pending orders can be canceled");
        }

        // Get customer's TRY asset
        Customer customer = order.getCustomer();
        Asset tryAsset = assetRepository.findByCustomerAndAssetName(customer, "TRY")
                .orElseThrow(() -> new IllegalStateException("Customer TRY asset not found"));

        BigDecimal totalCost = order.getPrice().multiply(order.getSize());

        // Revert TRY balance
        if (order.getOrderSide() == Side.BUY) {
            // Return TRY to customer
            tryAsset.setUsableSize(tryAsset.getUsableSize().add(totalCost));
        } else { // SELL
            // Remove TRY from customer (they didn't sell, so remove the added TRY)
            tryAsset.setUsableSize(tryAsset.getUsableSize().subtract(totalCost));
        }

        assetRepository.save(tryAsset);
        order.setStatus(Status.CANCELED);
        orderRepository.save(order);
    }
}
