package com.mishanin.springdata.controllers;

import com.mishanin.springdata.entities.User;
import com.mishanin.springdata.entities.VerificationToken;
import com.mishanin.springdata.events.RegistrationCompleteEvent;
import com.mishanin.springdata.services.UserService;
import com.mishanin.springdata.utils.UserDTO;
import com.mishanin.springdata.utils.enums.BadToken;
import com.mishanin.springdata.utils.enums.TypeRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserService userService;
    private ApplicationEventPublisher eventPublisher;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /*Во время проверки(валидатор) пароля отпиливает пробелы по краям(на тот случай если пользователь
    * вбил одни пробелы вместо разных символов)*/
    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/")
    public String showRegForm(Model model){
        model.addAttribute("userDto", new UserDTO());
        return "registration-form";
    }

    @PostMapping("/process")
    public String processRegistrationForm(@ModelAttribute("userDto") @Valid UserDTO userDTO, BindingResult bindingResult, Model model, WebRequest request){
        String userPhone = userDTO.getPhone();
        if(bindingResult.hasErrors()){
            return "registration-form";
        }
        User userExist = userService.findByPhone(userPhone);
        if(userExist != null && userExist.getTypereg().equals(TypeRegistration.FULL)){
            model.addAttribute("userDto", userDTO);
            model.addAttribute("registrationError", "User with current username is already exist");
            return "registration-form";
        }
        User user;
        if(userExist != null && userExist.getTypereg().equals(TypeRegistration.SHORT.toString())) {
            user = userService.saveFull(userDTO);
        } else {
            user = userService.save(userDTO);
        }
        //send token
        String pathUrl = "http://localhost:8189" + request.getContextPath() + "/register";
        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, pathUrl));

        return "registration-send-mail";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token){

        Locale locale = request.getLocale();
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if(verificationToken == null){
            model.addAttribute("infoCode", BadToken.NOT_EXIST);
            return "badUser";
        }

        User user = verificationToken.getUser();
        if(Duration.between(LocalDateTime.now(),verificationToken.getDate()).toMillis()<=0){
            model.addAttribute("infoCode", BadToken.EXPIRED);
            return "badUser";
        }

        user.setEnabled(true);
        userService.save(user);
        return "registration-confirmation";
    }
}
