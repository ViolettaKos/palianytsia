package com.example.palianytsia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "users")
// jsonidentituinfo annotation
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

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "user_role",
                joinColumns = {@JoinColumn(name = "user_id")},
                inverseJoinColumns = {@JoinColumn(name = "role_id")})
        private Collection<Role> roles;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
        private List<Location> locations;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
        private List<Order> orders;
}
