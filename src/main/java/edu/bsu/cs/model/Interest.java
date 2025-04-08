package edu.bsu.cs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "interests")
public class Interest {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    // Required by Hibernate
    public Interest() {
        this.id = UUID.randomUUID();
    }

    public Interest(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interest interest = (Interest) o;
        return Objects.equals(id, interest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
