package com.example.shoppingcart;

import com.example.shoppingcart.data.ShoppingCart;
import com.example.shoppingcart.data.dao.Item;
import com.example.shoppingcart.data.dto.CartItemRequest;
import com.example.shoppingcart.data.dto.DiscountRequest;
import com.example.shoppingcart.exception.DiscountValidationException;
import com.example.shoppingcart.exception.ItemQuantityException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Map;
import static com.example.shoppingcart.util.TestUtility.getCartItemRequest;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    @Test
    void testAddItem() {
        ShoppingCart shoppingCart = new ShoppingCart();

        CartItemRequest cartItemRequest = getCartItemRequest(2, BigDecimal.valueOf(100));
        shoppingCart.addItem(cartItemRequest);

        Map<String, Item> items = shoppingCart.getItems();

        assertEquals(1, items.size());
        assertTrue(items.containsKey("SKU123"));
        assertEquals(2, items.get("SKU123").getQuantity());
    }

    @Test
    void testRemoveItem() {
        ShoppingCart shoppingCart = new ShoppingCart();

        CartItemRequest cartItemRequest = getCartItemRequest(2, BigDecimal.valueOf(100));
        shoppingCart.addItem(cartItemRequest);

        Item removedItem = shoppingCart.removeItem("SKU123");

        assertNull(shoppingCart.getItems().get("SKU123"));
        assertEquals(2, removedItem.getQuantity());
    }

    @Test
    void testEmptyCart() {
        ShoppingCart shoppingCart = new ShoppingCart();

        CartItemRequest cartItemRequest = getCartItemRequest(2, BigDecimal.valueOf(100));
        shoppingCart.addItem(cartItemRequest);

        shoppingCart.emptyCart();

        assertTrue(shoppingCart.getItems().isEmpty());
    }

    @Test
    void testChangeQuantity() {
        ShoppingCart shoppingCart = new ShoppingCart();

        CartItemRequest cartItemRequest = getCartItemRequest(2, BigDecimal.valueOf(100));
        shoppingCart.addItem(cartItemRequest);

        CartItemRequest updatedCartItemRequest = getCartItemRequest(7, BigDecimal.valueOf(100));
        shoppingCart.changeQuantity(updatedCartItemRequest);

        Map<String, Item> items = shoppingCart.getItems();

        assertEquals(1, items.size());
        assertTrue(items.containsKey("SKU123"));
        assertEquals(9, items.get("SKU123").getQuantity());
    }

    @Test
    void testChangeQuantityExceedsLimit() {
        ShoppingCart shoppingCart = new ShoppingCart();

        CartItemRequest cartItemRequest = getCartItemRequest(2, BigDecimal.valueOf(100));
        shoppingCart.addItem(cartItemRequest);

        CartItemRequest updatedCartItemRequest = getCartItemRequest(1000, BigDecimal.valueOf(100));

        ItemQuantityException exception = assertThrows(ItemQuantityException.class,
                () -> shoppingCart.changeQuantity(updatedCartItemRequest));

        assertEquals("Overall Quantity must be less than 1000.", exception.getMessage());
    }

    @Test
    void testApplyDiscount() {
        ShoppingCart shoppingCart = new ShoppingCart();
        DiscountRequest discountRequest = new DiscountRequest("DISCOUNT_10");

        shoppingCart.addItem(getCartItemRequest(2, BigDecimal.valueOf(100)));
        shoppingCart.applyDiscount(discountRequest);

        assertEquals(180, shoppingCart.getTotalAmount().intValue());
    }

    @Test
    void testApplyDiscountTwice() {
        ShoppingCart shoppingCart = new ShoppingCart();
        DiscountRequest discountRequest = new DiscountRequest("DISCOUNT_10");
        shoppingCart.addItem(getCartItemRequest(2, BigDecimal.valueOf(100)));

        shoppingCart.applyDiscount(discountRequest);
        shoppingCart.applyDiscount(discountRequest);


        assertEquals(180, shoppingCart.getTotalAmount().intValue());
    }

    @Test
    void testGetItems() {
        ShoppingCart shoppingCart = new ShoppingCart();

        assertTrue(shoppingCart.getItems().isEmpty());

        CartItemRequest cartItemRequest = getCartItemRequest(4, BigDecimal.valueOf(100));
        shoppingCart.addItem(cartItemRequest);

        assertFalse(shoppingCart.getItems().isEmpty());
    }

    @Test
    void testGetTotalAmount() {
        ShoppingCart shoppingCart = new ShoppingCart();

        CartItemRequest cartItemRequest = getCartItemRequest(2, BigDecimal.valueOf(100));
        shoppingCart.addItem(cartItemRequest);

        assertEquals(200, shoppingCart.getTotalAmount().intValue());
    }


    @Test
    void testGetItemsUnmodifiable() {
        ShoppingCart shoppingCart = new ShoppingCart();
        CartItemRequest cartItemRequest = getCartItemRequest(5, BigDecimal.valueOf(100));
        shoppingCart.addItem(cartItemRequest);

        Map<String, Item> items = shoppingCart.getItems();
        assertThrows(UnsupportedOperationException.class, () -> items.put("SKU456", new Item()));

        assertEquals(1, items.size());
    }

    @Test
    void testGetTotalAmountWithDiscount() {
        ShoppingCart shoppingCart = new ShoppingCart();
        CartItemRequest cartItemRequest = getCartItemRequest(5, BigDecimal.valueOf(10));
        shoppingCart.addItem(cartItemRequest);

        DiscountRequest discountRequest = new DiscountRequest("DISCOUNT_10");
        shoppingCart.applyDiscount(discountRequest);

        assertEquals(45, shoppingCart.getTotalAmount().intValue());
    }

}

