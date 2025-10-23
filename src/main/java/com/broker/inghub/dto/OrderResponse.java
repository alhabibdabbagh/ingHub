package com.broker.inghub.dto;

import com.broker.inghub.model.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private String customerId;
    private String assetName;
    private String side;
    private BigDecimal size;
    private BigDecimal price;
    private Status status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy:MM:dd HH:mm:ss", timezone = "UTC")
    private Instant createDate;
}
