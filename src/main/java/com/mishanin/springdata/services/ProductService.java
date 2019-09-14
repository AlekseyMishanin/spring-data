package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.Product;
import com.mishanin.springdata.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("productService")
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll(){ return productRepository.findAll(); }

    @Transactional(readOnly = true)
    public Page<Product> findAll(PageRequest pageRequest){
        return productRepository.findAll(pageRequest);
    }

    @Transactional(readOnly = true)
    public long count(){
        return productRepository.count();
    }

    @Transactional(readOnly = true)
    public Product findMinCost(){ return  productRepository.findProductByCostIsMin();}

    @Transactional(readOnly = true)
    public Product findMaxCost(){ return  productRepository.findProductByCostIsMax();}

    @Transactional(readOnly = true)
    public List<Product> findBetweenCost(int min, int max){ return productRepository.findProductsByCostBetween(min, max);}

    @Transactional(readOnly = true)
    public Product findById(Long id){ return productRepository.findById(id).get();}

    @Transactional
    public void update(Product product){ productRepository.save(product);}

}
