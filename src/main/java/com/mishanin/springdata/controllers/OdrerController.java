package com.mishanin.springdata.controllers;

import com.mishanin.springdata.entities.Order;
import com.mishanin.springdata.entities.PaymentType;
import com.mishanin.springdata.entities.User;
import com.mishanin.springdata.services.MailService;
import com.mishanin.springdata.services.OrderService;
import com.mishanin.springdata.services.PaymentTypeService;
import com.mishanin.springdata.services.UserService;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.Principal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/order")
public class OdrerController {

    private UserService userService;
    private OrderService orderService;
    private PaymentTypeService paymentTypeService;
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

    @Autowired
    public void setPaymentTypeService(PaymentTypeService paymentTypeService) {
        this.paymentTypeService = paymentTypeService;
    }

    @PostMapping("/add")
    public String addOrder(@RequestParam(name = "phone") Long phone,
                           @RequestParam(name = "paymentTypeName") Long idPaymentType,
                           RedirectAttributes ra,
                           Principal principal) {
        User user;
        if(principal != null){
            //вытягиваем имя пользователя из Spring Security
            String userphone = principal.getName();
            //вытягиваем пользователя из БД по имени пользователя из Spring Security
            user = userService.findByPhone(userphone);
        } else {
            user = userService.findByPhone(Long.toString(phone));
            if(user == null){
                user = userService.saveAnonimus(Long.toString(phone));
            }
        }
        PaymentType paymentType = paymentTypeService.findById(idPaymentType);
        Order order = orderService.createOrder(user, phone, paymentType);
        executorService.execute(()->{
            Message message = Message.creator(new PhoneNumber('+' + Long.toString(phone)),
                    new PhoneNumber(twilio_phone),
                    "Your order №" + order.getId() + " has been accepted. Thanks you.")
                    .create();
        });
        if(principal != null) mailService.sendOrderMail(order);
        if(paymentType.getTitle().equals("PayPal")) {
            ra.addFlashAttribute("order", order);
            return "redirect:/paypal/buy";
        } else {
            return "redirect:/cart/success";
        }
    }
}
