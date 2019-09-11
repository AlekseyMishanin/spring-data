package com.mishanin.springdata.controllers;

import com.google.common.collect.Lists;
import com.mishanin.springdata.configs.FilterProduct;
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
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;      //ссылка на объект сервиса
    private FilterProduct filterProduct;        //ссылка на объект фильтра для product
    private static final int STEP_PAGE = 5;     //кол-во строк на одной странице
    private Integer pageCurrent = 0;            //номер страницы


    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setFilterProduct(FilterProduct filterProduct) {this.filterProduct = filterProduct;}

    @GetMapping("/")
    public String getProducts(Model model){
        List<Product> list;
        if(filterProduct.isActive()){
            list = productService.findBetweenCost(filterProduct.getMin(), filterProduct.getMax());
            filterProduct.setActive(false);
        } else {
            Page<Product> page = productService.findAll(PageRequest.of(pageCurrent, STEP_PAGE, Sort.by(Sort.Direction.ASC, "id")));
            list = Lists.newArrayList(page.iterator());
        }
        model.addAttribute("products", list);
        model.addAttribute("today", Calendar.getInstance());
        return "product-all-list.html";
    }

    @GetMapping("/filter")
    public String getFilteredProduct(HttpServletRequest request){
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()){
            switch (enumeration.nextElement()){
                case "between-button":{

                    //настраиваем нижнюю границу для фильтра
                    if(request.getParameter("min-value").isEmpty()) {
                        filterProduct.setMin(0);
                    } else {
                        filterProduct.setMin(Integer.parseInt(request.getParameter("min-value")));
                    }

                    //настраиваем верхнюю границу для фильтра
                    if(request.getParameter("max-value").isEmpty()) {
                        filterProduct.setMax(Integer.MAX_VALUE);
                    } else {
                        filterProduct.setMax(Integer.parseInt(request.getParameter("max-value")));
                    }

                    //переводим фильт в активное состояние
                    filterProduct.setActive(true);
                    break;
                }
                case "previous-button":{
                    int start = pageCurrent - 1;
                    pageCurrent = start >= 0 ? start : 0;
                    break;
                }
                case "next-button":{

                    int start = pageCurrent + 1;
                    boolean bool = ((int)productService.count()/STEP_PAGE > start);
                    pageCurrent = bool ? start : pageCurrent;
                    break;
                }
                default:break;
            }
        }
        return "redirect:/products/";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable(value = "id") Long id, Model model){
        Product product = productService.findById(id);
        model.addAttribute("editproduct", product);
        return "product-edit";
    }

    @PostMapping("/update")
    public String returnProducts(@RequestParam(value = "cancel-button", required = false) String clBtn,
                                 @RequestParam(value = "ok-button", required = false) String okBtn,
                                 @ModelAttribute(value = "editproduct") Product product){
        if(clBtn!=null){;}
        if(okBtn!=null){
            productService.update(product);
        }
        return "redirect:/products/";
    }
}
