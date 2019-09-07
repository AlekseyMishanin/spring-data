package com.mishanin.springdata.controllers;

import com.google.common.collect.Lists;
import com.mishanin.springdata.entities.Product;
import com.mishanin.springdata.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

@Controller
public class ProductController {

    private ProductService productService;
    private static final int STEP_PAGE = 5;
    private Integer pageCurrent = 0;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String getProducts(Model model){
        Page<Product> page = productService.findAll(PageRequest.of(pageCurrent, STEP_PAGE, Sort.by(Sort.Direction.ASC, "id")));
        //List<Product> productList = productService.findAll();
        model.addAttribute("products", Lists.newArrayList(page.iterator()));
        return "product-all-list.html";
    }

    @GetMapping("/filter")
    public String getFilteredProduct(Model model, HttpServletRequest request){
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()){
            switch (enumeration.nextElement()){
                case "min-button":{
                    Product product = productService.findMinCost();
                    model.addAttribute("type", "min".toString());
                    model.addAttribute("minproduct", product);
                    break;
                }
                case "max-button":{
                    Product product = productService.findMaxCost();
                    model.addAttribute("type", "max".toString());
                    model.addAttribute("maxproduct", product);
                    break;
                }
                case "between-button":{
                    List<Product> product = productService.findBetweenCost(
                            Integer.parseInt(request.getParameter("min-value")),
                            Integer.parseInt(request.getParameter("max-value"))
                    );
                    model.addAttribute("type", "between".toString());
                    model.addAttribute("betweenproduct", product);
                    break;
                }
                case "previous-button":{
                    System.out.println(pageCurrent - STEP_PAGE);
                    Page<Product> page = productService.findAll(
                            PageRequest.of(pageCurrent = (pageCurrent - STEP_PAGE)>=0 ? pageCurrent : 0,
                                    STEP_PAGE,
                                    Sort.by(Sort.Direction.ASC, "id")));
                    model.addAttribute("type", "page".toString());
                    model.addAttribute("pageproduct", Lists.newArrayList(page.iterator()));
                    break;
                }
                case "next-button":{
                    System.out.println(pageCurrent + STEP_PAGE);
                    int start = pageCurrent + STEP_PAGE;
                    boolean bool = ((int)productService.count() > start);
                    Page<Product> page = productService.findAll(
                            PageRequest.of(pageCurrent = bool ? start : pageCurrent,
                                    STEP_PAGE,
                                    Sort.by(Sort.Direction.ASC, "id")));
                    model.addAttribute("type", "page".toString());
                    model.addAttribute("pageproduct", Lists.newArrayList(page.iterator()));
                    break;
                }
                default:break;
            }
        }
        return "product-filter-list.html";
    }
}
