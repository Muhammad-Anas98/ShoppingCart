package com.example.shoppingcart.data;

import com.example.shoppingcart.data.dao.Item;
import com.example.shoppingcart.data.dto.CartItemRequest;
import com.example.shoppingcart.data.dto.DiscountRequest;
import com.example.shoppingcart.exception.ItemNotFoundException;
import com.example.shoppingcart.exception.ItemQuantityException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.shoppingcart.util.Constants.DISCOUNTS;

@Component
@Log4j2
public class ShoppingCart {

    private final Map<String, Item> shoppingCart = new ConcurrentHashMap<>();
    private Integer discountPercentage = null;
    public void addItem(CartItemRequest cartItemRequest) {
        String sku = cartItemRequest.getSku();
        shoppingCart.computeIfAbsent(sku, k -> createCartItem(cartItemRequest));
    }

    public Item removeItem(String sku) {
        if(!shoppingCart.containsKey(sku)){
            throw new ItemNotFoundException("Item not found in cart.");
        }
        return shoppingCart.remove(sku);
    }

    public void emptyCart() {
        shoppingCart.clear();
    }

    public void changeQuantity(CartItemRequest cartItemRequest) {
        String sku = cartItemRequest.getSku();

        if(!shoppingCart.containsKey(sku)){
            throw new ItemNotFoundException("Item not found in cart.");
        }

        shoppingCart.computeIfPresent(sku, (k, existingItem) -> {
            int computedQuantity = existingItem.getQuantity() + cartItemRequest.getQuantity();
            if(computedQuantity > 1000){
                throw new ItemQuantityException("Overall Quantity must be less than 1000.");
            }

            if(computedQuantity <= 0){
                //we can send event here remove as the quantity goes below 0 and we are removing it
                return null;
            }

            existingItem.setQuantity(existingItem.getQuantity() + cartItemRequest.getQuantity());
            return existingItem;
        });
    }

    public int applyDiscount(DiscountRequest discountRequest) {
        if (discountPercentage != null) {
            log.warn("Discount already applied.");
        }
        discountPercentage = DISCOUNTS.get(discountRequest.getDiscountCode());
        return discountPercentage;
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
