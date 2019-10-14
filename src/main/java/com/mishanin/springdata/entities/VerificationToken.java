package com.mishanin.springdata.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "verification_tokens")
public class VerificationToken {

    private static final int EXPIRATION = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "date_create")
    private LocalDateTime date;

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.date = LocalDateTime.now().plusDays(EXPIRATION);
    }
}
