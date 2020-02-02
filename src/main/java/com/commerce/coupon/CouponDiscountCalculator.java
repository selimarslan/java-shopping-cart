package com.commerce.coupon;

import com.commerce.cart.ShoppingCart;

public interface CouponDiscountCalculator{
    double calculateDiscount(ShoppingCart cart, Coupon coupon);
}
