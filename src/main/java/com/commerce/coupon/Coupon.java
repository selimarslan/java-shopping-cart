package com.commerce;

class Coupon{
    private double minPurchaseAmount;
    private double discountFactor;
    private DiscountType discountType;

    Coupon(Double minPurchaseAmount, Double discountFactor, DiscountType discountType){
        this.minPurchaseAmount = minPurchaseAmount;
        this.discountFactor = discountFactor;
        this.discountType = discountType;
    }

    public Double getMinPurchaseAmount() {
        return minPurchaseAmount;
    }

    public void setMinPurchaseAmount(Double minPurchaseAmount) {
        this.minPurchaseAmount = minPurchaseAmount;
    }

    public Double getDiscountFactor() {
        return discountFactor;
    }

    public void setDiscountFactor(Double discountFactor) {
        this.discountFactor = discountFactor;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }



}
