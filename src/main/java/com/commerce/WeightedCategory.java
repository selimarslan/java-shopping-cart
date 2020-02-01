package com.commerce;

import java.util.ArrayList;
import java.util.List;

public class WeightedCategory {
    private final Category category;
    private List<Campaign> campaigns;
    private List<LineItem> items;
    private Campaign maxCampaign;
    private double maxDiscount;

    WeightedCategory(Category category){
        this.category = category;
        campaigns = new ArrayList<>();
        items = new ArrayList<>();
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

        int totalQuantity = 0;
        double sumOfSubTotal = 0;
        for (LineItem item : items){
            // if(item.getProduct().getCategory() == campaign.getCategory() && item.getQuantity() >= campaign.getLowerLimit()){
            // double campaignDiscount = campaign.getDiscountFactor() / 100 * item.getSubTotal();
            totalQuantity += item.getQuantity();
            sumOfSubTotal += item.getSubTotal();
        }

        for (Campaign campaign: campaigns){
            double campaignDiscount = 0;
            if(totalQuantity >= campaign.getLowerLimit()){
                if(campaign.getDiscountType() == DiscountType.Rate){
                    campaignDiscount = campaign.getDiscountFactor() / 100 * sumOfSubTotal;
                }
                else if(campaign.getDiscountType() == DiscountType.Amount){
                    campaignDiscount = campaign.getDiscountFactor();
                }

                if(campaignDiscount > maxDiscount){
                    this.maxCampaign = campaign;
                    this.maxDiscount = campaignDiscount;
                }
            }
        }
    }
}
