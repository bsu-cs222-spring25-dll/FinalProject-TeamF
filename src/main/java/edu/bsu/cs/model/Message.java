package edu.bsu.cs.model;

import java.util.Objects;
import java.util.UUID;

public class Message {
    private final UUID id;
    private final User sender;
    private final Group group;
    private String content;


    public Message(User sender, Group group, String content) {
        if (sender == null) {
            throw new IllegalArgumentException("Sender cannot be null");
        }
        if (group == null) {
            throw new IllegalArgumentException("Group cannot be null");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be null or empty");
        }

        this.id = UUID.randomUUID();
        this.sender = sender;
        this.group = group;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }


    public User getSender() {
        return sender;
    }


    public Group getGroup() {
        return group;
    }


    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Message{sender=" + sender.getName() +
                ", content='" + (content.length() > 20 ? content.substring(0, 20) + "..." : content) + "'}";
    }
}