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
        user = new User("testuser", "test@example.com", "password123");
        sameUser = new User("testuser2", "test2@example.com", "password456");
        sameUser.setId(user.getId());
        differentUser = new User("otheruser", "other@example.com", "otherpassword");
        interest = new Interest("Programming");
    }

    @Test
    public void testConstructorSetsValues() {
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertNotNull(user.getId());
        assertNotNull(user.getInterests());
        assertTrue(user.getInterests().isEmpty());
    }

    @Test
    public void testSettersAndGetters() {
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
        assertTrue(user.addInterest(interest));
        assertEquals(1, user.getInterests().size());
        assertTrue(user.getInterests().contains(interest));

        assertFalse(user.addInterest(interest));
        assertEquals(1, user.getInterests().size());
    }

    @Test
    public void testRemoveInterest() {
        user.addInterest(interest);
        assertTrue(user.removeInterest(interest));
        assertEquals(0, user.getInterests().size());

        assertFalse(user.removeInterest(interest));
    }

    @Test
    public void testEquals() {
        assertEquals(user, sameUser);
        assertNotEquals(user, differentUser);
        assertEquals(user, user);
        assertNotEquals(user, interest);
        assertNotEquals(user, null);
    }

    @Test
    public void testHashCode() {
        assertEquals(user.hashCode(), sameUser.hashCode());
        assertNotEquals(user.hashCode(), differentUser.hashCode());
    }
}
