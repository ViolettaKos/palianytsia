package com.example.palianytsia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        @Column(name = "user_id", unique = true, nullable = false)
        private long id;

        @Column(unique = true, nullable = false)
        private String email;

        @Column(name = "first_name", nullable = false)
        private String firstName;

        @Column(name = "last_name", nullable = false)
        private String lastName;

        @Column(name = "pass", nullable = false)
        private String password;

        @Column(name = "mobile_num", nullable = false)
        private String mobileNumber;

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        private Role role;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
        private List<Location> locations;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
        private List<Order> orders;
}
