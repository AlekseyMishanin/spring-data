package com.mishanin.springdata.controllers;

import com.mishanin.springdata.entities.Product;
import com.mishanin.springdata.services.ProductService;
import com.mishanin.springdata.utils.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/cart")
public class CartController {

    private ProductService productService;
    private Cart cart;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @GetMapping("")
    public String show(Model model){
        model.addAttribute("products", cart.getProducts());
        return "cart";
    }

    @GetMapping("/succes")
    public String showSucces(Model model){
        model.addAttribute("products", cart.getProducts());
        model.addAttribute("cartSuccesOrder", "true");
        return "cart";
    }

    @GetMapping("/add")
    public void addProduct(@RequestParam("id") Long id,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        Product product = productService.findById(id).get();
        cart.addProduct(product);
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("/delete")
    public void removeProduct(@RequestParam("id") Long id,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        Product product = productService.findById(id).get();
        cart.removeProduct(product);
        response.sendRedirect(request.getHeader("referer"));
    }
}
