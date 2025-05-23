package edu.bsu.cs.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "event_attendees")
public class EventAttendee {

    public enum AttendanceStatus {
        ATTENDING,
        MAYBE,
        DECLINED
    }

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    @Column(name = "responded_at", nullable = false)
    private LocalDateTime respondedAt;

    // Required by Hibernate
    public EventAttendee() {
        this.id = UUID.randomUUID();
        this.respondedAt = LocalDateTime.now();
    }

    public EventAttendee(Event event, User user, AttendanceStatus status) {
        this.id = UUID.randomUUID();
        this.event = event;
        this.user = user;
        this.status = status;
        this.respondedAt = LocalDateTime.now();
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) {
        this.status = status;
        this.respondedAt = LocalDateTime.now();
    }

    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventAttendee that = (EventAttendee) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}