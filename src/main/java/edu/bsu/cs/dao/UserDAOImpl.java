package edu.bsu.cs.dao;

import edu.bsu.cs.model.User;
import edu.bsu.cs.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserDAOImpl extends AbstractDAO<User, UUID> implements UserDAO {

    @Override
    public Optional<User> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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
            String hql = "FROM User u WHERE lower(u.username) LIKE lower(:namePattern)";
            return session.createQuery(hql, User.class)
                    .setParameter("namePattern", "%" + nameContains + "%")
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}