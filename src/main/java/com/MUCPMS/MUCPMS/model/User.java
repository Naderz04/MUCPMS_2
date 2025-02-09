package com.MUCPMS.MUCPMS.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String name;
    private String photoPath;  // New field for storing the photo file path

    // getters and setters

    public enum Role {
        STUDENT, INSTRUCTOR
    }

    public String getPhotoUrl() {
        return photoPath != null ? "/uploads/users/" + photoPath : "/default-avatar.png";
    }
}