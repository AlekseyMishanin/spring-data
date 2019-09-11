package com.mishanin.springdata.configs;

import com.mishanin.springdata.entities.Product;
import com.mishanin.springdata.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Transactional
public class InitializeDb {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostConstruct
    @DependsOn("productRepository")
    public void init(){
        Random rnd = new Random();
        String[] prod = {"milk", "meat", "fish", "bread", "plum", "curd", "juice", "jam", "potatoes", "beet"};
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new Product(prod[rnd.nextInt(prod.length)],rnd.nextInt(1000)+50));
        }
        productRepository.saveAll(list);
    }
}
