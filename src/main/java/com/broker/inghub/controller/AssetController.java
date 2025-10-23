package com.broker.inghub.controller;

import com.broker.inghub.dto.AssetDetailResponse;
import com.broker.inghub.dto.AssetResponse;
import com.broker.inghub.service.AssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Slf4j
public class AssetController {
    private final AssetService assetService;

    @GetMapping
    public ResponseEntity<AssetResponse> listAssetsByCustomerId(@RequestParam Long customerId) {
        return ResponseEntity.ok(assetService.listAssetsByCustomerId(customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetDetailResponse> getAsset(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

}
