package com.mishanin.springdata.controllers;

import com.mishanin.springdata.services.ProductService;
import com.mishanin.springdata.utils.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ProductService productService;

    @Autowired
    private Filters filters;

    @GetMapping("")
    public String getProducts(Model model,
                              HttpServletResponse response,
                              @CookieValue(value = "page_size", required = false) Integer sp,
                              @RequestParam(name = "word", required = false) String word,
                              @RequestParam(name = "min", required = false) Integer min,
                              @RequestParam(name = "max", required = false) Integer max,
                              @RequestParam(name = "pageCurrent", required = false) Integer pageCurrent,
                              @RequestParam(name = "sizePage", required = false) Integer sizePage,
                              @RequestParam(name = "product_group", required = false) String idProductGroup
    ){

        if(sp == null){
            sp = 10;
            response.addCookie(new Cookie("page_size", String.valueOf(sp)));
        }

        productService.processing(
                word,
                min,
                max,
                pageCurrent,
                sizePage,
                idProductGroup);
        model.addAttribute("filters", filters);
        return "shop";
    }
}
