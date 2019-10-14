package com.mishanin.springdata.utils.validation.annotations;

import com.mishanin.springdata.utils.validation.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/*
* Аннотация для проверки поля email для замены аннотации @Email от Hibernate, которая
* считает валидным старый формат интрасети address@server
* */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Documented
@Constraint(validatedBy = EmailValidator.class)
public @interface CurrentEmail {
    String message() default "Invalid email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
