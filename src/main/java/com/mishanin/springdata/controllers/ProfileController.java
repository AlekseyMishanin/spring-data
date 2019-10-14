package com.mishanin.springdata.controllers;

import com.mishanin.springdata.entities.OrderDetails;
import com.mishanin.springdata.entities.User;
import com.mishanin.springdata.services.OrderDetailsService;
import com.mishanin.springdata.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private UserService userService;
    private OrderDetailsService orderDetailsService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderDetailsService(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    @GetMapping(value = {"", "/"})
    public String info(Principal principal, Model model, HttpSession session){
        if (principal != null) {
            String phone = principal.getName();
            User user = userService.findByPhone(phone);
            session.setAttribute("firstName", user.getFirstname());
            session.setAttribute("phone", user.getPhone());
            model.addAttribute("orders", user.getOrders());
        }
        return "private";
    }

    @GetMapping("/details/{id}")
    public String getOrderDetails(Model model, HttpSession session, @PathVariable Long id){
        List<OrderDetails> orderDetails = orderDetailsService.findOrderDetailsById(id);
        model.addAttribute("orderDetails", orderDetails);
        return "history_order";
    }
}
