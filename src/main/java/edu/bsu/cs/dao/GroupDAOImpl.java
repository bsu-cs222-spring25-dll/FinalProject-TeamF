package edu.bsu.cs.dao;

import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the GroupDAO interface.
 * Provides database operations for Group entities using Hibernate.
 */
public class GroupDAOImpl extends AbstractDAO<Group, UUID> implements GroupDAO {

    @Override
    public List<Group> findByNameContaining(String nameContains) {
        if (nameContains == null) {
            throw new IllegalArgumentException("Name search text cannot be null");
        }

        return executeWithResult(session -> {
            // Using simple query instead of property access
            String hql = "FROM " + Group.class.getName() + " g WHERE lower(g.name) LIKE lower(:nameParam)";
            return session.createQuery(hql, Group.class)
                    .setParameter("nameParam", "%" + nameContains + "%")
                    .getResultList();
        });
    }

    @Override
    public List<Group> findByCreator(User creator) {
        if (creator == null) {
            throw new IllegalArgumentException("Creator cannot be null");
        }

        return executeWithResult(session -> {
            String hql = "FROM " + Group.class.getName() + " g WHERE g.creator = :creatorParam";
            return session.createQuery(hql, Group.class)
                    .setParameter("creatorParam", creator)
                    .getResultList();
        });
    }

    @Override
    public List<Group> findByMember(User member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }

        return executeWithResult(session -> {
            // Using a native SQL query as an alternative
            String sql = "SELECT g.* FROM groups g " +
                    "JOIN group_members gm ON g.id = gm.group_id " +
                    "WHERE gm.user_id = :memberId";

            return session.createNativeQuery(sql, Group.class)
                    .setParameter("memberId", member.getId())
                    .getResultList();
        });
    }

    @Override
    public List<Group> findByAllInterests(Set<Interest> interests) {
        if (interests == null || interests.isEmpty()) {
            return new ArrayList<>();
        }

        return executeWithResult(session -> {
            // Using a native SQL query as an alternative
            String sql = "SELECT g.* FROM groups g " +
                    "JOIN group_interests gi ON g.id = gi.group_id " +
                    "WHERE gi.interest_id IN (:interestIds) " +
                    "GROUP BY g.id " +
                    "HAVING COUNT(DISTINCT gi.interest_id) = :interestCount";

            // Extract interest IDs
            List<UUID> interestIds = interests.stream()
                    .map(Interest::getId)
                    .collect(Collectors.toList());

            return session.createNativeQuery(sql, Group.class)
                    .setParameter("interestIds", interestIds)
                    .setParameter("interestCount", (long) interests.size())
                    .getResultList();
        });
    }

    @Override
    public List<Group> findByAnyInterest(Set<Interest> interests) {
        if (interests == null || interests.isEmpty()) {
            return new ArrayList<>();
        }

        return executeWithResult(session -> {
            // Using a native SQL query as an alternative
            String sql = "SELECT DISTINCT g.* FROM groups g " +
                    "JOIN group_interests gi ON g.id = gi.group_id " +
                    "WHERE gi.interest_id IN (:interestIds)";

            // Extract interest IDs
            List<UUID> interestIds = interests.stream()
                    .map(Interest::getId)
                    .collect(Collectors.toList());

            return session.createNativeQuery(sql, Group.class)
                    .setParameter("interestIds", interestIds)
                    .getResultList();
        });
    }

    @Override
    public List<Group> findPublicGroups() {
        return executeWithResult(session -> {
            String hql = "FROM " + Group.class.getName() + " g WHERE g.isPublic = true";
            return session.createQuery(hql, Group.class)
                    .getResultList();
        });
    }

    @Override
    public List<Group> recommendGroupsForUser(User user, int limit) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return executeWithResult(session -> {
            // Get the user's interests
            Set<Interest> userInterests = user.getInterests();

            if (userInterests.isEmpty()) {
                // No interests to match against, return empty list
                return new ArrayList<>();
            }

            // Using a native SQL query
            String sql = "SELECT g.* FROM groups g " +
                    "WHERE g.id NOT IN ( " +
                    "  SELECT gm.group_id FROM group_members gm WHERE gm.user_id = :userId " +
                    ")";

            List<Group> potentialGroups = session.createNativeQuery(sql, Group.class)
                    .setParameter("userId", user.getId())
                    .getResultList();

            // Calculate similarity scores and sort by similarity (do this in memory)
            return potentialGroups.stream()
                    .filter(group -> !group.getInterests().isEmpty())
                    .map(group -> new GroupWithSimilarity(group, calculateSimilarity(group.getInterests(), userInterests)))
                    .filter(gws -> gws.similarity > 0.0) // Only include groups with some similarity
                    .sorted((gws1, gws2) -> Double.compare(gws2.similarity, gws1.similarity)) // Sort by descending similarity
                    .limit(limit)
                    .map(gws -> gws.group)
                    .collect(Collectors.toList());
        });
    }

    /**
     * Calculates the similarity between two sets of interests.
     * Uses Jaccard similarity: size of intersection divided by size of union.
     *
     * @param groupInterests The group's interests
     * @param userInterests The user's interests
     * @return A similarity score (0.0 to 1.0) where higher means more similar
     */
    private double calculateSimilarity(Set<Interest> groupInterests, Set<Interest> userInterests) {
        if (groupInterests.isEmpty() || userInterests.isEmpty()) {
            return 0.0;
        }

        // Count interests that appear in both sets
        long commonInterestsCount = groupInterests.stream()
                .filter(userInterests::contains)
                .count();

        // Calculate union size
        long unionSize = groupInterests.size() + userInterests.size() - commonInterestsCount;

        // Jaccard similarity
        return (double) commonInterestsCount / unionSize;
    }

    /**
     * Helper class to hold a group and its similarity score for sorting.
     */
    private static class GroupWithSimilarity {
        Group group;
        double similarity;

        GroupWithSimilarity(Group group, double similarity) {
            this.group = group;
            this.similarity = similarity;
        }
    }
}