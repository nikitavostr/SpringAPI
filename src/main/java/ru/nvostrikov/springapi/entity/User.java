package ru.nvostrikov.springapi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String avatarUri;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Instant lastStatusChange;
}
