package edu.bsu.cs.model;

import java.util.Objects;
import java.util.UUID;


 //Represents an interest or hobby that can be associated with users and groups.
 //This class follows the Single Responsibility Principle by only managing interest data.

public class Interest {
    private final UUID id;
    private String name;
    private String category;

    public Interest(String name, String category) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Interest name cannot be null or empty");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }

        this.id = UUID.randomUUID();
        this.name = name;
        this.category = category;
    }

    public UUID getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Interest name cannot be null or empty");
        }
        this.name = name;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        this.category = category;
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

    @Override
    public String toString() {
        return name + " (" + category + ")";
    }
}