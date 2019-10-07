package com.mishanin.springdata.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "phone")
    private Long phone;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetails> orderDetails;

    public Order(User user){
        this.user = user;
        orderDetails = new ArrayList<>();
        status = false;
    }

    public Order(User user, Long phone){
        this.user = user;
        orderDetails = new ArrayList<>();
        status = false;
        this.phone = phone;
    }

    public void addItem(OrderDetails item){
        orderDetails.add(item);
        item.setOrder(this);
    }
}
