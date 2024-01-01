package com.example.shoppingcart.service;

import com.example.shoppingcart.util.EventType;
import com.example.shoppingcart.data.ShoppingCart;
import com.example.shoppingcart.data.dao.Item;
import com.example.shoppingcart.data.dto.CartItemRequest;
import com.example.shoppingcart.data.dto.DiscountRequest;
import com.example.shoppingcart.exception.DiscountValidationException;
import com.example.shoppingcart.exception.ItemQuantityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import static com.example.shoppingcart.util.Constants.DISCOUNTS;
import static com.example.shoppingcart.util.EventType.ADD_ITEM;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCart shoppingCart;
    private final EventService eventService;

    public void addItemToCart(CartItemRequest cartItemRequest) {
        validateQuantity(cartItemRequest.getQuantity());
        shoppingCart.addItem(cartItemRequest);
        eventService.recordCartEvent(ADD_ITEM, cartItemRequest);
    }

    public void removeItemFromCart(String sku) {
        Item removedItem = shoppingCart.removeItem(sku);
        eventService.recordCartEvent(EventType.REMOVE_ITEM, removedItem.convertToCartItemDto());
    }

    public void emptyCart() {
        shoppingCart.emptyCart();
        eventService.recordCartEvent(EventType.EMPTY_CART);
    }

    public void changeItemQuantity(CartItemRequest cartItemRequest) {
        shoppingCart.changeQuantity(cartItemRequest);
        eventService.recordCartEvent(EventType.UPDATE_ITEM, cartItemRequest);
    }

    public void applyDiscount(DiscountRequest discountRequest) {
        validateDiscountPercentage(discountRequest.getDiscountCode());
        int discountPercentage = shoppingCart.applyDiscount(discountRequest);
        eventService.recordDiscountEvent(EventType.APPLY_DISCOUNT, discountPercentage);
    }

    public List<Item> getCartItems() {
        return shoppingCart.getItems().values().stream().toList();
    }

    public BigDecimal getTotalAmount() {
        return shoppingCart.getTotalAmount();
    }

    public void validateQuantity(int quantity) {
        if (quantity < 1 || quantity > 1000) {
            throw new ItemQuantityException("Quantity must be between 1 and 1000.");
        }
    }

    public void validateDiscountPercentage(String discountCode) {
        if(!DISCOUNTS.containsKey(discountCode)) {
            throw new DiscountValidationException("Discount cant be applied.");
        }

        int discountPercentage = DISCOUNTS.get(discountCode);

        if (discountPercentage < 0 || discountPercentage >= 100) {
            throw new DiscountValidationException("Discount percentage must be between 0 and 100.");
        }
    }
}
