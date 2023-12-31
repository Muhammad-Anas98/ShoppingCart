package com.example.shoppingcart.data.dao;

import com.example.shoppingcart.constant.EventType;
import com.example.shoppingcart.data.dto.CartItemRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private EventType eventType;
    private CartItemRequest item;
    private int discountPercentage;
    private LocalDateTime timestamp;

    public Event(EventType eventType, CartItemRequest item, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.item = item;
        this.timestamp = timestamp;

    }

    public Event(EventType eventType, int discountPercentage, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.discountPercentage = discountPercentage;
        this.timestamp = timestamp;
    }
}