package com.mishanin.springdata.services;

import com.mishanin.springdata.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUserName(String username);
}
