package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.Order;
import com.mishanin.springdata.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order save(Order order){
        return orderRepository.save(order);
    }
}
