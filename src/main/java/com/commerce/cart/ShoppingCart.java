package com.commerce.cart;

import com.commerce.campaign.Campaign;
import com.commerce.campaign.CampaingDiscountManager;
import com.commerce.category.Category;
import com.commerce.category.WeightedCategory;
import com.commerce.coupon.Coupon;
import com.commerce.coupon.CouponDiscountCalculator;
import com.commerce.coupon.CouponDiscountCalculatorByAmount;
import com.commerce.coupon.CouponDiscountCalculatorByRate;
import com.commerce.delivery.DeliveryCostCalculator;
import com.commerce.shared.DiscountType;

import javax.sound.sampled.Line;
import java.io.PrintStream;
import java.util.*;

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

    public void addItem(Product product, int quantity) throws Exception {
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

        double unitDiscount = couponDiscount / totalAmount;
        for (LineItem item: items){
            item.setCouponDiscount(unitDiscount * item.getTotal());
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

    public double getDeliveryCost(){
        DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculator(100d, 10d, 2.99);
        double deliveryCost = deliveryCostCalculator.calculateFor(this);
        return deliveryCost;
    }

    public double getTotalAmountAfterDiscount(){
        double totalAmountAfterDiscount = totalAmount;
        totalAmountAfterDiscount -= getCampaignDiscount();
        totalAmountAfterDiscount -= getCouponDiscount();
        totalAmountAfterDiscount -= getDeliveryCost();
        return totalAmountAfterDiscount;
    }

    public void print(){
        printToStream(System.out);
    }

    void printToStream(PrintStream out){
        out.println("Shopping Cart");
        Map<Category, List<LineItem>> categoryListMap = new HashMap<>();
        for (LineItem item : items){
            Category category = item.getProduct().getCategory();
            List<LineItem> groupedItems = categoryListMap.get(category);
            if(groupedItems == null){
                categoryListMap.put(category, groupedItems = new ArrayList<>());
            }
            groupedItems.add(item);
        }

        out.printf("%15s | %15s | %8s | %10s | %11s | %14s\n"
                , "Category", "Product", "Quantity", "Unit Price", "Total Price", "Total Discount");
        for(List<LineItem> groupedItems: categoryListMap.values()){
            for(LineItem item:groupedItems){
                out.printf("%15s | %15s | %8d | %10.2f | %11.2f | %14.2f\n"
                        , item.getProduct().getCategory().getTitle()
                        , item.getProduct().getTitle()
                        , item.getQuantity()
                        , item.getProduct().getPrice()
                        , item.getTotal()
                        , item.getTotalDiscount()
                        // , item.getCampaignDiscount()
                        // , item.getCouponDiscount()
                        );
            }
        }
        out.printf("\n");
        out.printf("%63s Total Amount: %10.2f\n","", getTotalAmountAfterDiscount());
        out.printf("%63sDelivery Cost: %10.2f\n","", getDeliveryCost());
    }


}

