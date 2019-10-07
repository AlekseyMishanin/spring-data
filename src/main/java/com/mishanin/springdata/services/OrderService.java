package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.Order;
import com.mishanin.springdata.entities.OrderDetails;
import com.mishanin.springdata.entities.User;
import com.mishanin.springdata.repositories.OrderRepository;
import com.mishanin.springdata.utils.Cart;
import com.mishanin.springdata.utils.GroupOrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private Cart cart;
    private ProductService productService;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Transactional
    public void createOrder(User user){
        //формируем уникальный заказ
        Order newOrder = new Order(user);
        createOrderEnd(newOrder);
    }

    @Transactional
    public void createOrderByAnon(User user, Long phone){
        //формируем уникальный заказ
        Order newOrder = new Order(user, phone);
        createOrderEnd(newOrder);
    }

    private void createOrderEnd(Order order){
        //вытягиваем коллекцию товаров из корзины клиента
        GroupOrderDetails groupOrderDetails = cart.getProducts();
        //в цикле перебираем все группы товаров из коллекции
        if(groupOrderDetails.getOrderDetails().isEmpty()) return;
        //добавляем группы товаров в заказ
        groupOrderDetails.getOrderDetails().stream().forEach(a->{
            order.addItem(a.getValue());
            //обновляем сущность продукта
            a.getValue().setProduct(productService.save(a.getValue().getProduct()));
        });
        orderRepository.save(order);
        cart.clear();
    }
}
