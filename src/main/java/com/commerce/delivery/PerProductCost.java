package com.commerce.delivery;

import com.commerce.cart.ShoppingCart;

class PerProductCost implements DeliveryCost{

    @Override
    public double calculateAmount(ShoppingCart cart, DeliveryCostCalculator calculator) {
        return cart.getItems().size() * calculator.getCostPerProduct();
    }
}
