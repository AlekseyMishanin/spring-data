package com.mishanin.springdata.repositories;

import com.mishanin.springdata.entities.OrderDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long> {
}
