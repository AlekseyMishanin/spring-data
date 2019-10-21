package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.Order;
import com.mishanin.springdata.entities.OrderDetails;
import com.mishanin.springdata.entities.PaymentType;
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
    public Order createOrder(User user, Long phone, PaymentType paymentType){
        //формируем уникальный заказ
        Order newOrder = new Order(user, phone, paymentType);
        return createOrderEnd(newOrder);
    }

//    @Transactional
//    public Order createOrderByAnon(User user, Long phone){
//        //формируем уникальный заказ
//        Order newOrder = new Order(user, phone);
//        return createOrderEnd(newOrder);
//    }

    private synchronized Order createOrderEnd(Order order){
        //вытягиваем коллекцию товаров из корзины клиента
        GroupOrderDetails groupOrderDetails = cart.getProducts();
        //в цикле перебираем все группы товаров из коллекции
        if(groupOrderDetails.getOrderDetails().isEmpty()) return null;
        //добавляем группы товаров в заказ
        groupOrderDetails.getOrderDetails().stream().forEach(a->{
            order.addItem(a.getValue());
            //обновляем сущность продукта
            a.getValue().setProduct(productService.save(a.getValue().getProduct()));
        });
        cart.clear();
        return orderRepository.save(order);
    }

    public void save(Order order){ orderRepository.save(order);}

    public Order findById(Long id){return orderRepository.findById(id).get();}

    public void setStatusPaidById(String id){
        Order order = findById(Long.valueOf(id));
        order.setStatus(Order.Status.PAID);
        save(order);
    }
}
