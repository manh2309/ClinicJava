package org.example.clinicjava.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "accounts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID", nullable = false)
    private Long accountId;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "CREATED_BY")
    private Long createdBy;
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
    @Column(name = "MODIFIED_BY")
    private Long modifiedBy;
    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;
    @Column(name = "ROLE_ID")
    private Long roleId;
}
