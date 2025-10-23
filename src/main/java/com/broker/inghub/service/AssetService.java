package com.broker.inghub.service;

import com.broker.inghub.dto.AssetDetailResponse;
import com.broker.inghub.dto.AssetResponse;
import com.broker.inghub.model.Asset;
import com.broker.inghub.model.Customer;
import com.broker.inghub.repository.AssetRepository;
import com.broker.inghub.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetService {
    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;

    public AssetResponse listAssetsByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Asset firstAsset = customer.getAssets().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Customer has no assets"));

        return AssetResponse.builder()
                .customerId(customerId)
                .assetName(firstAsset.getAssetName())
                .size(BigDecimal.valueOf(customer.getAssets().size()))
                .build();

    }

    public AssetDetailResponse getAssetById(Long id) {
        Asset assetOptional = assetRepository.findById(id).orElseThrow();
        return AssetDetailResponse.builder()
                .assetId(assetOptional.getId())
                .assetName(assetOptional.getAssetName())
                .size(assetOptional.getSize())
                .usableSize(assetOptional.getUsableSize())
                .build();
    }

}
