package edu.bsu.cs.dao;

import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * DAO interface for Group entities.
 * Defines operations for storing and retrieving groups from the database.
 */
public interface GroupDAO extends GenericDAO<Group, UUID> {

    /**
     * Finds groups by name containing the given text (case-insensitive).
     *
     * @param nameContains Text to search for in group names
     * @return A list of matching groups
     */
    List<Group> findByNameContaining(String nameContains);

    /**
     * Finds groups created by a specific user.
     *
     * @param creator The creator user
     * @return A list of groups created by the user
     */
    List<Group> findByCreator(User creator);

    /**
     * Finds groups that a user is a member of.
     *
     * @param member The member user
     * @return A list of groups the user is a member of
     */
    List<Group> findByMember(User member);

    /**
     * Finds groups that have all of the specified interests.
     *
     * @param interests Set of interests to match
     * @return A list of groups with all the specified interests
     */
    List<Group> findByAllInterests(Set<Interest> interests);

    /**
     * Finds groups that have any of the specified interests.
     *
     * @param interests Set of interests to match any of
     * @return A list of groups with any of the specified interests
     */
    List<Group> findByAnyInterest(Set<Interest> interests);

    /**
     * Finds public groups.
     *
     * @return A list of all public groups
     */
    List<Group> findPublicGroups();

    /**
     * Recommends groups for a user based on their interests.
     *
     * @param user The user to find recommendations for
     * @param limit Maximum number of recommendations to return
     * @return A list of recommended groups, sorted by relevance
     */
    List<Group> recommendGroupsForUser(User user, int limit);
}