package edu.bsu.cs.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GroupTest {
    private Group group;
    private User creator;
    private User member1;
    private User member2;
    private Interest interest1;
    private Interest interest2;

    @BeforeEach
    void setUp() {
        creator = new User("creatorUser", "creator@example.com", "password123");
        member1 = new User("member1", "member1@example.com", "password123");
        member2 = new User("member2", "member2@example.com", "password123");
        interest1 = new Interest("Technology");
        interest2 = new Interest("Gaming");

        group = new Group("Test Group", "A group for testing", creator, true);
    }

    @Test
    void testGroupCreation() {
        assertNotNull(group.getId(), "Group ID should not be null");
        assertEquals("Test Group", group.getName(), "Group name should match");
        assertEquals("A group for testing", group.getDescription(), "Group description should match");
        assertEquals(creator, group.getCreator(), "Creator should be set correctly");
        assertTrue(group.isPublic(), "Group should be public");
        assertTrue(group.getMembers().contains(creator), "Creator should be a member");
    }

    @Test
    void testAddMember() {
        assertTrue(group.addMember(member1), "Member1 should be added successfully");
        assertTrue(group.getMembers().contains(member1), "Group should contain Member1");

        assertTrue(group.addMember(member2), "Member2 should be added successfully");
        assertTrue(group.getMembers().contains(member2), "Group should contain Member2");
    }

    @Test
    void testRemoveMember() {
        group.addMember(member1);
        assertTrue(group.removeMember(member1), "Member1 should be removed successfully");
        assertFalse(group.getMembers().contains(member1), "Group should no longer contain Member1");
    }

    @Test
    void testRemoveCreatorNotAllowed() {
        assertFalse(group.removeMember(creator), "Creator should not be removable from the group");
    }

    @Test
    void testAddInterest() {
        assertTrue(group.addInterest(interest1), "Interest1 should be added successfully");
        assertTrue(group.getInterests().contains(interest1), "Group should contain Interest1");

        assertTrue(group.addInterest(interest2), "Interest2 should be added successfully");
        assertTrue(group.getInterests().contains(interest2), "Group should contain Interest2");
    }

    @Test
    void testRemoveInterest() {
        group.addInterest(interest1);
        assertTrue(group.removeInterest(interest1), "Interest1 should be removed successfully");
        assertFalse(group.getInterests().contains(interest1), "Group should no longer contain Interest1");
    }
}
