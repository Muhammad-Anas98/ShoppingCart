package com.example.shoppingcart.data;

import com.example.shoppingcart.data.dao.Item;
import com.example.shoppingcart.data.dto.CartItemRequest;
import com.example.shoppingcart.data.dto.DiscountRequest;
import com.example.shoppingcart.exception.DiscountValidationException;
import com.example.shoppingcart.exception.ItemQuantityExceedsException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.shoppingcart.constant.Constants.DISCOUNTS;

@Component
public class ShoppingCart {

    private final Map<String, Item> shoppingCart = new ConcurrentHashMap<>();
    private Integer discountPercentage = null;
    public void addItem(CartItemRequest cartItemRequest) {
        String sku = cartItemRequest.getSku();
        shoppingCart.computeIfAbsent(sku, k -> createCartItem(cartItemRequest));
    }

    public Item removeItem(String sku) {
        return shoppingCart.remove(sku);
    }

    public void emptyCart() {
        shoppingCart.clear();
    }

    public void changeQuantity(CartItemRequest cartItemRequest) {
        String sku = cartItemRequest.getSku();
        shoppingCart.computeIfPresent(sku, (k, existingItem) -> {
            if((existingItem.getQuantity() + cartItemRequest.getQuantity()) > 1000){
                throw new ItemQuantityExceedsException("Quantity must be between 1 and 1000.");
            }

            existingItem.setQuantity(existingItem.getQuantity() + cartItemRequest.getQuantity());
            return existingItem;
        });
    }

    public void applyDiscount(DiscountRequest discountRequest) {
        if(!DISCOUNTS.containsKey(discountRequest.getDiscountCode()) || discountPercentage != null) {
            throw new DiscountValidationException("Discount cant be applied.");
        }
        discountPercentage = discountRequest.getDiscountPercentage();
    }

    public Map<String, Item> getItems() {
        return Collections.unmodifiableMap(shoppingCart);
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = shoppingCart.values()
                .stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (discountPercentage != null) {
            BigDecimal discountAmount = total.multiply(BigDecimal.valueOf(discountPercentage / 100.0));
            total = total.subtract(discountAmount);
        }

        return total;
    }

    private Item createCartItem(CartItemRequest cartItemRequest) {
    return new Item(
            cartItemRequest.getSku(),
            cartItemRequest.getQuantity(),
            cartItemRequest.getDisplayName(),
            cartItemRequest.getPrice());
    }

}
