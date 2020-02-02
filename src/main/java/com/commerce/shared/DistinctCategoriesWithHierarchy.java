package com.commerce.shared;

import com.commerce.category.Category;

import java.util.*;

class DistinctCategoriesWithHierarchy implements DistinctCategory {

    @Override
    public List<Category> getDistinctCategories(Collection<Category> singleLevelCategories) {
        Set<Category> result = new HashSet<>();
        for(Category category: singleLevelCategories){
            while (category.getParentCategory() != null){
                category = category.getParentCategory();
            }
            result.add(category);
        }
        return new ArrayList<>(result);
    }
}
