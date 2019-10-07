package com.mishanin.springdata.controllers;

import com.mishanin.springdata.entities.User;
import com.mishanin.springdata.services.OrderService;
import com.mishanin.springdata.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/order")
public class OdrerController {

    private UserService userService;
    private OrderService orderService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/add")
    public String addOrder(){
        //вытягиваем имя пользователя из Spring Security
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //вытягиваем пользователя из БД по имени пользователя из Spring Security
        User user = userService.findByUserName(username);
        orderService.createOrder(user);
        return "redirect:/cart/succes";
    }

    @PostMapping("/addAnonimus")
    public String addOrderAnonimus(Model model, @RequestParam(name = "phone") Long phone){
        User user = userService.findByUserName("anon");
        orderService.createOrderByAnon(user, phone);
        return "redirect:/cart/succes";
    }
}
