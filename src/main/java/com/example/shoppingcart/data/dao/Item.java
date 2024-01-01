package com.example.shoppingcart.data.dao;

import com.example.shoppingcart.data.dto.CartItemRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Item {

    @Id
    private String sku;
    private int quantity;
    private String displayName;
    private BigDecimal price;

    public Item(String sku, int quantity, String displayName, BigDecimal price) {
        this.sku = sku;
        this.quantity = quantity;
        this.displayName = displayName;
        this.price = price;
    }

    public CartItemRequest convertToCartItemDto() {
        return new CartItemRequest(this.sku, this.quantity, this.displayName, this.price);
    }
}