package com.broker.inghub.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetResponse {
    private Long assetId;
    private Long customerId;
    private String assetName;
    private BigDecimal size;
}

