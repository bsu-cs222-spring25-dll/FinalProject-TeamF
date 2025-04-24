package edu.bsu.cs.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

@SuppressWarnings("ALL")
public class InterestTest {

    private Interest interest;
    private Interest sameInterest;
    private Interest differentInterest;

    @BeforeEach
    public void setUp() {
        interest = new Interest("Programming");
        sameInterest = new Interest("Different Name");
        sameInterest.setId(interest.getId());
        differentInterest = new Interest("Gaming");
    }

    @Test
    public void defaultConstructorShouldGenerateId() {
        Interest emptyInterest = new Interest();
        assertNotNull(emptyInterest.getId());
    }

    @Test
    public void constructorShouldSetName() {
        assertEquals("Programming", interest.getName());
    }

    @Test
    public void constructorShouldGenerateId() {
        assertNotNull(interest.getId());
    }


    @Test
    public void getIdShouldReturnId() {
        assertEquals(interest.getId(), interest.getId());
    }

    @Test
    public void setIdShouldUpdateId() {
        UUID newId = UUID.randomUUID();
        interest.setId(newId);
        assertEquals(newId, interest.getId());
    }

    @Test
    public void getNameShouldReturnName() {
        assertEquals("Programming", interest.getName());
    }

    @Test
    public void setNameShouldUpdateName() {
        interest.setName("Java");
        assertEquals("Java", interest.getName());
    }


    @Test
    public void equalsShouldReturnTrueForSameObject() {
        assertEquals(interest, interest);
    }

    @Test
    public void equalsShouldReturnTrueForObjectsWithSameId() {
        assertEquals(interest, sameInterest);
    }

    @Test
    public void equalsShouldReturnFalseForObjectsWithDifferentIds() {
        assertNotEquals(interest, differentInterest);
    }

    @Test
    public void equalsShouldReturnFalseForNull() {
        assertNotEquals(interest, null);
    }

    @Test
    public void equalsShouldReturnFalseForDifferentClass() {
        assertNotEquals(interest, "Not an Interest");
    }


    @Test
    public void hashCodeShouldBeEqualForObjectsWithSameId() {
        assertEquals(interest.hashCode(), sameInterest.hashCode());
    }

    @Test
    public void hashCodeShouldBeDifferentForObjectsWithDifferentIds() {
        assertNotEquals(interest.hashCode(), differentInterest.hashCode());
    }

    @Test
    public void setNameToNullShouldWork() {
        interest.setName(null);
        assertNull(interest.getName());
    }

    @Test
    public void setNameToEmptyStringShouldWork() {
        interest.setName("");
        assertEquals("", interest.getName());
    }
}