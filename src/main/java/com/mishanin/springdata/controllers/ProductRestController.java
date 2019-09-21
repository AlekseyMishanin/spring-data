package com.mishanin.springdata.controllers;

import com.mishanin.springdata.entities.Product;
import com.mishanin.springdata.errors.AccessDeniedProductException;
import com.mishanin.springdata.errors.ProductNotFoundExceprion;
import com.mishanin.springdata.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProduct(){
        return productService.findAll().get();
    }

    @GetMapping(value = {"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public Product getProductById(@PathVariable Long id){
        return productService.findById(id).orElseThrow(()-> new ProductNotFoundExceprion("Product not found (ID: " + id + ")"));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody Product product){
        if(product.getId()!=null) throw new AccessDeniedProductException("It is forbidden to update records");
        return productService.save(product);
    }

    @PutMapping(value = {"/{id}", "/"})
    @ResponseStatus(HttpStatus.OK)
    public Product updateProductById(@RequestBody Product newProduct){
        return productService.findById(newProduct.getId())
                .map(updateProduct -> {
                    updateProduct.setTitle(newProduct.getTitle());
                    updateProduct.setPrice(newProduct.getPrice());
                    return productService.save(updateProduct);
                }).orElseThrow(()-> new ProductNotFoundExceprion("Product not found (ID: " + newProduct.getId() + ")"));
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id){
        productService.deleteById(id);
    }

}
