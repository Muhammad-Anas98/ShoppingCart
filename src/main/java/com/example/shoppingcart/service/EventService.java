package com.example.shoppingcart.service;

import com.example.shoppingcart.util.EventType;
import com.example.shoppingcart.data.dao.Event;
import com.example.shoppingcart.data.dto.CartItemRequest;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.shoppingcart.util.Constants.USER_UUID;

@Service
public class EventService {
    private final List<Event> events = new ArrayList<>();

    public void recordCartEvent(EventType eventType, CartItemRequest cartItem) {
        Event event = new Event(eventType, cartItem, ThreadContext.get(USER_UUID));
        events.add(event);
    }

    public void recordCartEvent(EventType eventType) {
        Event event = new Event(eventType, ThreadContext.get(USER_UUID));
        events.add(event);
    }

    public void recordDiscountEvent(EventType eventType, int discountPercentage) {
        Event event = new Event(eventType, discountPercentage);
        events.add(event);
    }

    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }
}