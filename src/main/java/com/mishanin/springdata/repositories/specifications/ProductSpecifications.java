package com.mishanin.springdata.repositories.specifications;

import com.mishanin.springdata.entities.Product;
import com.mishanin.springdata.entities.ProductGroup;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<Product> titleContains(String word) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + word + "%");
    }

    public static Specification<Product> priceGreaterThanOrEq(double value) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), value);
        };
    }

    public static Specification<Product> priceLesserThanOrEq(double value) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), value);
        };
    }

    public static Specification<Product> productGroupEq(ProductGroup productGroup){
        return (Specification<Product>)(root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("productGroup"), productGroup);
        };
    }
}
