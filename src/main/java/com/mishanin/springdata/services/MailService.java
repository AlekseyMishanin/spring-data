package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.Order;
import com.mishanin.springdata.events.RegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailService {
    private JavaMailSender sender;
    private MailMessageBuilder messageBuilder;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }

    @Autowired
    public void setMessageBuilder(MailMessageBuilder messageBuilder) {
        this.messageBuilder = messageBuilder;
    }

    private void sendMail(String email, String subject, String text){
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        try {
            helper.setTo(email);
            helper.setText(text, true);
            helper.setSubject(subject);
        } catch (MessagingException e){
            e.printStackTrace();
        }
        try {
            executorService.submit(()->sender.send(message));
        } catch (MailException e){
            e.printStackTrace();
        }
    }

    public void sendOrderMail(Order order){
        sendMail(order.getUser().getEmail(),
                String.format("Заказ %d%n отправлен в обработку", order.getId()),
                messageBuilder.buildOrderEmail(order));
    }

    public void sendRegistrationMail(RegistrationCompleteEvent event){
        sendMail(event.getUser().getEmail(),
                "Confirmation of registration",
                messageBuilder.buildRegActivationEmail(event));
    }
}
