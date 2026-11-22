package kz.platonus.model;

import java.util.Objects;

/**
 * Abstract base class for all users of Platonus.
 * Demonstrates abstraction, encapsulation and inheritance.
 */
public abstract class User {

    private final int id;
    private String fullName;
    private String email;

    public User(int id, String fullName, String email) {
        if (id <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        this.email = email;
    }

    public abstract UserRole getRole();

    public String getInfo() {
        return "ID: " + id +
                ", Name: " + fullName +
                ", Email: " + email +
                ", Role: " + getRole();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof User other)) {
            return false;
        }

        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}