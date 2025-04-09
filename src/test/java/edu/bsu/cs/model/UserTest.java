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

    // Constructor tests
    @Test
    public void constructorShouldSetUsername() {
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void constructorShouldSetEmail() {
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void constructorShouldSetPassword() {
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void constructorShouldGenerateId() {
        assertNotNull(user.getId());
    }

    @Test
    public void newUserShouldHaveEmptyInterestsSet() {
        assertTrue(user.getInterests().isEmpty());
    }

    // Setter/Getter tests
    @Test
    public void setIdShouldUpdateId() {
        UUID newId = UUID.randomUUID();
        user.setId(newId);
        assertEquals(newId, user.getId());
    }

    @Test
    public void setUsernameShouldUpdateUsername() {
        user.setUsername("newusername");
        assertEquals("newusername", user.getUsername());
    }

    @Test
    public void setEmailShouldUpdateEmail() {
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
    }

    @Test
    public void setPasswordShouldUpdatePassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    // Interest management tests
    @Test
    public void addInterestShouldReturnTrueWhenAddingNewInterest() {
        assertTrue(user.addInterest(interest));
    }

    @Test
    public void addInterestShouldAddInterestToCollection() {
        user.addInterest(interest);
        assertTrue(user.getInterests().contains(interest));
    }

    @Test
    public void addInterestShouldIncreaseSizeOfInterestCollection() {
        int initialSize = user.getInterests().size();
        user.addInterest(interest);
        assertEquals(initialSize + 1, user.getInterests().size());
    }

    @Test
    public void addInterestShouldReturnFalseWhenAddingDuplicateInterest() {
        user.addInterest(interest);
        assertFalse(user.addInterest(interest));
    }

    @Test
    public void addDuplicateInterestShouldNotChangeSizeOfCollection() {
        user.addInterest(interest);
        int sizeAfterFirstAdd = user.getInterests().size();
        user.addInterest(interest);
        assertEquals(sizeAfterFirstAdd, user.getInterests().size());
    }

    @Test
    public void removeInterestShouldReturnTrueWhenRemovingExistingInterest() {
        user.addInterest(interest);
        assertTrue(user.removeInterest(interest));
    }

    @Test
    public void removeInterestShouldRemoveInterestFromCollection() {
        user.addInterest(interest);
        user.removeInterest(interest);
        assertFalse(user.getInterests().contains(interest));
    }

    @Test
    public void removeInterestShouldDecreaseSizeOfInterestCollection() {
        user.addInterest(interest);
        int sizeBeforeRemove = user.getInterests().size();
        user.removeInterest(interest);
        assertEquals(sizeBeforeRemove - 1, user.getInterests().size());
    }

    @Test
    public void removeInterestShouldReturnFalseWhenRemovingNonexistentInterest() {
        assertFalse(user.removeInterest(interest));
    }

    // Equals and hashCode tests
    @Test
    public void equalsShouldReturnTrueForSameObject() {
        assertEquals(user, user);
    }

    @Test
    public void equalsShouldReturnTrueForObjectsWithSameId() {
        assertEquals(user, sameUser);
    }

    @Test
    public void equalsShouldReturnFalseForObjectsWithDifferentIds() {
        assertNotEquals(user, differentUser);
    }

    @Test
    public void equalsShouldReturnFalseForNull() {
        assertNotEquals(user, null);
    }

    @Test
    public void equalsShouldReturnFalseForDifferentClass() {
        assertNotEquals(user, interest);
    }

    @Test
    public void hashCodeShouldBeEqualForObjectsWithSameId() {
        assertEquals(user.hashCode(), sameUser.hashCode());
    }

    @Test
    public void hashCodeShouldBeDifferentForObjectsWithDifferentIds() {
        assertNotEquals(user.hashCode(), differentUser.hashCode());
    }
}