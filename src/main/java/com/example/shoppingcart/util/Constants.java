package com.example.shoppingcart.util;

import java.util.Map;

public class Constants {
    public static final Map<String , Integer> DISCOUNTS = Map.of(
            "DISCOUNT_10", 10,
            "DISCOUNT_20", 20,
            "DISCOUNT_30", 30,
            "DISCOUNT_40", 40,
            "DISCOUNT_50", 50
    );

    public static final String USER_UUID = "User-UUID";

    public static final Long DUMMY_ORDER_ID = 3412762L;
}
