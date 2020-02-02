package com.commerce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private List<LineItem> items = new ArrayList<>();
    private int totalQuanity = 0;
    private double totalAmount = 0;
    private Map<DiscountType, CouponDiscountCalculator> couponDiscountCalculators;

    public ShoppingCart(){
        this.couponDiscountCalculators = new HashMap<>();
        this.couponDiscountCalculators.put(DiscountType.Amount, new CouponDiscountCalculatorByAmount());
        this.couponDiscountCalculators.put(DiscountType.Rate, new CouponDiscountCalculatorByRate());
    }

    public void addItem(Product product, int quantity) {
        LineItem lineItem = new LineItem(product, quantity);

        this.items.add(lineItem);
        this.totalQuanity += quantity;
        this.totalAmount += lineItem.getTotal();
    }

    public int getItemCount() {
        return items.size();
    }

    public void applyDiscounts(Campaign... campaigns) throws Exception {
        CampaingDiscountManager campaingDiscountManager = new CampaingDiscountManager(campaigns);
        for (LineItem item: items){
            campaingDiscountManager.addItem(item);
        }
        List<WeightedCategory> weightedCategories = campaingDiscountManager.getMaxDiscount();
        double maxDiscount = 0;
        for (WeightedCategory weightedCategory: weightedCategories){
            weightedCategory.distributeDiscountsToLineItems();
        }
    }

    public double getCampaignDiscount() {
        double result = 0;
        for (LineItem item : items){
            result += item.getCampaignDiscount();
        }
        return result;
    }

    public void applyCoupon(Coupon coupon) throws Exception {
        if( totalAmount < coupon.getMinPurchaseAmount() ){
            return;
        }

        double couponDiscount = 0;
        CouponDiscountCalculator couponDiscountCalculator = couponDiscountCalculators.get(coupon.getDiscountType());
        couponDiscount = couponDiscountCalculator.calculateDiscount(this,coupon);

        double unitDiscount = couponDiscount / totalQuanity;
        for (LineItem item: items){
            item.setCouponDiscount(unitDiscount * item.getQuantity());
        }
    }

    public double getCouponDiscount() {
        double result = 0;
        for (LineItem item : items){
            result += item.getCouponDiscount();
        }
        return result;
    }

    public List<LineItem> getItems() {
        return items;
    }

    public double getTotalAmount(){
        return totalAmount;
    }
}

interface CampaignDiscountCalculator{
    double calculateDiscount(Campaign campaign, double totalAmount);
}

class CampaignDiscountCalculatorByAmount implements CampaignDiscountCalculator{

    @Override
    public double calculateDiscount(Campaign campaign, double totalAmount) {
        return campaign.getDiscountFactor();
    }
}

class CampaignDiscountCalculatorByRate implements CampaignDiscountCalculator{

    @Override
    public double calculateDiscount(Campaign campaign, double totalAmount) {
        double campaignDiscount = campaign.getDiscountFactor() / 100 * totalAmount;
        return campaignDiscount;
    }
}

interface CouponDiscountCalculator{
    double calculateDiscount(ShoppingCart cart, Coupon coupon);
}

class CouponDiscountCalculatorByRate implements CouponDiscountCalculator{

    @Override
    public double calculateDiscount(ShoppingCart cart, Coupon coupon) {
        double totalWithCampaingDiscount = cart.getTotalAmount() - cart.getCampaignDiscount();
        double couponDiscount = coupon.getDiscountFactor() / 100 * totalWithCampaingDiscount;
        return couponDiscount;
    }
}

class CouponDiscountCalculatorByAmount implements CouponDiscountCalculator{

    @Override
    public double calculateDiscount(ShoppingCart cart, Coupon coupon) {
        return coupon.getDiscountFactor();
    }
}
