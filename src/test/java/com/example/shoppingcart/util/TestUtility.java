package com.example.shoppingcart.util;

import com.example.shoppingcart.data.dao.Item;
import com.example.shoppingcart.data.dto.CartItemRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestUtility {
    public static CartItemRequest getCartItemRequest(int quantity, BigDecimal price) {
        return new CartItemRequest("SKU123", quantity, "shoes", price);
    }

    public static Map<String, Item> getItemsMap(int quantity, BigDecimal price) {
        return Collections.singletonMap("SKU123", new Item("SKU123", quantity, "shoes",price));
    }

}
