// UserTest.java
package edu.bsu.cs.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class UserTest {

    private User user;
    private User sameUser;
    private User differentUser;
    private Interest interest;

    @BeforeEach
    public void setUp() {
        // Create test objects
        user = new User("testuser", "test@example.com", "password123");
        sameUser = new User("testuser2", "test2@example.com", "password456");
        sameUser.setId(user.getId()); // Make IDs the same for equality testing
        differentUser = new User("otheruser", "other@example.com", "otherpassword");
        interest = new Interest("Programming");
    }

    @Test
    public void testConstructorSetsValues() {
        // Test that constructor sets values correctly
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertNotNull(user.getId());
        assertNotNull(user.getInterests());
        assertTrue(user.getInterests().isEmpty());
    }

    @Test
    public void testSettersAndGetters() {
        // Test setters and getters
        UUID newId = UUID.randomUUID();
        user.setId(newId);
        assertEquals(newId, user.getId());

        user.setUsername("newusername");
        assertEquals("newusername", user.getUsername());

        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());

        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    public void testAddInterest() {
        // Test adding interest
        assertTrue(user.addInterest(interest));
        assertEquals(1, user.getInterests().size());
        assertTrue(user.getInterests().contains(interest));

        // Test adding same interest again (should return false)
        assertFalse(user.addInterest(interest));
        assertEquals(1, user.getInterests().size());
    }

    @Test
    public void testRemoveInterest() {
        // Add interest first
        user.addInterest(interest);

        // Test removing interest
        assertTrue(user.removeInterest(interest));
        assertEquals(0, user.getInterests().size());

        // Test removing non-existent interest
        assertFalse(user.removeInterest(interest));
    }

    @Test
    public void testEquals() {
        // Same ID should be equal
        assertEquals(user, sameUser);

        // Different ID should not be equal
        assertNotEquals(user, differentUser);

        // Same object should be equal to itself
        assertEquals(user, user);

        // Different object types should not be equal
        assertNotEquals(user, interest);

        // Null should not be equal
        assertNotEquals(user, null);
    }

    @Test
    public void testHashCode() {
        // Same ID should have same hash code
        assertEquals(user.hashCode(), sameUser.hashCode());

        // Different ID should have different hash code
        assertNotEquals(user.hashCode(), differentUser.hashCode());
    }
}