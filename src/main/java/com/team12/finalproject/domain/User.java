package com.team12.finalproject.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userName;
    private String password;
    private String refreshToken;
    private LocalDateTime deletedAt;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
