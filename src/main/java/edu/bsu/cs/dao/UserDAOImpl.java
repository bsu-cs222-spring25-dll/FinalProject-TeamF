package edu.bsu.cs.dao;

import edu.bsu.cs.model.User;
import edu.bsu.cs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserDAOImpl extends AbstractDAO<User, UUID> implements UserDAO {

    @Override
    public Optional<User> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.clear();

            String hql = "FROM User u WHERE u.username = :username";
            User user = session.createQuery(hql, User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.clear();

            String hql = "FROM User u WHERE u.email = :email";
            User user = session.createQuery(hql, User.class)
                    .setParameter("email", email)
                    .uniqueResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findByNameContaining(String nameContains) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.clear();

            String hql = "FROM User u WHERE lower(u.username) LIKE lower(:namePattern)";
            return session.createQuery(hql, User.class)
                    .setParameter("namePattern", "%" + nameContains + "%")
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public User save(User entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            session.clear();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving entity", e);
        }
    }

    @Override
    public User update(User entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User updatedEntity = (User) session.merge(entity);
            transaction.commit();
            session.clear();
            return updatedEntity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating entity", e);
        }
    }
}