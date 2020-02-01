package com.commerce;

class Coupon{
    Double minPurchaseAmount;
    Double discountFactor;
    DiscountType discountType;

    Coupon(Double minPurchaseAmount, Double discountFactor, DiscountType discountType){
        this.minPurchaseAmount = minPurchaseAmount;
        this.discountFactor = discountFactor;
        this.discountType = discountType;
    }
}
