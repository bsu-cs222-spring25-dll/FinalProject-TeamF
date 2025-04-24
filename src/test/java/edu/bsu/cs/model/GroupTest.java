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
    void groupIdShouldNotBeNull() {
        assertNotNull(group.getId());
    }

    @Test
    void groupNameShouldBeSetCorrectly() {
        assertEquals("Test Group", group.getName());
    }


    @Test
    void testAddMember() {
        assertTrue(group.addMember(member1), "Member1 should be successfully added");
        assertTrue(group.getMembers().contains(member1), "Group should contain Member1");

        assertTrue(group.addMember(member2), "Member2 should be successfully added");
        assertTrue(group.getMembers().contains(member2), "Group should contain Member2");
    }

    @Test
    void testRemoveMember() {
        group.addMember(member1);
        assertTrue(group.removeMember(member1), "Member1 should be successfully removed");
        assertFalse(group.getMembers().contains(member1), "Group should no longer contain Member1 after removal");
    }

    @Test
    void testRemoveCreatorNotAllowed() {
        assertFalse(group.removeMember(creator), "Creator should not be removable from the group");
    }

    @Test
    void testAddInterest() {
        assertTrue(group.addInterest(interest1), "Interest1 should be successfully added to the group");
        assertTrue(group.getInterests().contains(interest1), "Group should contain Interest1");

        assertTrue(group.addInterest(interest2), "Interest2 should be successfully added to the group");
        assertTrue(group.getInterests().contains(interest2), "Group should contain Interest2");
    }

    @Test
    void testRemoveInterest() {
        group.addInterest(interest1);
        assertTrue(group.removeInterest(interest1), "Interest1 should be successfully removed from the group");
        assertFalse(group.getInterests().contains(interest1), "Group should no longer contain Interest1 after removal");
    }

    @Test
    void testAddMemberFailsIfAlreadyInGroup() {
        group.addMember(member1);
        assertFalse(group.addMember(member1), "Adding the same member twice should return false");
    }
}
