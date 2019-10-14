package com.mishanin.springdata.events;

import com.mishanin.springdata.services.MailService;
import com.mishanin.springdata.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationCompleteListener implements ApplicationListener<RegistrationCompleteEvent> {

    private UserService userService;
    private MailService mailService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent registrationCompleteEvent) {
        this.confirmRegistration(registrationCompleteEvent);
    }

    private void confirmRegistration(RegistrationCompleteEvent event){
        userService.createVerificationToken(event.getUser(), event.getToken());
        mailService.sendRegistrationMail(event);
    }
}
