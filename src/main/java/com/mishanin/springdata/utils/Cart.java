package com.mishanin.springdata.utils;

import com.mishanin.springdata.entities.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;

@Component
@SessionScope
public class Cart {

    private GroupProduct pr;

    public GroupProduct getProducts() {
        return pr;
    }

    @PostConstruct
    public void init(){
        pr = new GroupProduct();
    }

    public void addProduct(Product product){
        pr.addProduct(product);
    }

    public void removeProduct(Product product){
        pr.removeProduct(product);
    }

    public void clear(){
        pr.clear();
    }
}
