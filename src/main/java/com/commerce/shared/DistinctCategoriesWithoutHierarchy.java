package com.commerce.shared;

import com.commerce.category.Category;
import com.commerce.shared.DistinctCategory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DistinctCategoriesWithoutHierarchy implements DistinctCategory {

    @Override
    public List<Category> getDistinctCategories(Collection<Category> singleLevelCategories) {
        return new ArrayList<>(singleLevelCategories);
    }
}
