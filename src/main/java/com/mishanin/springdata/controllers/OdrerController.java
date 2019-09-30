package com.mishanin.springdata.controllers;

import com.mishanin.springdata.entities.Order;
import com.mishanin.springdata.entities.OrderDetails;
import com.mishanin.springdata.entities.Product;
import com.mishanin.springdata.entities.User;
import com.mishanin.springdata.services.OrderDetailsService;
import com.mishanin.springdata.services.OrderService;
import com.mishanin.springdata.services.ProductService;
import com.mishanin.springdata.services.UserService;
import com.mishanin.springdata.utils.Cart;
import com.mishanin.springdata.utils.GroupProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/order")
public class OdrerController {

    private UserService userService;
    private Cart cart;
    private OrderService orderService;
    private OrderDetailsService orderDetailsService;
    private ProductService productService;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setOrderDetailsService(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/add")
    public String addOrder(){

        //вытягиваем имя пользователя из Spring Security
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //вытягиваем пользователя из БД по имени пользователя из Spring Security
        User user = userService.findByUserName(username);

        //формируем уникальный заказ
        Order newOrder = new Order();
        //привязываем к заказу id клиента
        newOrder.setUser(user);
        //присваиваем статус "в работе"
        newOrder.setStatus(false);
        //записываем заказ в БД и получаем id заказа
        newOrder = orderService.save(newOrder);

        //вытягиваем коллекцию товаров из корзины клиента
        GroupProduct groupProduct = cart.getProducts();
        //в цикле перебираем все группы товаров из коллекции
        for (Map.Entry<Product,Integer> prop:
             groupProduct.getProducts()) {
            //создаем объект детализированного заказа
            OrderDetails orderDetails = new OrderDetails();
            //присваиваем кол-во штук продукта из выбранной категории
            orderDetails.setCount(prop.getValue());
            //присваиваем общую стоимость группы товаров = кол-во товара * цена(1шт)
            orderDetails.setTotalCost(prop.getValue() * prop.getKey().getPrice());
            //присваиваем id выбранного товара (предварительно через ProductService меняем состояние сущности с detached на persistent
            orderDetails.setProduct(productService.save(prop.getKey()));
            //присваиваем id заказа
            orderDetails.setOrder(newOrder);
            //записываем детализированный заказ в БД
            orderDetailsService.save(orderDetails);
        }
        cart.clear();

        return "redirect:/cart";
    }
}
