package com.commerce.delivery;

import com.commerce.cart.LineItem;
import com.commerce.cart.ShoppingCart;
import com.commerce.category.Category;
import com.commerce.shared.DistinctCategory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
