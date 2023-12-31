package com.example.shoppingcart;

import com.example.shoppingcart.constant.EventType;
import com.example.shoppingcart.data.dao.Event;
import com.example.shoppingcart.data.dto.CartItemRequest;
import com.example.shoppingcart.service.EventService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventServiceTest {

    @Test
    void testRecordCartAddEvent() {
        EventService eventService = new EventService();
        CartItemRequest cartItemRequest = new CartItemRequest("SKU123", 2, "shows", BigDecimal.valueOf(100));

        LocalDateTime beforeRecord = LocalDateTime.now();
        eventService.recordCartAddEvent(EventType.ADD_ITEM, cartItemRequest);
        LocalDateTime afterRecord = LocalDateTime.now();

        List<Event> events = eventService.getAllEvents();

        assertEquals(1, events.size());
        Event recordedEvent = events.get(0);
        assertEquals(EventType.ADD_ITEM, recordedEvent.getEventType());
        assertEquals(cartItemRequest, recordedEvent.getItem());
        assertTrue(recordedEvent.getTimestamp().isAfter(beforeRecord));
        assertTrue(recordedEvent.getTimestamp().isBefore(afterRecord));
    }

    @Test
    void testRecordDiscountEvent() {
        EventService eventService = new EventService();

        LocalDateTime beforeRecord = LocalDateTime.now();
        eventService.recordDiscountEvent(EventType.APPLY_DISCOUNT, 10);
        LocalDateTime afterRecord = LocalDateTime.now();

        List<Event> events = eventService.getAllEvents();

        assertEquals(1, events.size());
        Event recordedEvent = events.get(0);
        assertEquals(EventType.APPLY_DISCOUNT, recordedEvent.getEventType());
        assertEquals(10, recordedEvent.getDiscountPercentage());
        assertTrue(recordedEvent.getTimestamp().isAfter(beforeRecord));
        assertTrue(recordedEvent.getTimestamp().isBefore(afterRecord));
    }

    @Test
    void testGetAllEvents() {
        EventService eventService = new EventService();
        CartItemRequest cartItemRequest = new CartItemRequest("SKU123", 2, "shows", BigDecimal.valueOf(100));

        eventService.recordCartAddEvent(EventType.ADD_ITEM, cartItemRequest);
        eventService.recordDiscountEvent(EventType.APPLY_DISCOUNT, 10);

        List<Event> events = eventService.getAllEvents();

        assertEquals(2, events.size());
        assertEquals(EventType.ADD_ITEM, events.get(0).getEventType());
        assertEquals(EventType.APPLY_DISCOUNT, events.get(1).getEventType());
    }
}
