// MessageTest.java
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
        // Create test objects
        sender = new User("sender", "sender@example.com", "password");
        User creator = new User("creator", "creator@example.com", "password");
        group = new Group("Test Group", "Test Description", creator, true);

        message = new Message(sender, group, "Test message content");
        sameMessage = new Message(sender, group, "Different content");
        sameMessage.setId(message.getId()); // Make IDs the same for equality testing
        differentMessage = new Message(sender, group, "Another message");
    }

    @Test
    public void testConstructorSetsValues() {
        // Test that constructor sets values correctly
        assertEquals(sender, message.getSender());
        assertEquals(group, message.getGroup());
        assertEquals("Test message content", message.getContent());
        assertNotNull(message.getId());
        assertNotNull(message.getSentAt());

        // Sent time should be around now
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime messageSentTime = message.getSentAt();
        assertTrue(Math.abs(messageSentTime.getMinute() - now.getMinute()) <= 1);
    }

    @Test
    public void testSettersAndGetters() {
        // Test setters and getters
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
        // Same ID should be equal
        assertEquals(message, sameMessage);

        // Different ID should not be equal
        assertNotEquals(message, differentMessage);

        // Same object should be equal to itself
        assertEquals(message, message);

        // Different object types should not be equal
        assertNotEquals(message, sender);

        // Null should not be equal
        assertNotEquals(message, null);
    }

    @Test
    public void testHashCode() {
        // Same ID should have same hash code
        assertEquals(message.hashCode(), sameMessage.hashCode());

        // Different ID should have different hash code
        assertNotEquals(message.hashCode(), differentMessage.hashCode());
    }
}