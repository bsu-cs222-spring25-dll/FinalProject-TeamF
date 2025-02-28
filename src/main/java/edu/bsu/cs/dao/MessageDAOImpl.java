package edu.bsu.cs.dao;

import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Message;
import edu.bsu.cs.model.User;
import edu.bsu.cs.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageDAOImpl extends AbstractDAO<Message, UUID> implements MessageDAO {

    @Override
    public List<Message> findByGroup(Group group) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Message m WHERE m.group = :group ORDER BY m.sentAt";
            return session.createQuery(hql, Message.class)
                    .setParameter("group", group)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Message> findRecentByGroup(Group group, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Message m WHERE m.group = :group ORDER BY m.sentAt DESC";
            return session.createQuery(hql, Message.class)
                    .setParameter("group", group)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Message> findBySender(User sender) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Message m WHERE m.sender = :sender ORDER BY m.sentAt DESC";
            return session.createQuery(hql, Message.class)
                    .setParameter("sender", sender)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Message> findAfterTime(LocalDateTime time) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Message m WHERE m.sentAt > :time ORDER BY m.sentAt";
            return session.createQuery(hql, Message.class)
                    .setParameter("time", time)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}