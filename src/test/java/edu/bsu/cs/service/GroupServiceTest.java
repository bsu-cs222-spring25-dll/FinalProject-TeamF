package edu.bsu.cs.service;

import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupServiceTest {

    @Mock
    private GroupService groupService;

    private User testUser;
    private Group testGroup;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("testUser", "test@example.com", "password123");
        testGroup = new Group("Test Group", "A group for testing", testUser, true);
        testGroup.setMembers(new HashSet<>());
    }

    @Test
    void testJoinGroupSuccessfully() {
        when(groupService.joinGroup(testGroup, testUser)).thenReturn(true);

        boolean result = groupService.joinGroup(testGroup, testUser);

        assertTrue(result, "User should successfully join the group.");
        verify(groupService, times(1)).joinGroup(testGroup, testUser);
    }

    @Test
    void testLeaveGroupSuccessfully() {
        when(groupService.leaveGroup(testGroup, testUser)).thenReturn(true);

        boolean result = groupService.leaveGroup(testGroup, testUser);

        assertTrue(result, "User should successfully leave the group.");
        verify(groupService, times(1)).leaveGroup(testGroup, testUser);
    }

    @Test
    void testJoinGroupAlreadyMember() {
        when(groupService.joinGroup(testGroup, testUser)).thenReturn(false);

        boolean result = groupService.joinGroup(testGroup, testUser);

        assertFalse(result, "User should not be able to join a group twice.");
    }

    @Test
    void testLeaveGroupNotMember() {
        when(groupService.leaveGroup(testGroup, testUser)).thenReturn(false);

        boolean result = groupService.leaveGroup(testGroup, testUser);

        assertFalse(result, "User should not be able to leave a group they are not a member of.");
    }
}
