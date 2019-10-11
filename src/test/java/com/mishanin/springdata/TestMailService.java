package com.mishanin.springdata;

import com.mishanin.springdata.entities.Order;
import com.mishanin.springdata.entities.User;
import com.mishanin.springdata.events.RegistrationCompleteEvent;
import com.mishanin.springdata.services.MailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMailService {

    @Mock private MailService mailService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendMail(){

        doNothing().when(mailService).sendOrderMail(new Order(new User()));
        doNothing().when(mailService).sendRegistrationMail(new RegistrationCompleteEvent(new User(), new String()));
        Order order1 = new Order(new User());
        RegistrationCompleteEvent event = new RegistrationCompleteEvent(new User(), new String());

        mailService.sendOrderMail(order1);
        mailService.sendRegistrationMail(event);

        verify(mailService).sendOrderMail(any(Order.class));
        verify(mailService).sendOrderMail(order1);
        verify(mailService).sendRegistrationMail(eq(event));

        Order order2 = new Order(new User());

        mailService.sendOrderMail(order2);
        verify(mailService, times(2)).sendOrderMail(ArgumentMatchers.any(Order.class));
        verify(mailService, atLeastOnce()).sendRegistrationMail(ArgumentMatchers.any(RegistrationCompleteEvent.class));

    }

    @Test(expected = Exception.class)
    public void testSendMailWithException1(){

        doThrow(Exception.class).when(mailService).sendOrderMail(null);

        mailService.sendOrderMail(null);
    }
    @Test(expected = Exception.class)
    public void testSendMailWithException2(){

        doThrow(Exception.class).when(mailService).sendRegistrationMail(null);

        mailService.sendRegistrationMail(null);
    }
}
