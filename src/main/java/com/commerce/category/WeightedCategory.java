package com.commerce;

import java.util.*;

public class WeightedCategory implements Comparable<WeightedCategory>{
    private final Category category;
    private List<Campaign> campaigns;
    private List<LineItem> items;
    private Campaign maxCampaign;
    private double maxDiscount;
    private WeightedCategory parent;
    private int totalQuantity = 0;
    private double sumOfSubTotal = 0;
    private Map<DiscountType, CampaignDiscountCalculator> discountCalculators;

    WeightedCategory(Category category){
        this.category = category;
        campaigns = new ArrayList<>();
        items = new ArrayList<>();

        discountCalculators = new HashMap<>();
        discountCalculators.put(DiscountType.Amount, new CampaignDiscountCalculatorByAmount());
        discountCalculators.put(DiscountType.Rate, new CampaignDiscountCalculatorByRate());
    }

    public void addCampaign(Campaign campaign) {
        this.campaigns.add(campaign);
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    public Campaign getMaxCampaign() {
        return maxCampaign;
    }

    public void addLineItem(LineItem lineItem) {
        this.items.add(lineItem);
        totalQuantity += lineItem.getQuantity();
        sumOfSubTotal += lineItem.getSubTotal();

        for (Campaign campaign: campaigns){
            double campaignDiscount = 0;
            if(totalQuantity >= campaign.getLowerLimit()){
                CampaignDiscountCalculator discountCalculator = discountCalculators.get(campaign.getDiscountType());
                campaignDiscount = discountCalculator.calculateDiscount(campaign, sumOfSubTotal);

                if(campaignDiscount > maxDiscount){
                    this.maxCampaign = campaign;
                    this.maxDiscount = campaignDiscount;
                }
            }
        }
    }


    @Override
    public int compareTo(WeightedCategory o) {
        int result = Double.compare(this.getMaxDiscount(), o.getMaxDiscount());
        return result * -1;
    }

    @Override
    public String toString() {
        return "WeightedCategory{" +
                "category=" + category.title +
                ", maxDiscount=" + maxDiscount +
                '}';
    }

    public Category getCategory() {
        return category;
    }

    public WeightedCategory getParentWeightedCategory() {
        return parent;
    }

    public void setParentWeightedCategory(WeightedCategory parentWeightedCategory) {
        this.parent = parentWeightedCategory;
    }

    public void distributeDiscountsToLineItems() throws Exception {
        double unitDiscount = maxDiscount / totalQuantity;
        for (LineItem item: items){
            item.setCampaignDiscount(maxCampaign, unitDiscount * item.getQuantity());
        }
    }
}
