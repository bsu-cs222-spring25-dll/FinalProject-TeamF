package edu.bsu.cs.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a group in the social networking application.
 */
public class Group {
    private final UUID id;
    private String name;
    private String description;
    private User creator;
    private Set<User> members;
    private Set<Interest> interests;
    private boolean isPublic;

    /**
     * Creates a new group.
     */
    public Group(String name, String description, User creator, boolean isPublic) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty");
        }
        if (creator == null) {
            throw new IllegalArgumentException("Creator cannot be null");
        }

        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.isPublic = isPublic;
        this.members = new HashSet<>();
        this.members.add(creator); // Creator is automatically a member
        this.interests = new HashSet<>();
    }

    // Basic getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public User getCreator() { return creator; }
    public boolean isPublic() { return isPublic; }

    // Basic setters
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty");
        }
        this.name = name;
    }
    public void setDescription(String description) { this.description = description; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    // Member management
    public Set<User> getMembers() { return new HashSet<>(members); }
    public int getMemberCount() { return members.size(); }

    public boolean addMember(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        return members.add(user);
    }

    public boolean removeMember(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (user.equals(creator)) return false;
        return members.remove(user);
    }

    // Interest management
    public Set<Interest> getInterests() { return new HashSet<>(interests); }

    public boolean addInterest(Interest interest) {
        if (interest == null) throw new IllegalArgumentException("Interest cannot be null");
        return interests.add(interest);
    }

    public boolean removeInterest(Interest interest) {
        if (interest == null) throw new IllegalArgumentException("Interest cannot be null");
        return interests.remove(interest);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Group{id=" + id + ", name='" + name + "', members=" + members.size() + "}";
    }
}