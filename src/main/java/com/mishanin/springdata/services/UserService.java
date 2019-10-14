package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.User;
import com.mishanin.springdata.entities.VerificationToken;
import com.mishanin.springdata.utils.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(UserDTO userDTO);
    User save(User user);
    User getUser(String verificationToken);
    void createVerificationToken(User user, String token);
    VerificationToken getVerificationToken(String verificationToken);
    User findByPhone(String phone);
    User saveAnonimus(String phone);
    User saveFull(UserDTO userDTO);
}
