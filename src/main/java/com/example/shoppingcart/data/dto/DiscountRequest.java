package com.example.shoppingcart.data.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class DiscountRequest {
  @NonNull String discountCode;
  @NonNull private int discountPercentage;
}
