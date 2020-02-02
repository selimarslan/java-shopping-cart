package com.commerce.coupon;

import com.commerce.cart.ShoppingCart;

public class CouponDiscountCalculatorByAmount implements CouponDiscountCalculator{

    @Override
    public double calculateDiscount(ShoppingCart cart, Coupon coupon) {
        return coupon.getDiscountFactor();
    }
}
