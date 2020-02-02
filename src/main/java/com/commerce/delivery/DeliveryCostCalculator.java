package com.commerce;

import java.awt.*;
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

interface DeliveryCost{
    double calculateAmount(ShoppingCart cart, DeliveryCostCalculator calculator);
}

class PerDeliveryCost implements DeliveryCost{

    @Override
    public double calculateAmount(ShoppingCart cart, DeliveryCostCalculator calculator) {
        double deliveryCost = getNumberOfDeliveries(cart.getItems(), calculator) * calculator.getCostPerDelivery();
        return deliveryCost;
    }

    private int getNumberOfDeliveries(List<LineItem> items, DeliveryCostCalculator calculator){
        Set<Category> allSingleLevelCategories = new HashSet<>();
        for (LineItem item : items){
            allSingleLevelCategories.add(item.getProduct().getCategory());
        }
        DistinctCategory distinctCategoryStrategy = calculator.getDistinctCategoryStrategy();
        List<Category> distinctCategories = distinctCategoryStrategy.getDistinctCategories(allSingleLevelCategories);
        return distinctCategories.size();
    }
}

class PerProductCost implements DeliveryCost{

    @Override
    public double calculateAmount(ShoppingCart cart, DeliveryCostCalculator calculator) {
        return cart.getItems().size() * calculator.getCostPerProduct();
    }
}

class FixedCost implements DeliveryCost{

    @Override
    public double calculateAmount(ShoppingCart cart, DeliveryCostCalculator calculator) {
        return calculator.getFixedCost();
    }
}

interface DistinctCategory{
    List<Category> getDistinctCategories(Collection<Category> singleLevelCategories);
}

class DistinctCategoriesWithoutHierarchy implements DistinctCategory{

    @Override
    public List<Category> getDistinctCategories(Collection<Category> singleLevelCategories) {
        return new ArrayList<>(singleLevelCategories);
    }
}

class DistinctCategoriesWithHierarchy implements DistinctCategory{

    @Override
    public List<Category> getDistinctCategories(Collection<Category> singleLevelCategories) {
        Set<Category> result = new HashSet<>();
        for(Category category: singleLevelCategories){
            while (category.parentCategory != null){
                category = category.parentCategory;
            }
            result.add(category);
        }
        return new ArrayList<>(result);
    }
}
