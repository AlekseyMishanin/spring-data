package com.mishanin.springdata.repositories;

import com.mishanin.springdata.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {

}
