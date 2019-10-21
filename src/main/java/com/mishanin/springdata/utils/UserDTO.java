package com.mishanin.springdata.utils;

import com.mishanin.springdata.utils.validation.annotations.CurrentEmail;
import com.mishanin.springdata.utils.validation.annotations.FieldMatch;
import com.mishanin.springdata.utils.validation.annotations.PasswordMatches;
import com.mishanin.springdata.utils.validation.annotations.ValidPassword;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
@NoArgsConstructor
@FieldMatch(first = "password", second = "matchingPassword", message = "The password field must match")
public class UserDTO {

    @NotNull(message = "must be not null")
    @Size(min = 4, message = "is required")
    @ValidPassword
    private String password;

    @NotNull(message = "must be not null")
    @Size(min = 4, message = "is required")
    private String matchingPassword;

    @NotNull(message = "must be not null")
    @Size(min = 1, message = "is required")
    private String firstName;

    @NotNull(message = "must be not null")
    @Size(min = 1, message = "is required")
    private String lastName;

    @NotNull(message = "must be not null")
    @Size(min = 4, message = "is required")
    @CurrentEmail
    private String email;

    @NotNull(message = "must be not null")
    @Size(min = 8, message = "is required")
    private String phone;
}
