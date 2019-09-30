package com.mishanin.springdata.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "products")
public class Product implements Serializable {

    private static final long serialVersionUID = 3059323039068959973L;

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`title`", nullable = false)
    private String title;

    @Column(name = "`price`")
    private BigDecimal price;
}
