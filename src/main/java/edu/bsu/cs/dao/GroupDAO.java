package edu.bsu.cs.dao;

import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface GroupDAO extends GenericDAO<Group, UUID> {
    List<Group> findByNameContaining(String nameContains);
    List<Group> findByCreator(User creator);
    List<Group> findByMember(User member);
    List<Group> findByAllInterests(Set<Interest> interests);
    List<Group> findByAnyInterest(Set<Interest> interests);
    List<Group> findPublicGroups();
    List<Group> recommendGroupsForUser(User user, Set<Interest> additionalInterests, int limit);
    void createInitialGroups(User systemUser);
}