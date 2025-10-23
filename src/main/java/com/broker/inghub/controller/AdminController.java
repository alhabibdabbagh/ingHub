package com.broker.inghub.controller;

import com.broker.inghub.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final OrderService orderService;

    @PostMapping("/order/match/{id}")
    public ResponseEntity<Void> matchOrder(@PathVariable Long id) {
        orderService.matchOrderByAdmin(id);
        return ResponseEntity.ok().build();
    }
}

