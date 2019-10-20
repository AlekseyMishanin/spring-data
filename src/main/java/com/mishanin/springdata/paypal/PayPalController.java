package com.mishanin.springdata.paypal;

import com.mishanin.springdata.entities.Order;
import com.mishanin.springdata.services.OrderService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/paypal")
@PropertySource("classpath:private.properties")
public class PayPalController {

    @Value("${paypal.clientid}") private String clientId;
    @Value("${paypal.clientsecret}") private String clientSecret;
    private String mode = "sandbox";

    private APIContext apiContext;

    @PostConstruct
    public void init(){
        apiContext = new APIContext(clientId,clientSecret,mode);
    }

    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/buy")
    public String buy(@ModelAttribute("order") Order order,
                      HttpSession session,
                      Model model){
        try {

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");
            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl("http://localhost:8189/market/paypal/cancel");
            redirectUrls.setReturnUrl("http://localhost:8189/market/paypal/success");

            Amount amount = new Amount();
            amount.setCurrency("RUB");
            amount.setTotal(Double.toString(order.getTotalPrice()));

            session.setAttribute("idOrder",order.getId().toString());

            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDescription("Покупка в Market. Заказ №" +
                    order.getId() + ": " +
                    order.itemsNameConvertListToString());

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            Payment payment = new Payment();
            payment.setPayer(payer);
            payment.setRedirectUrls(redirectUrls);
            payment.setTransactions(transactions);
            payment.setIntent("sale");

            Payment doPayment = payment.create(apiContext);

            Iterator<Links> linksIterator = doPayment.getLinks().iterator();

            while (linksIterator.hasNext()){
                Links link = linksIterator.next();
                if(link.getRel().equalsIgnoreCase("approval_url")){
                    return "redirect:" + link.getHref();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("message", "Вы сюда не должны были попасть ...");
        return  "paypal-result";
    }

    @GetMapping("/success")
    public String success(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session,
                          Model model){
        try{
            String paymentId = request.getParameter("paymentId");
            String payerId = request.getParameter("PayerID");

            if(paymentId == null || paymentId.isEmpty() ||
            payerId == null || payerId.isEmpty()){
                return "redirect:/";
            }

            Payment payment = new Payment();
            payment.setId(paymentId);

            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);

            Payment executePayment = payment.execute(apiContext, paymentExecution);

            if(executePayment.getState().equals("approved")){
                String idOrder = (String) session.getAttribute("idOrder");
                Order order = orderService.findById(Long.valueOf(idOrder));
                order.setStatus(Order.Status.PAID);
                orderService.save(order);
                model.addAttribute("message", "Ваш заказ сформирован");
            } else {
                model.addAttribute("message", "Что-то пошло не так при формировании заказа, повторите");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "paypal-result";
    }

    @GetMapping("/cancel")
    public String cancel(Model model){
        model.addAttribute("message", "Оплата заказа была отменена");
        return "paypal-result";
    }
}
