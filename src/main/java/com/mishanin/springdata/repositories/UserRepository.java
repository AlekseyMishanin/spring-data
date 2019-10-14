package com.mishanin.springdata.repositories;

import com.mishanin.springdata.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByPhone(String phone);
    User findByPhone(String phone);
}
