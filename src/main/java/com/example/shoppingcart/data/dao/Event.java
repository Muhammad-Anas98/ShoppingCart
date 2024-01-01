package com.example.shoppingcart.data.dao;

import com.example.shoppingcart.util.EventType;
import com.example.shoppingcart.data.dto.CartItemRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import static com.example.shoppingcart.util.Constants.DUMMY_ORDER_ID;

@Data
@AllArgsConstructor
public class Event {
    private EventType eventType;
    private CartItemRequest item;
    private int discountPercentage;
    private LocalDateTime timestamp;
    private String actionBy;
    private Long orderId; // can be catered by cartId as well. This is specifically to have a correlation between cart and order.

    public Event(){
        this.timestamp = LocalDateTime.now();
        this.orderId = DUMMY_ORDER_ID;
    }

    public Event(EventType eventType, int discountPercentage) {
        this();
        this.eventType = eventType;
        this.discountPercentage = discountPercentage;
    }

    public Event(EventType eventType, CartItemRequest item, String actionBy) {
        this();
        this.eventType = eventType;
        this.item = item;
        this.actionBy = actionBy;
    }

    public Event(EventType eventType, String actionBy) {
        this();
        this.eventType = eventType;
        this.actionBy = actionBy;
    }
}