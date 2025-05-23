package edu.bsu.cs.manager;

import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.GroupService;

import java.util.List;
import java.util.Set;

public class GroupManager {

    private final GroupService groupService;

    public GroupManager(GroupService groupService) {
        this.groupService = groupService;
    }

    public List<Group> getUserGroups(User user) {
        return groupService.findGroupsByMember(user);
    }

    public List<Group> getAllGroups() {
        return groupService.findPublicGroups();
    }

    public List<Group> searchGroups(String query) {
        return groupService.findGroupsByNameContaining(query);
    }

    public boolean joinGroup(Group group, User user) {
        return groupService.joinGroup(group, user);
    }

    public boolean leaveGroup(Group group, User user) {
        return groupService.leaveGroup(group, user);
    }

    public Group createGroup(String name, String description, User creator, boolean isPublic, Set<Interest> interests) {
        return groupService.createGroup(name, description, creator, isPublic, interests);
    }

    public List<Group> findGroupsByUserInterests(User user, int limit) {
        return groupService.findGroupsByUserInterests(user, limit);
    }

}
