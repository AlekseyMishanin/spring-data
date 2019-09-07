package com.mishanin.springdata.repositories;

import com.mishanin.springdata.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository <Product,Long> {

    @Query("FROM Product p where p.cost = (select min(a.cost) from Product a)")
    Product findProductByCostIsMin();

    @Query("FROM Product p where p.cost = (select max (a.cost) from Product a)")
    Product findProductByCostIsMax();

    List<Product> findProductsByCostBetween(int min, int max);

    @Override
    long count();
}
