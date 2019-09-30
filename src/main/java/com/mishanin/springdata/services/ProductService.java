package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.Product;
import com.mishanin.springdata.errors.ProductNotFoundExceprion;
import com.mishanin.springdata.repositories.ProductRepository;
import com.mishanin.springdata.repositories.specifications.ProductSpecifications;
import com.mishanin.springdata.utils.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("productService")
public class ProductService {

    private ProductRepository productRepository;
    private Filters filters;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setFilters(Filters filters) {
        this.filters = filters;
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
    public void processing(// Model model,
                            String word,
                            Integer min,
                            Integer max,
                            Integer pageCurrent,
                            Integer sizePage){
        Specification<Product> spec = Specification.where(null);
        if(word != null) {
            filters.setWord(word);
        }

        if (filters.getWord() != null){
            spec = spec.and(ProductSpecifications.titleContains(filters.getWord()));
        }
        if(min != null) {
            filters.setMin(min.toString());
        }
        if(filters.getMin() != null){
            spec = spec.and(ProductSpecifications.priceGreaterThanOrEq(Integer.valueOf(filters.getMin())));
        }
        if(max != null) {
            filters.setMax(max.toString());
        }
        if(filters.getMax() != null){
            spec = spec.and(ProductSpecifications.priceLesserThanOrEq(Integer.valueOf(filters.getMax())));
        }
        if(filters.getPageCurrent()==null && pageCurrent == null) { pageCurrent = 1; }
        if(filters.getSizePage()==null && sizePage == null) { sizePage = 2; }

        if(pageCurrent != null) { filters.setPageCurrent(pageCurrent.toString()); }
        if(sizePage != null) { filters.setSizePage(sizePage.toString()); filters.setPageCurrent("1");}

        filters.setPageProduct(findAll(spec,PageRequest.of(
                Integer.valueOf(filters.getPageCurrent())-1,
                Integer.valueOf(filters.getSizePage()),
                Sort.by(Sort.Direction.ASC, "id"))));

    }
}
