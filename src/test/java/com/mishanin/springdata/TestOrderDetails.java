package com.mishanin.springdata;

import com.mishanin.springdata.entities.OrderDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class TestOrderDetails {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0, 0 }, { 1, 1 }, { 2, 1 }, { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 }
        });
    }

    @Parameter(0)
    public int count;

    @Parameter(1)
    public int price;

    public double result;

    @Before
    public void init(){
        this.result = count * price;
    }

    @Test
    public void testMethodGetGroupPrice(){

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCount(count);
        orderDetails.setProductCost(new BigDecimal(price));

        double localResult = orderDetails.getGroupPrice();

        Assert.assertTrue(this.result==localResult);
    }
}
