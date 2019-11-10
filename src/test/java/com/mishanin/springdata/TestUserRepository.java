package com.mishanin.springdata;

import com.mishanin.springdata.entities.Role;
import com.mishanin.springdata.entities.User;
import com.mishanin.springdata.repositories.UserRepository;
import com.mishanin.springdata.utils.enums.TypeRegistration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations="classpath:application.properties")
public class TestUserRepository {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void productRepositoryTest() {

        Role role = new Role("ROLE_ADMIN");

        User user = new User("password",
                "firstname",
                "lastname",
                "email",
                "phone",
                Arrays.asList(role),
                TypeRegistration.FULL,
                true);
        entityManager.persist(role);
        entityManager.flush();
        User out = entityManager.persist(user);
        entityManager.flush();

        List<User> productsList = (List<User>)userRepository.findAll();
        System.out.println(productsList.get(2).getPhone());

        Assert.assertEquals(3, productsList.size());
    }
}
