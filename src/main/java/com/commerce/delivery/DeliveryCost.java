package com.commerce.delivery;

import com.commerce.cart.ShoppingCart;

interface DeliveryCost{
    double calculateAmount(ShoppingCart cart, DeliveryCostCalculator calculator);
}
