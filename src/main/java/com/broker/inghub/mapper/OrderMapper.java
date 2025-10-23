package com.broker.inghub.mapper;

import com.broker.inghub.dto.OrderResponse;
import com.broker.inghub.model.OrderEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class OrderMapper {

    public static OrderResponse toDto(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        return OrderResponse.builder()
                .id(entity.getId())
                .customerId(entity.getCustomer() != null ? String.valueOf(entity.getCustomer().getId()) : null)
                .assetName(entity.getAssetName())
                .side(entity.getOrderSide() != null ? entity.getOrderSide().name() : null)
                .size(entity.getSize())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .createDate(entity.getCreateDate())
                .build();
    }

    public static List<OrderResponse> toDtoList(List<OrderEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(OrderMapper::toDto)
                .toList();
    }
}

