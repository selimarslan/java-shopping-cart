package com.commerce.delivery;

import com.commerce.cart.Product;
import com.commerce.cart.ShoppingCart;
import com.commerce.category.Category;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DeliveryCostTest {
    DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculator(10, 20, 30);
    ShoppingCart cart = new ShoppingCart();

    @Before
    public void setUp() throws Exception {

        Category foodCategory = new Category("Food");
        Category fruitCategory = new Category("Fruit", foodCategory);
        Category vegetableCategory = new Category("Vegetable", foodCategory);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product mango = new Product("Mango", 150.0, fruitCategory);
        Product onion = new Product("Onion", 40.0, vegetableCategory);

        cart.addItem(apple, 5);
        cart.addItem(mango, 15);
        cart.addItem(onion, 10);

    }

    @Test
    public void testCalculateAmountForFixedCost() {
        FixedCost fixedCost = new FixedCost();

        double result = fixedCost.calculateAmount(cart, deliveryCostCalculator);

        Assert.assertEquals(30, result, 0.0001);
    }

    @Test
    public void testCalculateAmountForPerProduct(){
        PerProductCost costCenter = new PerProductCost();

        double result = costCenter.calculateAmount(cart, deliveryCostCalculator);

        Assert.assertEquals(60, result, 0.0001);
    }

    @Test
    public void testCalculateAmountForPerCategory(){
        PerDeliveryCost costCenter = new PerDeliveryCost();

        double result = costCenter.calculateAmount(cart, deliveryCostCalculator);

        Assert.assertEquals(20, result, 0.0001);
    }

    @Test
    public void testDeliveryCost() throws Exception {
        DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculator(100d, 10d, 2.99);
        // deliveryCostCalculator.setDistinctCategoryStrategy(new DistinctCategoriesWithHierarchy());


        double deliveryCost = deliveryCostCalculator.calculateFor(cart);


        Assert.assertEquals(232.99, deliveryCost, 0.0001);
    }
}