package com.mishanin.springdata;

import com.mishanin.springdata.entities.Order;
import com.mishanin.springdata.repositories.OrderRepository;
import com.mishanin.springdata.services.OrderService;
import com.mishanin.springdata.services.ProductService;
import com.mishanin.springdata.utils.Cart;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestOrderService {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private Cart cart;

    @MockBean
    private ProductService productService;

    @Test
    public void testSaveOrder(){

        Order order = new Order();

        Mockito.when(orderRepository.save(order)).thenReturn(order);

        orderService.save(order);

        Mockito.verify(orderRepository,Mockito.times(1)).save(Mockito.any(Order.class));
    }

    @Test
    public void testFindById(){

        Order order = new Order();
        order.setId(1l);

        Mockito.when(orderRepository.findById(1l)).thenReturn(Optional.of(order));

        Order result = orderService.findById(1l);

        Assert.assertEquals(result.getId(), order.getId());
        Mockito.verify(orderRepository,Mockito.times(1)).findById(1l);
    }

    @Test
    public void testSetStatusPaidById(){

        Order order = new Order();
        order.setId(1l);

        Mockito.when(orderRepository.save(order)).thenReturn(order);
        Mockito.when(orderRepository.findById(1l)).thenReturn(Optional.of(order));

        orderService.setStatusPaidById("1");

        Assert.assertEquals(Order.Status.PAID, order.getStatus());
        Mockito.verify(orderRepository,Mockito.times(1)).findById(1l);
        Mockito.verify(orderRepository,Mockito.times(1)).save(Mockito.any(Order.class));
    }
}
