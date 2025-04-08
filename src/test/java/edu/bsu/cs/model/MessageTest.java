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

    @Test
    public void testConstructorSetsValues() {
        assertEquals(sender, message.getSender());
        assertEquals(group, message.getGroup());
        assertEquals("Test message content", message.getContent());
        assertNotNull(message.getId());
        assertNotNull(message.getSentAt());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime messageSentTime = message.getSentAt();
        assertTrue(Math.abs(messageSentTime.getMinute() - now.getMinute()) <= 1);
    }

    @Test
    public void testSettersAndGetters() {
        UUID newId = UUID.randomUUID();
        message.setId(newId);
        assertEquals(newId, message.getId());

        User newSender = new User("newsender", "new@example.com", "newpass");
        message.setSender(newSender);
        assertEquals(newSender, message.getSender());

        User newCreator = new User("newcreator", "creator@example.com", "pass");
        Group newGroup = new Group("New Group", "New Description", newCreator, false);
        message.setGroup(newGroup);
        assertEquals(newGroup, message.getGroup());

        message.setContent("New content");
        assertEquals("New content", message.getContent());

        LocalDateTime newTime = LocalDateTime.now().minusDays(1);
        message.setSentAt(newTime);
        assertEquals(newTime, message.getSentAt());
    }

    @Test
    public void testEquals() {
        assertEquals(message, sameMessage);
        assertNotEquals(message, differentMessage);
        assertEquals(message, message);
        assertNotEquals(message, sender);
        assertNotEquals(message, null);
    }

    @Test
    public void testHashCode() {
        assertEquals(message.hashCode(), sameMessage.hashCode());
        assertNotEquals(message.hashCode(), differentMessage.hashCode());
    }
}
