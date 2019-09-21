package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.Product;
import com.mishanin.springdata.errors.ProductNotFoundExceprion;
import com.mishanin.springdata.repositories.ProductRepository;
import com.mishanin.springdata.repositories.specifications.ProductSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service("productService")
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Optional<List<Product>> findAll(){
        return Optional.of(productRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Page<Product> findAll(Specification<Product> spec, Pageable pageable){
        return productRepository.findAll(spec,pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id){ return productRepository.findById(id);}

    @Transactional
    public void update(Product product){ productRepository.save(product);}

    @Transactional
    public Product save(Product product){ return productRepository.save(product);}

    @Transactional
    public void deleteById(Long id){ productRepository.delete(productRepository.findById(id).orElseThrow(()-> new ProductNotFoundExceprion("Product not found (ID: " + id + ")")));}

    @Transactional(readOnly = true)
    public Model processing( Model model,
                            String word,
                            Integer min,
                            Integer max,
                            Integer pageCurrent,
                            Integer sizePage){
        Specification<Product> spec = Specification.where(null);
        if(word != null) {
            spec = spec.and(ProductSpecifications.titleContains(word));
        }
        if(min != null) {
            spec = spec.and(ProductSpecifications.priceGreaterThanOrEq(min));
        }
        if(max != null) {
            spec = spec.and(ProductSpecifications.priceLesserThanOrEq(max));
        }
        if(pageCurrent == null) { pageCurrent = 1; }
        if(sizePage == null) { sizePage = 2; }
        model.addAttribute("page", findAll(spec,PageRequest.of(pageCurrent-1,sizePage, Sort.by(Sort.Direction.ASC, "id"))));
        model.addAttribute("word",word);
        model.addAttribute("min",min);
        model.addAttribute("max",max);
        model.addAttribute("pageCurrent",pageCurrent);
        model.addAttribute("sizePage", sizePage);
        return model;
    }

/*
    @Transactional(readOnly = true)
    public Model processing(Model model,
                            String word,
                            Integer min,
                            Integer max,
                            Integer pageCurrent,
                            Integer sizePage,
                            HashMap<String,String> filters){
        if (filters == null) {filters = new HashMap<>();}
        if(word != null) filters.put("word",word);
        if(min != null) filters.put("min",Integer.toString(min));
        if(max != null) filters.put("max",Integer.toString(max));
        if(pageCurrent != null) filters.put("pageCurrent",Integer.toString(pageCurrent));
        if(sizePage != null) filters.put("sizePage",Integer.toString(sizePage));

        Specification<Product> spec = Specification.where(null);
        if(filters.get("word")!=null && !filters.get("word").isEmpty()) {
            spec = spec.and(ProductSpecifications.titleContains((String) filters.get("word")));
        }
        if(filters.get("min")!=null && !filters.get("min").isEmpty()) {
            spec = spec.and(ProductSpecifications.priceGreaterThanOrEq(Integer.parseInt(filters.get("min"))));
        }
        if(filters.get("max")!=null && !filters.get("max").isEmpty()) {
            spec = spec.and(ProductSpecifications.priceLesserThanOrEq(Integer.parseInt(filters.get("max"))));
        }
        if(filters.get("pageCurrent")==null || filters.get("pageCurrent").isEmpty()) { filters.put("pageCurrent", "1");  }
        if(filters.get("sizePage")==null || filters.get("sizePage").isEmpty()) { filters.put("sizePage", "2"); }
        System.out.println(filters);
        model.addAttribute("page", findAll(spec,PageRequest.of(
                Integer.parseInt(filters.get("pageCurrent"))-1,
                Integer.parseInt(filters.get("sizePage")),
                Sort.by(Sort.Direction.ASC, "id"))));
        model.addAttribute("filters", filters);
        return model;
    }*/


}
