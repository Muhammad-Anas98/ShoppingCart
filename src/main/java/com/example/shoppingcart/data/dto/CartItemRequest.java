package com.example.shoppingcart.data.dto;

import lombok.*;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {
    @NonNull
    private String sku;
    @NonNull
    private int quantity;
    private String displayName;
    @NonNull
    private BigDecimal price;
}

