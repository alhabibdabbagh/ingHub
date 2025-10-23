package com.broker.inghub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "assets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotBlank(message = "Asset name is required")
    @Column(nullable = false)
    private String assetName;

    @NotNull(message = "Size is required")
    @DecimalMin(value = "0.0", message = "Size cannot be negative")
    @Digits(integer = 10, fraction = 4, message = "Size must be a valid decimal")
    @Column(nullable = false)
    private BigDecimal size;

    @NotNull(message = "Usable size is required")
    @DecimalMin(value = "0.0", message = "Usable size cannot be negative")
    @Digits(integer = 10, fraction = 4, message = "Usable size must be a valid decimal")
    @Column(nullable = false)
    private BigDecimal usableSize;
}

