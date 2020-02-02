package com.commerce.delivery;

import com.commerce.cart.ShoppingCart;
import com.commerce.shared.DistinctCategoriesWithoutHierarchy;
import com.commerce.shared.DistinctCategory;

import java.util.*;
import java.util.List;

public class DeliveryCostCalculator {

    private final double costPerDelivery;
    private final double costPerProduct;
    private final double fixedCost;
    private final List<DeliveryCost> deliveryCostList;
    private DistinctCategory distinctCategoryStrategy;

    public DeliveryCostCalculator(double costPerDelivery, double costPerProduct, double fixedCost) {
        this.costPerDelivery = costPerDelivery;
        this.costPerProduct = costPerProduct;
        this.fixedCost = fixedCost;

        distinctCategoryStrategy = new DistinctCategoriesWithoutHierarchy();

        deliveryCostList = new ArrayList<>();
        deliveryCostList.add(new PerDeliveryCost());
        deliveryCostList.add(new PerProductCost());
        deliveryCostList.add(new FixedCost());
    }


    public double calculateFor(ShoppingCart cart) {
        double result = 0;
        for (DeliveryCost deliveryCost : deliveryCostList){
            result += deliveryCost.calculateAmount(cart, this);
        }
        return result;
    }


    public double getCostPerDelivery() {
        return costPerDelivery;
    }

    public double getCostPerProduct() {
        return costPerProduct;
    }

    public double getFixedCost() {
        return fixedCost;
    }

    public DistinctCategory getDistinctCategoryStrategy() {
        return distinctCategoryStrategy;
    }

    public void setDistinctCategoryStrategy(DistinctCategory distinctCategoryStrategy) {
        this.distinctCategoryStrategy = distinctCategoryStrategy;
    }

    public DeliveryCost[] getDeliveryCostList() {
        return deliveryCostList.toArray(new DeliveryCost[0]);
    }

    public void addDeliveryCost(DeliveryCost deliveryCost) {
        deliveryCostList.add(deliveryCost);
    }
}

