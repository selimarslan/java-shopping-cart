package com.commerce;

import com.commerce.campaign.Campaign;
import com.commerce.campaign.CampaingDiscountManager;
import com.commerce.campaign.DiscountHeap;
import com.commerce.cart.LineItem;
import com.commerce.cart.Product;
import com.commerce.cart.ShoppingCart;
import com.commerce.category.Category;
import com.commerce.category.WeightedCategory;
import com.commerce.coupon.Coupon;
import com.commerce.delivery.DeliveryCostCalculator;
import com.commerce.shared.DiscountType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ShoppingCartTest {
    @Test
    public void shouldCreateShoppingCart(){
        ShoppingCart shoppingCart = new ShoppingCart();
    }

    @Test
    public void testAddItem() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        Category foodCategory = new Category("Food");
        Product apple = new Product("Apple", 100.0, foodCategory);
        shoppingCart.addItem(apple, 10);
        int itemCount = shoppingCart.getItemCount();
        Assert.assertEquals(1, itemCount);
    }

    @Test
    public void testCategories() {
        Category foodCategory = new Category("Food");
        Category vegetableCategory = new Category("Vegetable", foodCategory);
        Category fruitCategory = new Category("Fruit", foodCategory);
        Assert.assertEquals(foodCategory, fruitCategory.getParentCategory());
    }

    @Test
    public void testZeroCampaignDiscount() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        Category foodCategory = new Category("Food");
        Campaign campaign = new Campaign(foodCategory, 15.0, 5, DiscountType.Rate);
        Campaign campaignSecond = new Campaign(foodCategory, 55.0, 10, DiscountType.Amount);
        shoppingCart.applyDiscounts(campaign, campaignSecond);
        double campaignDiscount = shoppingCart.getCampaignDiscount();
        Assert.assertEquals(0, campaignDiscount, 0.1);
    }

    @Test
    public void testNoCouponDiscount() {
        ShoppingCart shoppingCart = new ShoppingCart();
        double couponDiscount = shoppingCart.getCouponDiscount();
        Assert.assertEquals(0, couponDiscount, 0.1);
    }



    @Test
    public void testLineItemSubTotal() throws Exception {
        Category foodCategory = new Category("Food");
        Product apple = new Product("Apple", 100.0, foodCategory);
        LineItem lineItem = new LineItem(apple, 3);
        Campaign campaign = new Campaign(foodCategory, 15.0, 5, DiscountType.Rate);
        lineItem.setCampaignDiscount(campaign, 35.23);
        lineItem.setCouponDiscount(12.23);
        double subTotal = lineItem.getSubTotal();
        Assert.assertEquals(252.54, subTotal, 0.1);
    }

    @Test
    public void testCampaignDiscountWithDifferentCategorySingleLevel() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();

        Category fruitCategory = new Category("Fruit");
        Category vegetableCategory = new Category("Vegetable");

        Campaign fruitCampaign = new Campaign(fruitCategory, 15.0, 5, DiscountType.Rate);
        Campaign vegetableCampaign = new Campaign(vegetableCategory, 55.0, 10, DiscountType.Rate);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product onion = new Product("Onion", 200.0, vegetableCategory);

        shoppingCart.addItem(apple, 5); // 500 75
        shoppingCart.addItem(onion, 10); // 2000 1100

        shoppingCart.applyDiscounts(fruitCampaign, vegetableCampaign);
        double campaignDiscount = shoppingCart.getCampaignDiscount();


        Assert.assertEquals(1175, campaignDiscount, 0.1);
    }

    @Test
    public void testCampaignDiscountWithSameCategorySingleLevel() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();

        Category fruitCategory = new Category("Fruit");

        Campaign fruitCampaign = new Campaign(fruitCategory, 15.0, 5, DiscountType.Rate);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product mango = new Product("Mango", 200.0, fruitCategory);

        shoppingCart.addItem(apple, 5); // 500 75
        shoppingCart.addItem(mango, 10); // 2000 300

        shoppingCart.applyDiscounts(fruitCampaign);
        double campaignDiscount = shoppingCart.getCampaignDiscount();

        Assert.assertEquals(375, campaignDiscount, 0.1);
    }

    @Test
    public void testCampaignDiscountWithParentCategory() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();

        Category foodCategory = new Category("Food");
        Category fruitCategory = new Category("Fruit", foodCategory);
        Category vegetableCategory = new Category("Vegetable", foodCategory);

        Campaign fruitCampaign = new Campaign(fruitCategory, 15.0, 5, DiscountType.Rate);
        Campaign vegetableCampaign = new Campaign(vegetableCategory, 25.0, 10, DiscountType.Rate);
        Campaign foodCampaign = new Campaign(foodCategory, 700d, 15, DiscountType.Amount);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product onion = new Product("Onion", 200.0, vegetableCategory);

        shoppingCart.addItem(apple, 5); // 500 75
        shoppingCart.addItem(onion, 10); // 2000 1100

        shoppingCart.applyDiscounts(fruitCampaign, vegetableCampaign, foodCampaign);
        double campaignDiscount = shoppingCart.getCampaignDiscount();

        Assert.assertEquals(700, campaignDiscount, 0.1);
    }

    @Test
    public void testWeightedCategory() {
        Category fruitCategory = new Category("Fruit");
        Campaign fruitCampaign = new Campaign(fruitCategory, 15.0, 5, DiscountType.Rate);
        Campaign goldFruitCampaign = new Campaign(fruitCategory, 278.16, 8, DiscountType.Amount);
        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product mango = new Product("Mango", 150.0, fruitCategory);

        WeightedCategory weightedCategory = new WeightedCategory(fruitCategory);
        weightedCategory.addCampaign(fruitCampaign);
        weightedCategory.addCampaign(goldFruitCampaign);
        LineItem lineItem = new LineItem(apple, 4);
        weightedCategory.addLineItem(lineItem);
        weightedCategory.addLineItem(new LineItem(mango, 5));
        double maxDiscount = weightedCategory.getMaxDiscount();
        Campaign maxCampaign = weightedCategory.getMaxCampaign();

        Assert.assertEquals(278.16, maxDiscount, 0.1);
        Assert.assertEquals(goldFruitCampaign, maxCampaign);
    }

    WeightedCategory createWeightedCategory(String categoryTitle, String productTitle, double discount){
        Category category = new Category(categoryTitle);
        Campaign fruitCampaign = new Campaign(category, discount, 1, DiscountType.Rate);
        Product apple = new Product(productTitle, 100.0, category);
        WeightedCategory weightedCategory = new WeightedCategory(category);
        weightedCategory.addCampaign(fruitCampaign);
        weightedCategory.addLineItem(new LineItem(apple, 1));
        return weightedCategory;
    }

    @Test
    public void testDiscountHeap() {
        DiscountHeap discountHeap = new DiscountHeap();

        WeightedCategory fruitApple50 = createWeightedCategory("Fruit", "Apple", 50);
        WeightedCategory vegetableOnion30 = createWeightedCategory("Vegetable", "Onion", 30);
        WeightedCategory foodAppleAndOnion60 = createWeightedCategory("Food", "(Apple,Onion)", 60);
        WeightedCategory groceryFoodAndCoffe70 = createWeightedCategory("Grocery", "Food(Apple,Onion),Coffee", 70);


        discountHeap.addWeightedCategory(fruitApple50);
        WeightedCategory temp = discountHeap.getMaxDiscount();
        discountHeap.addWeightedCategory(groceryFoodAndCoffe70);
        discountHeap.addWeightedCategory(foodAppleAndOnion60);
        discountHeap.addWeightedCategory(vegetableOnion30);

        WeightedCategory maxDiscountBeforeRemove70 = discountHeap.getMaxDiscount();

        boolean b = discountHeap.removeWeightedCategory( groceryFoodAndCoffe70 );
        WeightedCategory maxDiscountAfterRemove70 = discountHeap.getMaxDiscount();

        Assert.assertEquals(50, fruitApple50.getMaxDiscount(), 0.0001);
        Assert.assertEquals(30, vegetableOnion30.getMaxDiscount(), 0.0001);
        Assert.assertEquals(groceryFoodAndCoffe70, maxDiscountBeforeRemove70);
        Assert.assertEquals(foodAppleAndOnion60, maxDiscountAfterRemove70);
    }

    @Test
    public void testCampaignDiscountManager(){

        Category groceryCategory = new Category("Grocery");
        Category foodCategory = new Category("Food", groceryCategory);
        Category beverageCategory = new Category("Beverage", groceryCategory);
        Category fruitCategory = new Category("Fruit", foodCategory);
        Category vegetableCategory = new Category("Vegetable", foodCategory);

        Category vehicleCategory = new Category("Vehicle");
        Category suvCategory = new Category("Suv", vehicleCategory);
        Category sedanCategory = new Category("Sedan", vehicleCategory);

        Category homeCategory = new Category("Home");

        Campaign fruitCampaign = new Campaign(fruitCategory, 15d, 5, DiscountType.Rate);
        Campaign fruitCampaignGold = new Campaign(fruitCategory, 17d, 10, DiscountType.Rate);
        Campaign vegetableCampaign = new Campaign(vegetableCategory, 20d, 10, DiscountType.Rate);
        Campaign beverageCampaign = new Campaign(beverageCategory, 100d, 10, DiscountType.Amount);
        Campaign foodCampaign = new Campaign(foodCategory, 452.23, 20, DiscountType.Amount);
        Campaign groceryCampaign = new Campaign(groceryCategory, 21d, 10, DiscountType.Rate);

        Campaign vehicleCampaign = new Campaign(vehicleCategory, 19d, 3, DiscountType.Rate);
        Campaign suvCampaign = new Campaign(suvCategory, 23d, 2, DiscountType.Rate);
        Campaign sedanCampaign = new Campaign(sedanCategory, 18d, 4, DiscountType.Rate);

        Campaign homeCampaign = new Campaign(homeCategory, 12d, 2, DiscountType.Rate);


        Campaign[] campaigns = new Campaign[10];
        campaigns[0] = fruitCampaign;
        campaigns[1] = vegetableCampaign;
        campaigns[2] = beverageCampaign;
        campaigns[3] = foodCampaign;
        campaigns[4] = fruitCampaignGold;
        campaigns[5] = groceryCampaign;

        campaigns[6] = vehicleCampaign;
        campaigns[7] = suvCampaign;
        campaigns[8] = sedanCampaign;

        campaigns[9] = homeCampaign;

        Product apple = new Product("Apple", 100.0, fruitCategory);
        LineItem appleItem = new LineItem(apple, 5);

        Product mango = new Product("Mango", 150.0, fruitCategory);
        LineItem mangoItem = new LineItem(mango, 15);

        Product onion = new Product("Onion", 40.0, foodCategory);
        LineItem onionItem = new LineItem(onion, 10);

        Product potato = new Product("Potato", 52.0, vegetableCategory);
        LineItem potatoItem = new LineItem(potato, 8);

        Product corolla = new Product("Corrolla", 1340.0, sedanCategory);
        LineItem corollaItem = new LineItem(corolla, 5);

        Product kodiaq = new Product("Kodiaq", 1810.0, suvCategory);
        LineItem kodiaqItem = new LineItem(kodiaq, 3);

        Product home = new Product("Home", 32382.0, homeCategory);
        LineItem homeItem = new LineItem(home, 3);

        CampaingDiscountManager campaingDiscountManager = new CampaingDiscountManager(campaigns);
        campaingDiscountManager.addItem(appleItem);
        campaingDiscountManager.addItem(mangoItem);
        campaingDiscountManager.addItem(onionItem);
        campaingDiscountManager.addItem(potatoItem);

        campaingDiscountManager.addItem(corollaItem);
        campaingDiscountManager.addItem(kodiaqItem);
        campaingDiscountManager.addItem(homeItem);

        List<WeightedCategory> weightedCategories = campaingDiscountManager.getMaxDiscount();
        double maxDiscount = 0;
        for (WeightedCategory weightedCategory: weightedCategories){
            maxDiscount += weightedCategory.getMaxDiscount();
        }
        Assert.assertEquals(14711.08, maxDiscount, 0.001);
    }

    @Test
    public void testCouponDiscount() throws Exception {
        Category foodCategory = new Category("Food");
        Category fruitCategory = new Category("Fruit", foodCategory);
        Category vegetableCategory = new Category("Vegetable", foodCategory);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product mango = new Product("Mango", 150.0, fruitCategory);
        Product onion = new Product("Onion", 40.0, vegetableCategory);

        Campaign fruitCampaign = new Campaign(fruitCategory, 20d, 15, DiscountType.Rate);

        ShoppingCart cart = new ShoppingCart();
        cart.addItem(apple, 5);
        cart.addItem(mango, 15);
        cart.addItem(onion, 10);

        cart.applyDiscounts(fruitCampaign); // 3150 - 2600

        Coupon coupon = new Coupon(2000.0, 10.0, DiscountType.Rate);
        cart.applyCoupon(coupon);
        double couponDiscount = cart.getCouponDiscount();
        Assert.assertEquals(260, couponDiscount, 0.001);
    }

    @Test
    public void testDeliveryCost() throws Exception {

        Category foodCategory = new Category("Food");
        Category fruitCategory = new Category("Fruit", foodCategory);
        Category vegetableCategory = new Category("Vegetable", foodCategory);

        Product apple = new Product("Apple", 100.0, fruitCategory);
        Product mango = new Product("Mango", 150.0, fruitCategory);
        Product onion = new Product("Onion", 40.0, vegetableCategory);

        ShoppingCart cart = new ShoppingCart();
        cart.addItem(apple, 5);
        cart.addItem(mango, 15);
        cart.addItem(onion, 10);


        DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculator(100d, 10d, 2.99);
        // deliveryCostCalculator.setDistinctCategoryStrategy(new DistinctCategoriesWithHierarchy());
        double deliveryCost = deliveryCostCalculator.calculateFor(cart);
        Assert.assertEquals(232.99, deliveryCost, 0.0001);
    }


}
