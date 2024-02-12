package com.d101.frientree.entity;

import com.d101.frientree.exception.leaf.CategoryNotFoundException;
import lombok.Getter;

@Getter
public enum LeafCategory {
    FREEDOM,
    ENCOURAGEMENT,
    COMFORT;

    public static String findLeafCategory(int num){
        if(num>=0 && num<=2){
            LeafCategory value = LeafCategory.values()[num];
            return value.toString();
        }
        else{
            throw new CategoryNotFoundException("Category Not Found");
        }
    }
}