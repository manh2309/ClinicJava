package org.example.clinicjava.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "token_blacklist")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenBlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "TOKEN", nullable = false, length = 500)
    String token;

    @Column(name = "EXPIRE_AT", nullable = false)
    LocalDateTime expireAt;

    @Column(name = "CREATED_DATE")
    LocalDateTime createdDate;
}
