package com.commerce;

import sun.awt.HKSCS;

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
        DiscountHeap discountHeap = null;
        WeightedCategory weightedCategory = null;
        if(!weightedCategoryMap.containsKey(item.getProduct().getCategory())){
            WeightedCategoryDiscountHeap weightedCategoryDiscountHeap = new WeightedCategoryDiscountHeap();
            discountHeap = new DiscountHeap();
            weightedCategoryDiscountHeap.discountHeap = discountHeap;

            weightedCategory = new WeightedCategory(item.getProduct().getCategory());
            List<Campaign> campaigns = campaignMap.get(item.getProduct().getCategory());
            if(campaigns != null){
                for (Campaign campaign : campaigns){
                    weightedCategory.addCampaign(campaign);
                }
            }

            discountHeap.addWeightedCategory(weightedCategory);
            //weightedCategory.addLineItem(item);
            weightedCategoryDiscountHeap.weightedCategory = weightedCategory;

            weightedCategoryMap.put(item.getProduct().getCategory(), weightedCategoryDiscountHeap);
        }else{
            WeightedCategoryDiscountHeap weightedCategoryDiscountHeap = weightedCategoryMap.get(item.getProduct().getCategory());
            discountHeap = weightedCategoryDiscountHeap.discountHeap;
            weightedCategory = weightedCategoryDiscountHeap.weightedCategory;
        }

        weightedCategory.addLineItem(item);
        discountHeap.removeWeightedCategory(weightedCategory);
        discountHeap.addWeightedCategory(weightedCategory);

        while (weightedCategory.getCategory().parentCategory != null){
            WeightedCategory parentWeightedCategory = weightedCategory.getParentWeightedCategory();
            if(parentWeightedCategory == null){

                Category parentCategory = weightedCategory.getCategory().parentCategory;
                WeightedCategoryDiscountHeap h = weightedCategoryMap.get(parentCategory);
                if(h==null){
                    parentWeightedCategory = new WeightedCategory(parentCategory);

                    List<Campaign> campaigns = campaignMap.get(parentCategory);
                    if(campaigns!=null){
                        for (Campaign campaign : campaigns){
                            parentWeightedCategory.addCampaign(campaign);
                        }
                    }

                    weightedCategory.setParentWeightedCategory(parentWeightedCategory);
                    discountHeap.addWeightedCategory(parentWeightedCategory);

                    h=new WeightedCategoryDiscountHeap();
                    h.discountHeap = discountHeap;
                    h.weightedCategory = parentWeightedCategory;
                    weightedCategoryMap.put(parentCategory, h);
                }
                else{
                    h.discountHeap.mergeDiscountHeap(discountHeap);
                    discountHeap = h.discountHeap;
                    parentWeightedCategory = h.weightedCategory;
                }

            }

            weightedCategory = parentWeightedCategory;

            weightedCategory.addLineItem(item);
            discountHeap.removeWeightedCategory(weightedCategory);
            discountHeap.addWeightedCategory(weightedCategory);
        }

        if(!discountHeapList.contains(discountHeap)){
            discountHeapList.add(discountHeap);
        }
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
