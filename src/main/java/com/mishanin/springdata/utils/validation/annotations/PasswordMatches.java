package com.mishanin.springdata.utils.validation.annotations;

import com.mishanin.springdata.utils.validation.PasswordMatchesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface PasswordMatches {
    String message() default "Password don't match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
