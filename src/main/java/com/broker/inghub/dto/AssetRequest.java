package com.broker.inghub.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Asset name is required")
    private String assetName;

    @NotNull(message = "Size is required")
    @DecimalMin(value = "0.0", message = "Size cannot be negative")
    @Digits(integer = 10, fraction = 4, message = "Size must be a valid decimal")
    private BigDecimal size;

    @NotNull(message = "Usable size is required")
    @DecimalMin(value = "0.0", message = "Usable size cannot be negative")
    @Digits(integer = 10, fraction = 4, message = "Usable size must be a valid decimal")
    private BigDecimal usableSize;
}

