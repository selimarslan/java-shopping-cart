package com.commerce.coupon;

import com.commerce.cart.ShoppingCart;

public class CouponDiscountCalculatorByRate implements CouponDiscountCalculator{

    @Override
    public double calculateDiscount(ShoppingCart cart, Coupon coupon) {
        double totalWithCampaingDiscount = cart.getTotalAmount() - cart.getCampaignDiscount();
        double couponDiscount = coupon.getDiscountFactor() / 100 * totalWithCampaingDiscount;
        return couponDiscount;
    }
}
