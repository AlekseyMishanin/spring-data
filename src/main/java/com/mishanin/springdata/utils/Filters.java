package com.mishanin.springdata.utils;

import com.mishanin.springdata.entities.Product;
import com.mishanin.springdata.entities.ProductGroup;
import com.mishanin.springdata.services.ProductGroupService;
import com.mishanin.springdata.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.List;

@Component
@SessionScope
public class Filters {

    private ProductGroupService productGroupService;

    private enum FilterParam {
        MIN, MAX, WORD, PAGECURRENT, PAGESIZE, PAGE, PRODUCTGROUPID
    }

    private HashMap<FilterParam, String> filtersString = new HashMap<>();
    private HashMap<FilterParam, Object> filtersObject = new HashMap<>();

    @Autowired
    public void setProductService(ProductGroupService productGroupService) {
        this.productGroupService = productGroupService;
    }

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

    public void  setProductGroupId(String productGroupId) {filtersString.put(FilterParam.PRODUCTGROUPID, productGroupId);}

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

    public List<ProductGroup> getProductGroup(){return productGroupService.fingAll();}

    public ProductGroup getProductGroupById(){
        if(filtersString.get(FilterParam.PRODUCTGROUPID) != null &&
                !filtersString.get(FilterParam.PRODUCTGROUPID).equals("0")) {
            return productGroupService.findById(filtersString.get(FilterParam.PRODUCTGROUPID));
        }
        return null;
    }
}
