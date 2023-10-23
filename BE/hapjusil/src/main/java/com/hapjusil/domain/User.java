package com.hapjusil.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String encryptedPassword;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String name, String email, String encryptedPassword, Role role) {
        this.name = name;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.role = role;
    }

    public User update(String name) {
        this.name = name;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
