package com.mishanin.springdata.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    public enum Status{
        CREATED, PAID, SEND, RECEIVED, CENCELED
    }

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "phone")
    private Long phone;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime craetedAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetails> orderDetails;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_type_id")
    private PaymentType paymentType;

    public Order(User user){
        this.user = user;
        orderDetails = new ArrayList<>();
        status = Status.CREATED;
    }

    public Order(User user, Long phone, PaymentType paymentType){
        this(user);
        this.phone = phone;
        this.paymentType = paymentType;
    }

    public void addItem(OrderDetails item){
        orderDetails.add(item);
        item.setOrder(this);
    }

    public double getTotalPrice(){
        return orderDetails.stream().map(a->new Double(a.getGroupPrice())).reduce((a,b)->a+b).get();
    }

    public String itemsNameConvertListToString(){
        StringBuilder str = new StringBuilder();
        orderDetails.forEach(item->str.append(item.getProduct().getTitle()).append(", "));
        str.delete(str.lastIndexOf(", "),str.length());
        return str.toString();
    }
}
