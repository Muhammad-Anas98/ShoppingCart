package com.example.shoppingcart.service;

import com.example.shoppingcart.constant.EventType;
import com.example.shoppingcart.data.ShoppingCart;
import com.example.shoppingcart.data.dao.Item;
import com.example.shoppingcart.data.dto.CartItemRequest;
import com.example.shoppingcart.data.dto.DiscountRequest;
import com.example.shoppingcart.exception.DiscountValidationException;
import com.example.shoppingcart.exception.ItemQuantityExceedsException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import static com.example.shoppingcart.constant.EventType.ADD_ITEM;

@Service
public class ShoppingCartService {

    private final ShoppingCart shoppingCart;
    private final EventService eventService;

    public ShoppingCartService(ShoppingCart shoppingCart, EventService eventService) {
        this.shoppingCart = shoppingCart;
        this.eventService = eventService;
    }

    public void addItemToCart(CartItemRequest cartItemRequest) {
        validateQuantity(cartItemRequest.getQuantity());
        shoppingCart.addItem(cartItemRequest);
        eventService.recordCartAddEvent(ADD_ITEM, cartItemRequest);
    }

    public void removeItemFromCart(String sku) {
        Item removedItem = shoppingCart.removeItem(sku);
        eventService.recordCartAddEvent(EventType.REMOVE_ITEM, removedItem.convertToCartItemDto());
    }

    public void emptyCart() {
        shoppingCart.emptyCart();
        eventService.recordCartAddEvent(EventType.EMPTY_CART, null);
    }

    public void changeItemQuantity(CartItemRequest cartItemRequest) {
        validateQuantity(cartItemRequest.getQuantity());
        shoppingCart.changeQuantity(cartItemRequest);
        eventService.recordCartAddEvent(EventType.UPDATE_ITEM, cartItemRequest);
    }

    public void applyDiscount(DiscountRequest discountRequest) {
        validateDiscountPercentage(discountRequest.getDiscountPercentage());
        shoppingCart.applyDiscount(discountRequest);
        eventService.recordDiscountEvent(EventType.APPLY_DISCOUNT, discountRequest.getDiscountPercentage());
    }

    public Map<String, Item> getCartItems() {
        return shoppingCart.getItems();
    }

    public BigDecimal getTotalAmount() {
        return shoppingCart.getTotalAmount();
    }

    public void validateQuantity(int quantity) {
        if (quantity < 1 || quantity > 1000) {
      throw new ItemQuantityExceedsException("Quantity must be between 1 and 1000.");
        }
    }

    public void validateDiscountPercentage(int discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
      throw new DiscountValidationException("Discount percentage must be between 0 and 100.");
        }
    }
}
