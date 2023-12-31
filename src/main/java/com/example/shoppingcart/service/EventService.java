package com.example.shoppingcart.service;

import com.example.shoppingcart.constant.EventType;
import com.example.shoppingcart.data.dao.Event;
import com.example.shoppingcart.data.dto.CartItemRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final List<Event> events = new ArrayList<>();


    public void recordCartAddEvent(EventType eventType, CartItemRequest cartItem) {
        Event event = new Event(eventType, cartItem, LocalDateTime.now());
        events.add(event);
    }

    public void recordDiscountEvent(EventType eventType, int discountPercentage) {
        Event event = new Event(eventType, discountPercentage, LocalDateTime.now());
        events.add(event);
    }

    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }
}