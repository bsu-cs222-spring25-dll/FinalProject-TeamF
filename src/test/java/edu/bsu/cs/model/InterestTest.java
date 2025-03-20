// InterestTest.java
package edu.bsu.cs.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class InterestTest {

    private Interest interest;
    private Interest sameInterest;
    private Interest differentInterest;

    @BeforeEach
    public void setUp() {
        // Create test objects
        interest = new Interest("Programming");
        sameInterest = new Interest("Reading");
        sameInterest.setId(interest.getId()); // Make IDs the same for equality testing
        differentInterest = new Interest("Music");
    }

    @Test
    public void testConstructorSetsValues() {
        // Test that constructor sets values correctly
        assertEquals("Programming", interest.getName());
        assertNotNull(interest.getId());
    }

    @Test
    public void testSettersAndGetters() {
        // Test setters and getters
        UUID newId = UUID.randomUUID();
        interest.setId(newId);
        assertEquals(newId, interest.getId());

        interest.setName("New Interest");
        assertEquals("New Interest", interest.getName());
    }

    @Test
    public void testEquals() {
        // Same ID should be equal
        assertEquals(interest, sameInterest);

        // Different ID should not be equal
        assertNotEquals(interest, differentInterest);

        // Same object should be equal to itself
        assertEquals(interest, interest);

        // Different object types should not be equal
        assertNotEquals(interest, new User("user", "email", "pass"));

        // Null should not be equal
        assertNotEquals(interest, null);
    }

    @Test
    public void testHashCode() {
        // Same ID should have same hash code
        assertEquals(interest.hashCode(), sameInterest.hashCode());

        // Different ID should have different hash code
        assertNotEquals(interest.hashCode(), differentInterest.hashCode());
    }
}