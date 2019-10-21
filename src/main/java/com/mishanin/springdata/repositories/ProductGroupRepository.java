package com.mishanin.springdata.repositories;

import com.mishanin.springdata.entities.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long>, JpaSpecificationExecutor<ProductGroup> {
    ProductGroup findProductGroupByTitle(String title);
}
