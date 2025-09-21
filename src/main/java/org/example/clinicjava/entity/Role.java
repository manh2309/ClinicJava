package org.example.clinicjava.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;
    @Column(name = "ROLE_NAME")
    private String roleName;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
}
