package edu.bsu.cs.dao;

import edu.bsu.cs.model.Event;
import edu.bsu.cs.model.EventAttendee;
import edu.bsu.cs.model.User;
import edu.bsu.cs.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EventAttendeeDAOImpl extends AbstractDAO<EventAttendee, UUID> implements EventAttendeeDAO {

    @Override
    public List<EventAttendee> findByEvent(Event event) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM EventAttendee ea WHERE ea.event = :event";
            return session.createQuery(hql, EventAttendee.class)
                    .setParameter("event", event)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<EventAttendee> findByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM EventAttendee ea WHERE ea.user = :user";
            return session.createQuery(hql, EventAttendee.class)
                    .setParameter("user", user)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<EventAttendee> findByEventAndUser(Event event, User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM EventAttendee ea WHERE ea.event = :event AND ea.user = :user";
            EventAttendee attendee = session.createQuery(hql, EventAttendee.class)
                    .setParameter("event", event)
                    .setParameter("user", user)
                    .uniqueResult();
            return Optional.ofNullable(attendee);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAttendingUsersByEvent(Event event) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT ea.user FROM EventAttendee ea WHERE ea.event = :event AND ea.status = 'ATTENDING'";
            return session.createQuery(hql, User.class)
                    .setParameter("event", event)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}