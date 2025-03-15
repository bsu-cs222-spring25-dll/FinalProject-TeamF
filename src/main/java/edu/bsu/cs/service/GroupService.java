package edu.bsu.cs.service;

import edu.bsu.cs.dao.GroupDAO;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;

import java.util.*;

public class GroupService {
    private final GroupDAO groupDAO;

    public GroupService(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public Group createGroup(String name, String description, User creator, boolean isPublic) {
        Group group = new Group(name, description, creator, isPublic);
        return groupDAO.save(group);
    }

    public boolean joinGroup(Group group, User user) {
        if (group.addMember(user)) {
            groupDAO.update(group);
            return true;
        }
        return false;
    }

    public boolean leaveGroup(Group group, User user) {
        if (group.removeMember(user)) {
            groupDAO.update(group);
            return true;
        }
        return false;
    }

    public boolean addInterest(Group group, Interest interest) {
        if (group.addInterest(interest)) {
            groupDAO.update(group);
            return true;
        }
        return false;
    }

    public List<Group> findGroupsByNameContaining(String nameContains) {
        return groupDAO.findByNameContaining(nameContains);
    }

    public List<Group> findGroupsByCreator(User creator) {
        return groupDAO.findByCreator(creator);
    }

    public List<Group> findGroupsByMember(User member) {
        return groupDAO.findByMember(member);
    }

    public List<Group> findGroupsByInterests(Set<Interest> interests) {
        return groupDAO.findByAnyInterest(interests);
    }

    public List<Group> findPublicGroups() {
        if (groupDAO == null) {
            return new ArrayList<>(); // Return empty list instead of null pointer exception
        }
        return groupDAO.findPublicGroups();
    }

    public List<Group> recommendGroupsForUser(User user, int limit) {
        return groupDAO.recommendGroupsForUser(user, user.getInterests(), limit);
    }

    public Optional<Group> findById(UUID id) {
        return groupDAO.findById(id);
    }
}