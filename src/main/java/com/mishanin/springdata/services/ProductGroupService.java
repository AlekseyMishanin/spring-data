package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.ProductGroup;
import com.mishanin.springdata.repositories.ProductGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductGroupService {

    private ProductGroupRepository productGroupRepository;

    @Autowired
    public void setProductGroup(ProductGroupRepository productGroupRepository) {
        this.productGroupRepository = productGroupRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductGroup> fingAll() {return productGroupRepository.findAll();}

    @Transactional
    public Long findIdByName(String name) {return productGroupRepository.findProductGroupByTitle(name).getId();}

    @Transactional
    public ProductGroup findById(String id) {return productGroupRepository.findById(Long.valueOf(id)).get();}

}
