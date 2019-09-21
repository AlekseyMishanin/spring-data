package com.mishanin.springdata.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private Boolean status;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Collection<OrderDetails> orderDetails;
}
