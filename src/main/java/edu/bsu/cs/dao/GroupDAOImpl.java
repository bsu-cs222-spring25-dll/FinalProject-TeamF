package edu.bsu.cs.dao;

import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import edu.bsu.cs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class GroupDAOImpl extends AbstractDAO<Group, UUID> implements GroupDAO {

    @Override
    public List<Group> findByNameContaining(String nameContains) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Group g WHERE lower(g.name) LIKE lower(:namePattern)";
            return session.createQuery(hql, Group.class)
                    .setParameter("namePattern", "%" + nameContains + "%")
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Group> findByCreator(User creator) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Group g WHERE g.creator = :creator";
            return session.createQuery(hql, Group.class)
                    .setParameter("creator", creator)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Group> findByMember(User member) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT g FROM Group g JOIN g.members m WHERE m = :member";
            return session.createQuery(hql, Group.class)
                    .setParameter("member", member)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Group> findByAllInterests(Set<Interest> interests) {
        if (interests == null || interests.isEmpty()) {
            return new ArrayList<>();
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT g FROM Group g JOIN g.interests i WHERE i IN (:interests) " +
                    "GROUP BY g HAVING COUNT(DISTINCT i) = :count";
            return session.createQuery(hql, Group.class)
                    .setParameter("interests", interests)
                    .setParameter("count", (long) interests.size())
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Group> findByAnyInterest(Set<Interest> interests) {
        if (interests == null || interests.isEmpty()) {
            return new ArrayList<>();
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT g FROM Group g JOIN g.interests i WHERE i IN (:interests)";
            return session.createQuery(hql, Group.class)
                    .setParameter("interests", interests)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Group> findPublicGroups() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Group g WHERE g.isPublic = true";
            return session.createQuery(hql, Group.class).getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Group> recommendGroupsForUser(User user, Set<Interest> additionalInterests, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Simple implementation - just return public groups the user isn't in
            String hql = "SELECT g FROM Group g WHERE g.isPublic = true AND :user NOT MEMBER OF g.members";
            return session.createQuery(hql, Group.class)
                    .setParameter("user", user)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void createInitialGroups(User systemUser) {
        if (systemUser == null) {
            return;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Define the predefined groups
            Object[][] groupsData = {
                    {"New to Town", "For people who just moved to a new city and want to make friends",
                            new String[]{"Travel"}},
                    {"Tech Enthusiasts", "Discuss the latest in technology and programming",
                            new String[]{"Technology", "Programming"}},
                    {"Outdoor Adventures", "Find people to hike, bike, and explore with",
                            new String[]{"Hiking", "Sports"}},
                    {"Book Club", "Monthly book discussions and recommendations",
                            new String[]{"Reading"}},
                    {"Music Lovers", "Share and discover new music with fellow enthusiasts",
                            new String[]{"Music"}}
            };

            InterestDAO interestDAO = new InterestDAOImpl();

            // Create each group if it doesn't exist
            for (Object[] groupData : groupsData) {
                String name = (String) groupData[0];
                String description = (String) groupData[1];
                String[] interestNames = (String[]) groupData[2];

                // Check if group exists
                String checkHql = "FROM Group g WHERE g.name = :name";
                List<Group> existingGroups = session.createQuery(checkHql, Group.class)
                        .setParameter("name", name)
                        .getResultList();

                if (existingGroups.isEmpty()) {
                    Group group = new Group(name, description, systemUser, true);

                    // Find and add interests
                    for (String interestName : interestNames) {
                        Optional<Interest> interestOpt = interestDAO.findByName(interestName);
                        interestOpt.ifPresent(group::addInterest);
                    }

                    session.save(group);
                }
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public List<Group> findGroupsByUserInterests(User user, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Basic HQL to find groups matching user's interests
            String hql = "SELECT DISTINCT g FROM Group g " +
                    "JOIN g.interests gi " +
                    "WHERE gi IN :userInterests";

            return session.createQuery(hql, Group.class)
                    .setParameter("userInterests", user.getInterests())
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception e) {
            // Log the error
            System.err.println("Error finding groups by interests: " + e.getMessage());
            return new ArrayList<>();
        }
    }


}