package com.mishanin.springdata.utils.validation;

import com.mishanin.springdata.utils.UserDTO;
import com.mishanin.springdata.utils.validation.annotations.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/*Класс проверяет совпададают ли пароли введенные при регистрации*/
public class PasswordMatchesValidator
    implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        UserDTO user = (UserDTO)obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
