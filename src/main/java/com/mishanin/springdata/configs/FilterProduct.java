package com.mishanin.springdata.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
/**
 * Класс инкапсулирует фильтр для списка объектов типа Product
 * */
public class FilterProduct {

    private int min;            //минимальное значение
    private int max ;           //максимальное значение
    private boolean isActive;   //признак активности фильтра

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
