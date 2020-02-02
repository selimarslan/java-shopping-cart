package com.commerce.delivery;

import com.commerce.cart.ShoppingCart;

class FixedCost implements DeliveryCost{

    @Override
    public double calculateAmount(ShoppingCart cart, DeliveryCostCalculator calculator) {
        return calculator.getFixedCost();
    }
}
