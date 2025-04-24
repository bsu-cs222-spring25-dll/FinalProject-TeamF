package edu.bsu.cs.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "events")
public class Event {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    // Required by Hibernate
    public Event() {
        this.id = UUID.randomUUID();
    }

    public Event(String title, String description, LocalDateTime startTime, LocalDateTime endTime, Group group) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.group = group;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return title + " - " + formatDateTime(startTime);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.getMonth() + " " + dateTime.getDayOfMonth() + ", " + dateTime.getYear();
    }
}