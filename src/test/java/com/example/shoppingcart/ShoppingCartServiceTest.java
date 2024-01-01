package com.example.shoppingcart;

import com.example.shoppingcart.util.EventType;
import com.example.shoppingcart.data.ShoppingCart;
import com.example.shoppingcart.data.dao.Item;
import com.example.shoppingcart.data.dto.CartItemRequest;
import com.example.shoppingcart.data.dto.DiscountRequest;

import com.example.shoppingcart.exception.DiscountValidationException;
import com.example.shoppingcart.exception.ItemQuantityException;
import com.example.shoppingcart.service.EventService;
import com.example.shoppingcart.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.example.shoppingcart.util.TestUtility.getCartItemRequest;
import static com.example.shoppingcart.util.TestUtility.getItemsMap;
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
        CartItemRequest cartItemRequest = getCartItemRequest(2, BigDecimal.valueOf(100));

        when(shoppingCart.getItems()).thenReturn(getItemsMap(2, BigDecimal.valueOf(100)));
        doNothing().when(eventService).recordCartEvent(EventType.ADD_ITEM, cartItemRequest);

        shoppingCartService.addItemToCart(cartItemRequest);

        verify(shoppingCart, times(1)).addItem(cartItemRequest);
        verify(eventService, times(1)).recordCartEvent(EventType.ADD_ITEM, cartItemRequest);
    }

    @Test
    void testRemoveItemFromCart() {
        String sku = "SKU123";
        Item removedItem = new Item("SKU123", 2, "shoes", BigDecimal.valueOf(100));

        when(shoppingCart.getItems()).thenReturn(getItemsMap(2, BigDecimal.valueOf(100)));

        when(shoppingCart.removeItem(sku)).thenReturn(removedItem);
        doNothing().when(eventService).recordCartEvent(EventType.REMOVE_ITEM, removedItem.convertToCartItemDto());

        shoppingCartService.removeItemFromCart(sku);

        verify(shoppingCart, times(1)).removeItem(sku);
        verify(eventService, times(1)).recordCartEvent(EventType.REMOVE_ITEM, removedItem.convertToCartItemDto());
    }

    @Test
    void testEmptyCart() {
        doNothing().when(shoppingCart).emptyCart();
        doNothing().when(eventService).recordCartEvent(EventType.EMPTY_CART);

        shoppingCartService.emptyCart();

        verify(shoppingCart, times(1)).emptyCart();
        verify(eventService, times(1)).recordCartEvent(EventType.EMPTY_CART);
    }

    @Test
    void testChangeItemQuantity() {
        CartItemRequest cartItemRequest = getCartItemRequest(2, BigDecimal.valueOf(100));

        when(shoppingCart.getItems()).thenReturn(getItemsMap(2, BigDecimal.valueOf(100)));
        when(shoppingCart.getTotalAmount()).thenReturn(BigDecimal.valueOf(200));
        doNothing().when(shoppingCart).changeQuantity(cartItemRequest);
        doNothing().when(eventService).recordCartEvent(EventType.UPDATE_ITEM, cartItemRequest);

        shoppingCartService.changeItemQuantity(cartItemRequest);

        verify(shoppingCart, times(1)).changeQuantity(cartItemRequest);
        verify(eventService, times(1)).recordCartEvent(EventType.UPDATE_ITEM, cartItemRequest);
    }


    @Test
    void testApplyDiscount() {
        DiscountRequest discountRequest = new DiscountRequest("DISCOUNT_10");

        when(shoppingCart.getTotalAmount()).thenReturn(BigDecimal.valueOf(180));
        when(shoppingCart.applyDiscount(discountRequest)).thenReturn(10);
        doNothing().when(eventService).recordDiscountEvent(EventType.APPLY_DISCOUNT, 10);

        shoppingCartService.applyDiscount(discountRequest);

        verify(shoppingCart, times(1)).applyDiscount(discountRequest);
        verify(eventService, times(1)).recordDiscountEvent(EventType.APPLY_DISCOUNT, 10);
    }

    @Test
    void testGetCartItems() {
        Map<String, Item> items = getItemsMap(2, BigDecimal.valueOf(100));

        when(shoppingCart.getItems()).thenReturn(items);

        List<Item> result = shoppingCartService.getCartItems();

        assertEquals(items.values().stream().toList(), result);
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
        ItemQuantityException exception = assertThrows(ItemQuantityException.class,
                () -> shoppingCartService.validateQuantity(1001));

        assertEquals("Quantity must be between 1 and 1000.", exception.getMessage());
    }

    @Test
    void testValidateDiscountPercentageValid() {
        assertDoesNotThrow(() -> shoppingCartService.validateDiscountPercentage("DISCOUNT_10"));
    }

    @Test
    void testValidateDiscountPercentageInvalid() {
        DiscountValidationException exception = assertThrows(DiscountValidationException.class,
                () -> shoppingCartService.validateDiscountPercentage("DISCOUNT_101"));

        assertEquals("Discount cant be applied.", exception.getMessage());
    }
}
