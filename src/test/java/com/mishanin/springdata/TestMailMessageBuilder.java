package com.mishanin.springdata;

import com.mishanin.springdata.entities.Order;
import com.mishanin.springdata.entities.User;
import com.mishanin.springdata.events.RegistrationCompleteEvent;
import com.mishanin.springdata.services.MailMessageBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMailMessageBuilder {

    @Mock private MailMessageBuilder mailMessageBuilder;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBuildEmail(){
        when(mailMessageBuilder.buildOrderEmail(new Order())).thenReturn("testOrderHTML");

        RegistrationCompleteEvent rce = new RegistrationCompleteEvent(new User(), new String());
        when(mailMessageBuilder.buildRegActivationEmail(rce)).thenReturn("testRegHTML");

        Assert.assertEquals("testOrderHTML", mailMessageBuilder.buildOrderEmail(new Order()));
        Assert.assertEquals("testRegHTML", mailMessageBuilder.buildRegActivationEmail(rce));

        verify(mailMessageBuilder).buildOrderEmail(any(Order.class));
        verify(mailMessageBuilder,atLeastOnce()).buildOrderEmail(any(Order.class));

        verify(mailMessageBuilder).buildRegActivationEmail(any(RegistrationCompleteEvent.class));
        verify(mailMessageBuilder,atLeastOnce()).buildRegActivationEmail(any(RegistrationCompleteEvent.class));
    }

    @Test (expected = Exception.class)
    public void testNullEmail1(){
        when(mailMessageBuilder.buildOrderEmail(null)).thenThrow(Exception.class);

        mailMessageBuilder.buildOrderEmail(new Order());
    }

    @Test (expected = Exception.class)
    public void testNullEmail2(){
        when(mailMessageBuilder.buildRegActivationEmail(null)).thenThrow(Exception.class);

        mailMessageBuilder.buildRegActivationEmail(new RegistrationCompleteEvent(new User(), new String()));
    }
}
