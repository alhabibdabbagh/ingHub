package com.broker.inghub.mapper;

import com.broker.inghub.dto.AssetRequest;
import com.broker.inghub.dto.AssetResponse;
import com.broker.inghub.model.Asset;
import com.broker.inghub.model.Customer;

import java.util.List;

public class AssetMapper {

    private AssetMapper() {
        // Utility class, no instantiation allowed
    }

    public static Asset toEntity(AssetRequest request) {
        Customer customer = new Customer();
        customer.setId(request.getCustomerId());

        return Asset.builder()
                .customer(customer)
                .assetName(request.getAssetName())
                .size(request.getSize())
                .usableSize(request.getUsableSize())
                .build();
    }

    public static AssetResponse toDto(Asset asset) {
        return AssetResponse.builder()
                .assetId(asset.getId())
                .customerId(asset.getCustomer() != null ? asset.getCustomer().getId() : null)
                .assetName(asset.getAssetName())
                .size(asset.getSize())
                .build();
    }

    public static List<AssetResponse> toDtoList(List<Asset> assets) {
        return assets.stream()
                .map(AssetMapper::toDto)
                .toList();
    }
}

