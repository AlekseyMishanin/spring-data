package com.mishanin.springdata;

import com.mishanin.springdata.entities.User;
import com.mishanin.springdata.repositories.UserRepository;
import com.mishanin.springdata.services.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserService extends Assert {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testFindByUserName(){

        Mockito.doReturn(new User("anon",
                "$2a$10$LrQ5VVqf1M293xzqI3mH8.dtTTnHLGoZ.xgOBZtF4u7WsJ4TY3tw.",
                "anon",
                "anon",
                "anon@gmail.com",
                "+71111111111"))
                .when(userRepository)
                .findOneByUsername("anon");
        User user = userService.findByUserName("anon");
        assertTrue(user != null);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByUsername(ArgumentMatchers.eq("anon"));
        Mockito.verify(userRepository,Mockito.times(1)).findOneByUsername(ArgumentMatchers.any(String.class));
    }
}
