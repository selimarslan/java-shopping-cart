package com.commerce.campaign;

import com.commerce.cart.LineItem;
import com.commerce.category.Category;
import com.commerce.category.WeightedCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CampaingDiscountManager {
    private HashMap<Category, List<Campaign>> campaignMap;
    private HashMap<Category, WeightedCategoryDiscountHeap> weightedCategoryMap;
    private List<DiscountHeap> discountHeapList;

    public CampaingDiscountManager(Campaign[] campaigns) {
        discountHeapList = new ArrayList<>();
        weightedCategoryMap = new HashMap<>();
        campaignMap = new HashMap<>();
        for (Campaign campaign: campaigns){
            if(!campaignMap.containsKey(campaign.getCategory())){
                List<Campaign> campaignsList = new ArrayList<>();
                campaignsList.add(campaign);
                campaignMap.put(campaign.getCategory(), campaignsList);
            }else{
                campaignMap.get(campaign.getCategory()).add(campaign);
            }
        }
    }

    public void addItem(LineItem item) {
        WeightedCategoryDiscountHeap weightedCategoryDiscountHeap = null;
        if(!weightedCategoryMap.containsKey(item.getProduct().getCategory())){
            weightedCategoryDiscountHeap = createWeightedCategoryDiscountHeap(item.getProduct().getCategory(), new DiscountHeap());
        }else{
            weightedCategoryDiscountHeap = weightedCategoryMap.get(item.getProduct().getCategory());
        }

        DiscountHeap discountHeap = weightedCategoryDiscountHeap.discountHeap;
        WeightedCategory weightedCategory = weightedCategoryDiscountHeap.weightedCategory;
        addLineItemToWeightedCategory(discountHeap, weightedCategory, item);

        while (weightedCategory.getCategory().getParentCategory() != null){
            WeightedCategory parentWeightedCategory = weightedCategory.getParentWeightedCategory();
            if(parentWeightedCategory == null){

                Category parentCategory = weightedCategory.getCategory().getParentCategory();
                weightedCategoryDiscountHeap = weightedCategoryMap.get(parentCategory);
                if(weightedCategoryDiscountHeap==null){
                    weightedCategoryDiscountHeap = createWeightedCategoryDiscountHeap(parentCategory, discountHeap);
                    parentWeightedCategory = weightedCategoryDiscountHeap.weightedCategory;
                    weightedCategory.setParentWeightedCategory(parentWeightedCategory);
                }
                else{
                    weightedCategoryDiscountHeap.discountHeap.mergeDiscountHeap(discountHeap);
                    discountHeap = weightedCategoryDiscountHeap.discountHeap;
                    parentWeightedCategory = weightedCategoryDiscountHeap.weightedCategory;
                }
            }
            weightedCategory = parentWeightedCategory;
            addLineItemToWeightedCategory(discountHeap, weightedCategory, item);
        }

        if(!discountHeapList.contains(discountHeap)){
            discountHeapList.add(discountHeap);
        }
    }

    private void addLineItemToWeightedCategory(DiscountHeap discountHeap, WeightedCategory weightedCategory, LineItem item){
        weightedCategory.addLineItem(item);
        discountHeap.removeWeightedCategory(weightedCategory);
        discountHeap.addWeightedCategory(weightedCategory);
    }

    private WeightedCategoryDiscountHeap createWeightedCategoryDiscountHeap(Category category, DiscountHeap discountHeap){
        WeightedCategoryDiscountHeap weightedCategoryDiscountHeap = new WeightedCategoryDiscountHeap();
        weightedCategoryDiscountHeap.discountHeap = discountHeap;

        WeightedCategory weightedCategory = new WeightedCategory(category);
        List<Campaign> campaigns = campaignMap.get(category);
        if(campaigns != null){
            for (Campaign campaign : campaigns){
                weightedCategory.addCampaign(campaign);
            }
        }

        discountHeap.addWeightedCategory(weightedCategory);
        weightedCategoryDiscountHeap.weightedCategory = weightedCategory;

        weightedCategoryMap.put(category, weightedCategoryDiscountHeap);
        return weightedCategoryDiscountHeap;
    }



    public List<WeightedCategory> getMaxDiscount() {
        List<WeightedCategory> result = new ArrayList<>();
        for(DiscountHeap discountHeap: discountHeapList){
            result.add(discountHeap.getMaxDiscount());
        }
        return result;
    }

    class WeightedCategoryDiscountHeap{
        public WeightedCategory weightedCategory;
        public DiscountHeap discountHeap;
    }
}
