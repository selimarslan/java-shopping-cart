package com.commerce.coupon;

import com.commerce.campaign.Campaign;
import com.commerce.campaign.DiscountHeap;
import com.commerce.cart.Product;
import com.commerce.cart.ShoppingCart;
import com.commerce.category.Category;
import com.commerce.delivery.DeliveryCostCalculator;
import com.commerce.shared.DiscountType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CouponTest {
    ShoppingCart cart = new ShoppingCart();

    @Before
    public void setUp() throws Exception {

        Category foodCategory = new Category("Food");
        Category fruitCategory = new Category("Fruit", foodCategory);
        Category vegetableCategory = new Category("Vegetable", foodCategory);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product mango = new Product("Mango", 150.0, fruitCategory);
        Product onion = new Product("Onion", 40.0, vegetableCategory);

        Campaign fruitCampaign = new Campaign(fruitCategory, 20d, 15, DiscountType.Rate);

        cart.addItem(apple, 5);
        cart.addItem(mango, 15);
        cart.addItem(onion, 10);

        cart.applyDiscounts(fruitCampaign); // 3150 - 2600



    }

    @Test
    public void testCalculateDiscountByAmount() throws Exception {
        Coupon coupon = new Coupon(2000d, 1230.23d, DiscountType.Amount);
        CouponDiscountCalculator calculator = new CouponDiscountCalculatorByAmount();

        cart.applyCoupon(coupon);
        double result = calculator.calculateDiscount(cart, coupon);

        Assert.assertEquals(1230.23, result, 0.001);
    }

    @Test
    public void testCalculateDiscountByRate() throws Exception {
        Coupon coupon = new Coupon(2000d, 10d, DiscountType.Rate);
        CouponDiscountCalculator calculator = new CouponDiscountCalculatorByRate();

        cart.applyCoupon(coupon);
        double result = calculator.calculateDiscount(cart, coupon);

        Assert.assertEquals(260, result, 0.001);
    }

}