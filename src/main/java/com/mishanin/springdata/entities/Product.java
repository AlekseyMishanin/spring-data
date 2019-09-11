package com.mishanin.springdata.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 3059323039068959973L;

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`name`", nullable = false)
    private String name;

    @Column(name = "`cost`")
    private int cost;

    public Product() {
    }

    public Product(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }
}
