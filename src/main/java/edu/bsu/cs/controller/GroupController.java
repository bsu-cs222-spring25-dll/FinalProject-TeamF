package edu.bsu.cs.controller;

import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Interest;
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

    public List<Group> getAllGroups() {
        return groupService.findPublicGroups();
    }

    public List<Group> getUserGroups(User user) {
        return groupService.findGroupsByMember(user);
    }

    public List<Group> searchGroups(String query) {
        return groupService.findGroupsByNameContaining(query);
    }

    public List<Group> getRecommendedGroups(User user) {
        return groupService.recommendGroupsForUser(user, 10); // Show top 10 recommendations
    }

    public boolean joinGroup(Group group, User user) {
        return groupService.joinGroup(group, user);
    }

    public boolean leaveGroup(Group group, User user) {
        return groupService.leaveGroup(group, user);
    }

    public Group createGroup(String name, String description, User creator, boolean isPublic) {
        return groupService.createGroup(name, description, creator, isPublic);
    }

    public Optional<Group> getGroupById(UUID id) {
        return groupService.findById(id);
    }

    public boolean addInterestToGroup(Group group, Interest interest) {
        return groupService.addInterest(group, interest);
    }
}