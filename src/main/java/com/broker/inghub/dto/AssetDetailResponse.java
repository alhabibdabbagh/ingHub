package com.broker.inghub.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDetailResponse {
    private Long assetId;
    private String assetName;
    private BigDecimal size;
    private BigDecimal usableSize;
}

