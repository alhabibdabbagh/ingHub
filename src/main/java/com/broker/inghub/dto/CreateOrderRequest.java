package com.broker.inghub.dto;

import com.broker.inghub.model.Side;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotBlank(message = "Asset name is required")
    private String assetName;

    @NotNull(message = "Order side is required")
    private Side side;

    @NotNull(message = "Size is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Size must be positive")
    private BigDecimal size;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;
}
