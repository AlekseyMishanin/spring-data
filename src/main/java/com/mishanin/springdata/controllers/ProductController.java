package com.mishanin.springdata.controllers;

import com.mishanin.springdata.entities.Product;
import com.mishanin.springdata.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/show/{id}")
    public String editProduct(@PathVariable(value = "id") Long id, Model model, HttpSession session){
        Product product = productService.findById(id).get();
        HashMap<Long, Product> watchList= (HashMap<Long, Product>) session.getAttribute("watchList");
        if(watchList == null) {
            watchList = new HashMap<>();
        }
        watchList.put(id, product);
        session.setAttribute("watchList", watchList);
        model.addAttribute("product", product);
        return "product-single";
    }

//    @GetMapping("/edit/{id}")
//    public String editProduct(@PathVariable(value = "id") Long id, Model model){
//        Product product = productService.findById(id).get();
//        model.addAttribute("editproduct", product);
//        return "product-edit";
//    }
//
//    @GetMapping("/add")
//    public String addProduct(Model model){
//        Product product = new Product();
//        model.addAttribute("editproduct", product);
//        return "product-edit";
//    }
//
//    @PostMapping("/update")
//    public String returnProducts(@RequestParam(value = "cancel-button", required = false) String clBtn,
//                                 @RequestParam(value = "ok-button", required = false) String okBtn,
//                                 @ModelAttribute(value = "editproduct") Product product){
//        System.out.println(product.toString());
//        if(clBtn!=null){;}
//        if(okBtn!=null){ productService.update(product); }
//        return "redirect:/products/";
//    }
}
