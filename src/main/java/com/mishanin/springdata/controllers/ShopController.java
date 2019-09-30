package com.mishanin.springdata.controllers;

import com.mishanin.springdata.services.ProductService;
import com.mishanin.springdata.utils.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ProductService productService;

    @Autowired
    private Filters filters;

    @GetMapping("")
    public String getProducts(Model model,
                              @RequestParam(name = "word", required = false) String word,
                              @RequestParam(name = "min", required = false) Integer min,
                              @RequestParam(name = "max", required = false) Integer max,
                              @RequestParam(name = "pageCurrent", required = false) Integer pageCurrent,
                              @RequestParam(name = "sizePage", required = false) Integer sizePage
    ){


        productService.processing(
                word,
                min,
                max,
                pageCurrent,
                sizePage);
        model.addAttribute("filters", filters);
        return "shop";
    }
}
