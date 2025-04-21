package edu.bsu.cs.dao;

import edu.bsu.cs.model.Event;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.User;
import edu.bsu.cs.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventDAOImpl extends AbstractDAO<Event, UUID> implements EventDAO {

    @Override
    public List<Event> findByGroup(Group group) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Event e WHERE e.group = :group ORDER BY e.startTime";
            return session.createQuery(hql, Event.class)
                    .setParameter("group", group)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Event> findUpcomingEvents(LocalDateTime from, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Event e WHERE e.startTime >= :from ORDER BY e.startTime";
            return session.createQuery(hql, Event.class)
                    .setParameter("from", from)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Event> findUpcomingEventsForUser(User user, LocalDateTime from, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Get events from groups where the user is a member
            String hql = "SELECT e FROM Event e JOIN e.group g JOIN g.members m " +
                    "WHERE m = :user AND e.startTime >= :from " +
                    "ORDER BY e.startTime";
            return session.createQuery(hql, Event.class)
                    .setParameter("user", user)
                    .setParameter("from", from)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}