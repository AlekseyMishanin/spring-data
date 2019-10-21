package com.mishanin.springdata.controllers;

import com.mishanin.springdata.entities.Order;
import com.mishanin.springdata.entities.User;
import com.mishanin.springdata.services.MailService;
import com.mishanin.springdata.services.OrderService;
import com.mishanin.springdata.services.UserService;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/order")
public class OdrerController {

    private UserService userService;
    private OrderService orderService;
    private MailService mailService;
    @Value("${twilio.phone}") private String twilio_phone;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/add")
    public String addOrder(@RequestParam(name = "phone") Long phone){
        //вытягиваем имя пользователя из Spring Security
        String userphone = SecurityContextHolder.getContext().getAuthentication().getName();
        //вытягиваем пользователя из БД по имени пользователя из Spring Security
        User user = userService.findByPhone(userphone);
        Order order = orderService.createOrder(user, phone);
        executorService.execute(()->{
            Message message = Message.creator(new PhoneNumber('+' + Long.toString(phone)),
                    new PhoneNumber(twilio_phone),
                    "Your order №" + order.getId() + " has been accepted. Thanks you.")
                    .create();
        });
        mailService.sendOrderMail(order);

        return "redirect:/cart/success";
    }

    @PostMapping("/addAnonimus")
    public String addOrderAnonimus(Model model, @RequestParam(name = "phone") Long phone){
        User user = userService.findByPhone(Long.toString(phone));
        if(user == null){
            user = userService.saveAnonimus(Long.toString(phone));
        }
        Order order = orderService.createOrder(user, phone);
        executorService.execute(()->{
            Message message = Message.creator(new PhoneNumber('+' + Long.toString(phone)),
                    new PhoneNumber(twilio_phone),
                    "Your order №" + order.getId() + " has been accepted. Thanks you.")
                    .create();
        });
        return "redirect:/cart/success";
    }
}
