package com.commerce.campaign;

import com.commerce.category.Category;
import com.commerce.shared.DiscountType;

public class Campaign{
    private Category category;
    private Double discountFactor;
    private int quantityLowerLimit;
    private DiscountType discountType;

    public Campaign(Category category, Double discountFactor, int quantityLowerLimit, DiscountType discountType){
        this.category = category;
        this.discountFactor = discountFactor;
        this.quantityLowerLimit = quantityLowerLimit;
        this.discountType = discountType;
    }

    public int getLowerLimit() {
        return quantityLowerLimit;
    }

    public double getDiscountFactor() {
        return discountFactor;
    }

    public Category getCategory() {
        return category;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }
}