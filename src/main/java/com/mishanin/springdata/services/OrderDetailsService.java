package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.OrderDetails;
import com.mishanin.springdata.repositories.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderDetailsService {

    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    public void setOrderDetailsRepository(OrderDetailsRepository orderDetailsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @Transactional
    public OrderDetails save(OrderDetails orderDetails){
        return orderDetailsRepository.save(orderDetails);
    }
}
