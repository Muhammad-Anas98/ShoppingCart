package com.example.shoppingcart;

import com.example.shoppingcart.constant.EventType;
import com.example.shoppingcart.data.ShoppingCart;
import com.example.shoppingcart.data.dao.Item;
import com.example.shoppingcart.data.dto.CartItemRequest;
import com.example.shoppingcart.data.dto.DiscountRequest;

import com.example.shoppingcart.exception.DiscountValidationException;
import com.example.shoppingcart.exception.ItemQuantityExceedsException;
import com.example.shoppingcart.service.EventService;
import com.example.shoppingcart.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ShoppingCartServiceTest {

    @Mock
    private ShoppingCart shoppingCart;

    @Mock
    private EventService eventService;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddItemToCart() {
        CartItemRequest cartItemRequest = getCartItemRequest();

        when(shoppingCart.getItems()).thenReturn(Collections.singletonMap("SKU123", new Item("SKU123", 2, "shoes", BigDecimal.valueOf(100))));
        doNothing().when(eventService).recordCartAddEvent(EventType.ADD_ITEM, cartItemRequest);

        shoppingCartService.addItemToCart(cartItemRequest);

        verify(shoppingCart, times(1)).addItem(cartItemRequest);
        verify(eventService, times(1)).recordCartAddEvent(EventType.ADD_ITEM, cartItemRequest);
    }

    @Test
    void testRemoveItemFromCart() {
        String sku = "SKU123";
        Item removedItem = new Item("SKU123", 2, "shoes", BigDecimal.valueOf(100));

        when(shoppingCart.getItems()).thenReturn(Collections.singletonMap("SKU123", new Item("SKU123", 2, "shoes", BigDecimal.valueOf(100))));

        when(shoppingCart.removeItem(sku)).thenReturn(removedItem);
        doNothing().when(eventService).recordCartAddEvent(EventType.REMOVE_ITEM, removedItem.convertToCartItemDto());

        shoppingCartService.removeItemFromCart(sku);

        verify(shoppingCart, times(1)).removeItem(sku);
        verify(eventService, times(1)).recordCartAddEvent(EventType.REMOVE_ITEM, removedItem.convertToCartItemDto());
    }

    @Test
    void testEmptyCart() {
        doNothing().when(shoppingCart).emptyCart();
        doNothing().when(eventService).recordCartAddEvent(EventType.EMPTY_CART, null);

        shoppingCartService.emptyCart();

        verify(shoppingCart, times(1)).emptyCart();
        verify(eventService, times(1)).recordCartAddEvent(EventType.EMPTY_CART, null);
    }

    @Test
    void testChangeItemQuantity() {
        CartItemRequest cartItemRequest = getCartItemRequest();

        when(shoppingCart.getItems()).thenReturn(Collections.singletonMap("SKU123", new Item("SKU123", 2, "shoes", BigDecimal.valueOf(100))));
        when(shoppingCart.getTotalAmount()).thenReturn(BigDecimal.valueOf(200));
        doNothing().when(shoppingCart).changeQuantity(cartItemRequest);
        doNothing().when(eventService).recordCartAddEvent(EventType.UPDATE_ITEM, cartItemRequest);

        shoppingCartService.changeItemQuantity(cartItemRequest);

        verify(shoppingCart, times(1)).changeQuantity(cartItemRequest);
        verify(eventService, times(1)).recordCartAddEvent(EventType.UPDATE_ITEM, cartItemRequest);
    }


    @Test
    void testApplyDiscount() {
        DiscountRequest discountRequest = new DiscountRequest("DISC10", 10);

        when(shoppingCart.getTotalAmount()).thenReturn(BigDecimal.valueOf(180));
        doNothing().when(shoppingCart).applyDiscount(discountRequest);
        doNothing().when(eventService).recordDiscountEvent(EventType.APPLY_DISCOUNT, 10);

        shoppingCartService.applyDiscount(discountRequest);

        verify(shoppingCart, times(1)).applyDiscount(discountRequest);
        verify(eventService, times(1)).recordDiscountEvent(EventType.APPLY_DISCOUNT, 10);
    }

    @Test
    void testGetCartItems() {
        Map<String, Item> items = Collections.singletonMap("SKU123", new Item("SKU123", 2, "shoes", BigDecimal.valueOf(100)));

        when(shoppingCart.getItems()).thenReturn(items);

        Map<String, Item> result = shoppingCartService.getCartItems();

        assertEquals(items, result);
    }

    @Test
    void testGetTotalAmount() {
        BigDecimal totalAmount = BigDecimal.valueOf(100);

        when(shoppingCart.getTotalAmount()).thenReturn(totalAmount);

        BigDecimal result = shoppingCartService.getTotalAmount();

        assertEquals(totalAmount, result);
    }

    @Test
    void testValidateQuantityValid() {
        assertDoesNotThrow(() -> shoppingCartService.validateQuantity(5));
    }

    @Test
    void testValidateQuantityInvalid() {
        ItemQuantityExceedsException exception = assertThrows(ItemQuantityExceedsException.class,
                () -> shoppingCartService.validateQuantity(1001));

        assertEquals("Quantity must be between 1 and 1000.", exception.getMessage());
    }

    @Test
    void testValidateDiscountPercentageValid() {
        assertDoesNotThrow(() -> shoppingCartService.validateDiscountPercentage(10));
    }

    @Test
    void testValidateDiscountPercentageInvalid() {
        DiscountValidationException exception = assertThrows(DiscountValidationException.class,
                () -> shoppingCartService.validateDiscountPercentage(-5));

        assertEquals("Discount percentage must be between 0 and 100.", exception.getMessage());
    }

    CartItemRequest getCartItemRequest() {
        return new CartItemRequest("SKU123", 2, "shoes", BigDecimal.valueOf(100));
    }
}
