package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.Order;
import com.mishanin.springdata.events.RegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailMessageBuilder {
    private TemplateEngine templateEngine;

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildOrderEmail(Order order){
        Context context = new Context();
        context.setVariable("order", order);
        return templateEngine.process("order-mail", context);
    }

    public String buildRegActivationEmail(RegistrationCompleteEvent event){
        Context context = new Context();
        context.setVariable("event", event);
        return templateEngine.process("reg-email-activation", context);
    }
}
