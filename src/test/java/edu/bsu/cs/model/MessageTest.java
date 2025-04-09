package edu.bsu.cs.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessageTest {

    private Message message;
    private Message sameMessage;
    private Message differentMessage;
    private User sender;
    private Group group;

    @BeforeEach
    public void setUp() {
        sender = new User("sender", "sender@example.com", "password");
        User creator = new User("creator", "creator@example.com", "password");
        group = new Group("Test Group", "Test Description", creator, true);

        message = new Message(sender, group, "Test message content");
        sameMessage = new Message(sender, group, "Different content");
        sameMessage.setId(message.getId());
        differentMessage = new Message(sender, group, "Another message");
    }

    // Constructor tests
    @Test
    public void constructorShouldSetSender() {
        assertEquals(sender, message.getSender());
    }

    @Test
    public void constructorShouldSetGroup() {
        assertEquals(group, message.getGroup());
    }

    @Test
    public void constructorShouldSetContent() {
        assertEquals("Test message content", message.getContent());
    }

    @Test
    public void constructorShouldGenerateId() {
        assertNotNull(message.getId());
    }

    @Test
    public void constructorShouldSetSentAtTime() {
        assertNotNull(message.getSentAt());
    }

    @Test
    public void sentAtTimeShouldBeCloseToCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime messageSentTime = message.getSentAt();
        assertTrue(Math.abs(messageSentTime.getMinute() - now.getMinute()) <= 1);
    }

    // Setter/Getter tests
    @Test
    public void setIdShouldUpdateId() {
        UUID newId = UUID.randomUUID();
        message.setId(newId);
        assertEquals(newId, message.getId());
    }

    @Test
    public void setSenderShouldUpdateSender() {
        User newSender = new User("newsender", "new@example.com", "newpass");
        message.setSender(newSender);
        assertEquals(newSender, message.getSender());
    }

    @Test
    public void setGroupShouldUpdateGroup() {
        User newCreator = new User("newcreator", "creator@example.com", "pass");
        Group newGroup = new Group("New Group", "New Description", newCreator, false);
        message.setGroup(newGroup);
        assertEquals(newGroup, message.getGroup());
    }

    @Test
    public void setContentShouldUpdateContent() {
        message.setContent("New content");
        assertEquals("New content", message.getContent());
    }

    @Test
    public void setSentAtShouldUpdateSentAtTime() {
        LocalDateTime newTime = LocalDateTime.now().minusDays(1);
        message.setSentAt(newTime);
        assertEquals(newTime, message.getSentAt());
    }

    // Equals and hashCode tests
    @Test
    public void equalsShouldReturnTrueForSameObject() {
        assertEquals(message, message);
    }

    @Test
    public void equalsShouldReturnTrueForObjectsWithSameId() {
        assertEquals(message, sameMessage);
    }

    @Test
    public void equalsShouldReturnFalseForObjectsWithDifferentIds() {
        assertNotEquals(message, differentMessage);
    }

    @Test
    public void equalsShouldReturnFalseForNull() {
        assertNotEquals(message, null);
    }

    @Test
    public void equalsShouldReturnFalseForDifferentClass() {
        assertNotEquals(message, sender);
    }

    @Test
    public void hashCodeShouldBeEqualForObjectsWithSameId() {
        assertEquals(message.hashCode(), sameMessage.hashCode());
    }

    @Test
    public void hashCodeShouldBeDifferentForObjectsWithDifferentIds() {
        assertNotEquals(message.hashCode(), differentMessage.hashCode());
    }
}