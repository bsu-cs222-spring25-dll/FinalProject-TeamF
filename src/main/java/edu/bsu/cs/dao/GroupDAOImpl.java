package edu.bsu.cs.dao;

import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import edu.bsu.cs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

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

            Object[][] groupsData = {
                    {"New to Town", "For people who just moved to a new city and want to make friends",
                            new String[]{"Travel", "Networking", "Cultural Experiences"}},
                    {"Entrepreneurs & Investors", "Discuss business, startups, investing, and leadership",
                            new String[]{"Entrepreneurship", "Investing", "Personal Finance", "Marketing"}},
                    {"Language & Culture Exchange", "Meet people interested in learning new languages and cultures",
                            new String[]{"Languages", "Cultural Experiences", "Travel"}},
                    {"Volunteers & Activists", "Make an impact through volunteering and activism",
                            new String[]{"Volunteering", "Activism", "Environmentalism"}},
                    {"Tech Enthusiasts", "Discuss the latest in technology, programming, and AI",
                            new String[]{"Technology", "Programming", "AI & Machine Learning", "Cybersecurity"}},
                    {"Science & Space Enthusiasts", "Explore astronomy, physics, and scientific discoveries",
                            new String[]{"Astronomy", "Physics", "Science", "Space Exploration"}},
                    {"Outdoor Adventures", "Find people to hike, bike, camp, and explore with",
                            new String[]{"Hiking", "Cycling", "Camping", "Rock Climbing", "Backpacking"}},
                    {"Fitness & Wellness", "Stay active with fitness buddies and wellness tips",
                            new String[]{"Fitness", "Yoga", "Running", "Weightlifting", "Martial Arts"}},
                    {"Sports Fanatics", "Discuss and play sports like soccer, basketball, and tennis",
                            new String[]{"Soccer", "Basketball", "Tennis", "American Football"}},
                    {"Creative Minds", "A space for artists, designers, writers, and creators",
                            new String[]{"Photography", "Painting", "Digital Art", "Graphic Design", "Writing"}},
                    {"Fashion & Beauty", "Trendy fashion discussions, beauty tips, and makeup tutorials",
                            new String[]{"Fashion", "Makeup", "Jewelry Design", "Tattoo Art"}},
                    {"DIY & Crafting", "A community for crafting, home decor, and DIY projects",
                            new String[]{"DIY Projects", "Interior Design", "Calligraphy"}},
                    {"Book Club", "Monthly book discussions, recommendations, and literary debates",
                            new String[]{"Reading", "Writing", "Poetry", "History"}},
                    {"Movie Buffs", "Discuss the latest movies, TV shows, and film classics",
                            new String[]{"Movies", "TV Shows", "Theater"}},
                    {"Gamers United", "Connect with gamers for casual play, esports, and streaming",
                            new String[]{"Video Games", "Esports", "Board Games", "Streaming"}},
                    {"Music Lovers", "Share and discover new music, instruments, and concerts",
                            new String[]{"Music", "Singing", "DJing"}},
                    {"Foodies & Home Chefs", "Discover and share new recipes, restaurants, and baking tips",
                            new String[]{"Cooking", "Baking", "Foodie Adventures"}},
                    {"Travel & Adventure Seekers", "Plan trips, find travel buddies, and explore new places",
                            new String[]{"Travel", "Backpacking", "Road Trips", "Theme Parks"}},
                    {"Mindfulness & Self-Improvement", "Grow personally through mindfulness and self-development",
                            new String[]{"Mindfulness", "Meditation", "Journaling", "Self-Improvement"}},
                    {"Parenting & Family", "Support and advice for parents raising kids",
                            new String[]{"Parenting", "Relationships"}},
                    {"Pet Lovers", "A community for pet owners to share tips and experiences",
                            new String[]{"Pets"}},
                    {"Astrology & Mysticism", "Discuss horoscopes, tarot, and astrology",
                            new String[]{"Astrology"}},
                    {"True Crime & Conspiracies", "For those fascinated by crime stories and unsolved mysteries",
                            new String[]{"True Crime", "Conspiracy Theories"}},
                    {"Chess & Strategy Games", "For lovers of chess, strategy, and board games",
                            new String[]{"Chess", "Board Games", "Puzzle Solving"}},
            };

            InterestDAO interestDAO = new InterestDAOImpl();

            for (Object[] groupData : groupsData) {
                String name = (String) groupData[0];
                String description = (String) groupData[1];
                String[] interestNames = (String[]) groupData[2];

                String checkHql = "FROM Group g WHERE g.name = :name";
                List<Group> existingGroups = session.createQuery(checkHql, Group.class)
                        .setParameter("name", name)
                        .getResultList();

                if (existingGroups.isEmpty()) {
                    Group group = new Group(name, description, systemUser, true);

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

    @Override
    public List<Group> findGroupsByUserInterests(User user, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT g FROM Group g " +
                    "JOIN g.interests gi " +
                    "WHERE gi IN :userInterests";

            return session.createQuery(hql, Group.class)
                    .setParameter("userInterests", user.getInterests())
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
