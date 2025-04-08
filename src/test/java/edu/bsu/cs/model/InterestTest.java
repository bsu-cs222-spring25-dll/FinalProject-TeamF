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
        interest = new Interest("Programming");
        sameInterest = new Interest("Reading");
        sameInterest.setId(interest.getId()); // Make IDs the same for equality testing
        differentInterest = new Interest("Music");
    }

    @Test
    public void testConstructorSetsValues() {
        assertEquals("Programming", interest.getName());
        assertNotNull(interest.getId());
    }

    @Test
    public void testSettersAndGetters() {
        UUID newId = UUID.randomUUID();
        interest.setId(newId);
        assertEquals(newId, interest.getId());

        interest.setName("New Interest");
        assertEquals("New Interest", interest.getName());
    }

    @Test
    public void testEquals() {
        assertEquals(interest, sameInterest);
        assertNotEquals(interest, differentInterest);
        assertEquals(interest, interest);
        assertNotEquals(interest, new User("user", "email", "pass"));
        assertNotEquals(interest, null);
    }

    @Test
    public void testHashCode() {
        assertEquals(interest.hashCode(), sameInterest.hashCode());
        assertNotEquals(interest.hashCode(), differentInterest.hashCode());
    }
}
