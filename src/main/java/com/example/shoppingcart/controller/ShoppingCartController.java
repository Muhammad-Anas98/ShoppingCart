package com.example.shoppingcart.controller;

import com.example.shoppingcart.data.dao.Item;
import com.example.shoppingcart.data.dto.CartItemRequest;
import com.example.shoppingcart.data.dto.DiscountRequest;
import com.example.shoppingcart.service.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Validated
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public ResponseEntity<String> addItemToCart(@Valid @RequestBody CartItemRequest cartItemRequest) {
      shoppingCartService.addItemToCart(cartItemRequest);
      return ResponseEntity.ok("Item added to the cart successfully.");
    }

    @DeleteMapping("/remove/{sku}")
    public ResponseEntity<String> removeItemFromCart(@PathVariable String sku) {
      shoppingCartService.removeItemFromCart(sku);
      return ResponseEntity.ok("Item removed from the cart successfully.");
    }

    @PostMapping("/empty")
    public ResponseEntity<String> emptyCart() {
        shoppingCartService.emptyCart();
        return ResponseEntity.ok("Cart emptied successfully.");
    }

    @PatchMapping("/update/{sku}/{quantity}")
    public ResponseEntity<String> changeItemQuantity(@PathVariable String sku, @PathVariable int quantity) {
      shoppingCartService.changeItemQuantity(new CartItemRequest(sku, quantity, null, null));
      return ResponseEntity.ok("Item quantity updated successfully.");
    }

    @PostMapping("/apply-discount")
    public ResponseEntity<String> applyDiscount(@RequestBody @Valid DiscountRequest discountRequest) {
      shoppingCartService.applyDiscount(discountRequest);
      return ResponseEntity.ok("Discount applied successfully.");
    }

    @GetMapping("/items")
    public ResponseEntity<List<Item>> getCartItems() {
        return ResponseEntity.ok(shoppingCartService.getCartItems());
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalAmount() {
        return ResponseEntity.ok(shoppingCartService.getTotalAmount());
    }
}
