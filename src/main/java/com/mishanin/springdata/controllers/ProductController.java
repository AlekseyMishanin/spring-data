package com.mishanin.springdata.controllers;

import com.mishanin.springdata.entities.Product;
import com.mishanin.springdata.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

//    @GetMapping("")
//    public String getProducts(Model model,
//                              @RequestParam(name = "word", required = false) String word,
//                              @RequestParam(name = "min", required = false) Integer min,
//                              @RequestParam(name = "max", required = false) Integer max,
//                              @RequestParam(name = "pageCurrent", required = false) Integer pageCurrent,
//                              @RequestParam(name = "sizePage", required = false) Integer sizePage
//                             // @RequestParam(name = "filters", required = false) HashMap<String,String> filters
//    ){
//
//
//        productService.processing(model,
//                word,
//                min,
//                max,
//                pageCurrent,
//                sizePage);
//        /*
//        productService.processing(model,
//                word,
//                min,
//                max,
//                pageCurrent,
//                sizePage,filters);*/
//        return "product-all-list.html";
//    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable(value = "id") Long id, Model model){
        Product product = productService.findById(id).get();
        model.addAttribute("editproduct", product);
        return "product-edit";
    }

    @GetMapping("/add")
    public String addProduct(Model model){
        Product product = new Product();
        model.addAttribute("editproduct", product);
        return "product-edit";
    }

    @PostMapping("/update")
    public String returnProducts(@RequestParam(value = "cancel-button", required = false) String clBtn,
                                 @RequestParam(value = "ok-button", required = false) String okBtn,
                                 @ModelAttribute(value = "editproduct") Product product){
        System.out.println(product.toString());
        if(clBtn!=null){;}
        if(okBtn!=null){ productService.update(product); }
        return "redirect:/products/";
    }
}
