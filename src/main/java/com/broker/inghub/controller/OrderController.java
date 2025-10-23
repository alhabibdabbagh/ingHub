package com.broker.inghub.controller;

import com.broker.inghub.dto.CreateOrderRequest;
import com.broker.inghub.dto.OrderResponse;
import com.broker.inghub.mapper.OrderMapper;
import com.broker.inghub.model.OrderEntity;
import com.broker.inghub.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest req) {
        OrderResponse created = orderService.createOrder(req);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> listOrders(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Instant fromDate = null;
        Instant toDate = null;

        // Validate date range if both provided
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            log.warn("Invalid date range: startDate {} is after endDate {}", startDate, endDate);
            return ResponseEntity.badRequest().build();
        }

        if (startDate != null) {
            // Inclusive start of day in UTC
            LocalDateTime startOfDay = startDate.atStartOfDay();
            fromDate = startOfDay.atZone(ZoneOffset.UTC).toInstant();
        }
        if (endDate != null) {
            // Inclusive end of day in UTC (set to last nanosecond of the day)
            LocalDateTime endOfNextDayStart = endDate.plusDays(1).atStartOfDay();
            LocalDateTime endOfDay = endOfNextDayStart.minusNanos(1);
            toDate = endOfDay.atZone(ZoneOffset.UTC).toInstant();
        }

        List<OrderEntity> orders = orderService.listOrders(customerId, fromDate, toDate);
        return ResponseEntity.ok(OrderMapper.toDtoList(orders));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
