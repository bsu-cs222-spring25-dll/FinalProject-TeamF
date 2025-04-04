package edu.bsu.cs.controller;

import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.GroupService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // Get the groups that the user is a part of
    public List<Group> getUserGroups(User user) {
        return groupService.findGroupsByMember(user);  // Fetch groups for the user
    }

    // Get all public groups
    public List<Group> getAllGroups() {
        return groupService.findPublicGroups();
    }

    // Search groups by name
    public List<Group> searchGroups(String query) {
        return groupService.findGroupsByNameContaining(query);
    }

    // Get recommended groups for a user
    public List<Group> getRecommendedGroups(User user,int limit) {
        return groupService.recommendGroupsForUser(user, 10); // Show top 10 recommendations
    }

    // Join a group
    public boolean joinGroup(Group group, User user) {
        return groupService.joinGroup(group, user);
    }

    // Leave a group
    public boolean leaveGroup(Group group, User user) {
        return groupService.leaveGroup(group, user);
    }

    // Create a new group
    public Group createGroup(String name, String description, User creator, boolean isPublic) {
        return groupService.createGroup(name, description, creator, isPublic);
    }

    // Find groups matching user's interests
    public List<Group> findGroupsByUserInterests(User user, int limit) {
        return groupService.findGroupsByUserInterests(user, limit);
    }

    // Get group by ID
    public Optional<Group> getGroupById(UUID id) {
        return groupService.findById(id);
    }

    // Add an interest to a group
    public boolean addInterestToGroup(Group group, User interest) {
        return groupService.addInterest(group, interest);
    }
}