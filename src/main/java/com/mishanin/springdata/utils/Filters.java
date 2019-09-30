package com.mishanin.springdata.utils;

import com.mishanin.springdata.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;

@Component
@SessionScope
public class Filters {

    private enum FilterParam {
        MIN, MAX, WORD, PAGECURRENT, PAGESIZE, PAGE
    }

    private HashMap<FilterParam, String> filtersString = new HashMap<>();
    private HashMap<FilterParam, Object> filtersObject = new HashMap<>();

    public void setMax(String max){
        filtersString.put(FilterParam.MAX, max);
    }

    public void setMin(String min){
        filtersString.put(FilterParam.MIN, min);
    }

    public void setWord(String word){
        filtersString.put(FilterParam.WORD, word);
    }

    public void setPageCurrent(String pageCurrent){
        filtersString.put(FilterParam.PAGECURRENT, pageCurrent);
    }

    public void setSizePage(String sizePage){
        filtersString.put(FilterParam.PAGESIZE, sizePage);
    }

    public void setPageProduct(Page<Product> page){
        filtersObject.put(FilterParam.PAGE, page);
    }

    public String getMax(){
        return filtersString.get(FilterParam.MAX);
    }

    public String getMin(){
        return filtersString.get(FilterParam.MIN);
    }

    public String getWord(){
        return filtersString.get(FilterParam.WORD);
    }

    public String getPageCurrent(){
        return filtersString.get(FilterParam.PAGECURRENT);
    }

    public String getSizePage(){
        return filtersString.get(FilterParam.PAGESIZE);
    }

    public Page<Product> getPageProduct(){
        return (Page<Product>) filtersObject.get(FilterParam.PAGE);
    }
}
