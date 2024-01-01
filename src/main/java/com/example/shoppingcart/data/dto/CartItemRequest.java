package com.example.shoppingcart.data.dto;

import com.example.shoppingcart.data.dao.Item;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartItemRequest {
    @NotBlank
    private String sku;
    @Positive
    @NotNull
    private int quantity;
    private String displayName;
    @NotNull
    private BigDecimal price;

    public Item convertToItemDao() {
        return new Item(this.sku, this.quantity, this.displayName, this.price);
    }
}

