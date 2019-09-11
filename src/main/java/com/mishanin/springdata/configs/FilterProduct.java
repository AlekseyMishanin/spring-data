package com.mishanin.springdata.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class FilterProduct {

    private int min;
    private int max ;
    private boolean isActive;

    public FilterProduct(){
        min = 0;
        max = Integer.MAX_VALUE;
        isActive = false;
    }

    public void clear(){
        min = 0;
        max = Integer.MAX_VALUE;
        isActive = false;
    }
}
