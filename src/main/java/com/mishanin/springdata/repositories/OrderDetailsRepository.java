package com.mishanin.springdata.repositories;

import com.mishanin.springdata.entities.OrderDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long> {
    public List<OrderDetails> findByOrder_Id(Long id);
}
