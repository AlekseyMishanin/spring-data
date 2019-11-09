package com.mishanin.springdata;

import com.mishanin.springdata.entities.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestEntityOrder {

    @Test
    public void testConstructors(){

        Order order1 = new Order();
        assertNotNull(order1);
        assertNull(order1.getCraetedAt());
        assertNull(order1.getOrderDetails());
        assertNull(order1.getPaymentType());
        assertNull(order1.getUpdatedAt());
        assertNull(order1.getUser());
        assertNull(order1.getId());
        assertNull(order1.getPhone());
        assertNull(order1.getStatus());
        try {
            assertNull(order1.getTotalPrice());
            fail();
        } catch (Exception e){
            assertTrue(true);
        }

        User user = Mockito.mock(User.class);

        Order order2 = new Order(user);
        assertNotNull(order2);
        assertNull(order2.getCraetedAt());
        assertNotNull(order2.getOrderDetails());
        assertTrue(order2.getOrderDetails().isEmpty());
        assertNull(order2.getPaymentType());
        assertNull(order2.getUpdatedAt());
        assertNotNull(order2.getUser());
        assertNull(order2.getId());
        assertNull(order2.getPhone());
        assertNotNull(order2.getStatus());
        assertEquals(Order.Status.CREATED, order2.getStatus());
        try {
            assertNull(order2.getTotalPrice());
            fail();
        } catch (Exception e){
            assertTrue(true);
        }

        PaymentType paymentType = Mockito.mock(PaymentType.class);

        Order order3 = new Order(user, 1111111111L, paymentType);
        assertNotNull(order3);
        assertNull(order3.getCraetedAt());
        assertNotNull(order2.getOrderDetails());
        assertTrue(order2.getOrderDetails().isEmpty());
        assertNotNull(order3.getPaymentType());
        assertNull(order3.getUpdatedAt());
        assertNotNull(order3.getUser());
        assertNull(order3.getId());
        assertNotNull(order3.getPhone());
        assertNotNull(order2.getStatus());
        assertEquals(Order.Status.CREATED, order2.getStatus());
        try {
            assertNull(order3.getTotalPrice());
            fail();
        } catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    public void testMethodAddItem(){

        Order order = new Order();

        OrderDetails orderDetails = Mockito.mock(OrderDetails.class);
        List<OrderDetails> orderDetailsList = Mockito.mock(List.class);
        Mockito.doNothing().when(orderDetails).setOrder(order);
        Mockito.doReturn(true).when(orderDetailsList).add(orderDetails);

        order.setOrderDetails(orderDetailsList);
        order.addItem(orderDetails);

        Mockito.verify(orderDetails,Mockito.times(1)).setOrder(order);
        Mockito.verify(orderDetails,Mockito.times(1)).setOrder(Mockito.any(Order.class));

        Mockito.verify(orderDetailsList,Mockito.atLeastOnce()).add(Mockito.any(OrderDetails.class));
    }

    @Test
    public void testMethodGetTotalPrice(){

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCount(1);
        orderDetails.setProductCost(new BigDecimal(1));

        orderDetailsList.add(orderDetails);
        double result = orderDetailsList.stream()
                .map(a->new Double(a.getGroupPrice()))
                .reduce((a,b)->a+b).get();

        assertTrue(result==1);
    }

    @Test
    public void testItemsNameConvertListToString(){

        Order order = new Order();
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        Product product1 = new Product();
        product1.setTitle("product1");
        Product product2 = new Product();
        product2.setTitle("product2");

        OrderDetails orderDetails1 = new OrderDetails();
        orderDetails1.setProduct(product1);
        OrderDetails orderDetails2 = new OrderDetails();
        orderDetails2.setProduct(product2);

        orderDetailsList.addAll(Arrays.asList(orderDetails1,orderDetails2));
        order.setOrderDetails(orderDetailsList);

        String result = order.itemsNameConvertListToString();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("product1, product2",result);

        orderDetailsList = new ArrayList<>();
        order.setOrderDetails(orderDetailsList);

        try {
            order.itemsNameConvertListToString();
            assertTrue(false);
        } catch (Exception e){
            assertTrue(true);
        }
    }
}
