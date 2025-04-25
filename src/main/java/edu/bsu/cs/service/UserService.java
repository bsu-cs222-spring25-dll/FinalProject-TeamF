package edu.bsu.cs.service;

import edu.bsu.cs.dao.UserDAO;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import edu.bsu.cs.util.HibernateSessionManager;
import edu.bsu.cs.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User registerUser(String username, String email, String password) {
        return HibernateSessionManager.executeWithTransaction(session -> {
            if (userDAO.findByUsername(username).isPresent()) {
                throw new IllegalArgumentException("Username is already in use");
            }
            if (userDAO.findByEmail(email).isPresent()) {
                throw new IllegalArgumentException("Email is already in use");
            }
            User user = new User(username, email, password);
            return userDAO.save(user);
        });
    }

    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = userDAO.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }

    public User updateProfile(User user) {
        return userDAO.update(user);
    }

    public boolean addInterest(User user, Interest interest) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User managedUser = session.get(User.class, user.getId());
            Interest managedInterest = session.get(Interest.class, interest.getId());

            if (managedUser == null || managedInterest == null) {
                return false;
            }

            boolean added = managedUser.addInterest(managedInterest);

            if (added) {
                session.update(managedUser);
                // Update the passed-in user object to reflect changes
                user.getInterests().clear();
                user.getInterests().addAll(managedUser.getInterests());
            }

            transaction.commit();
            return added;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeInterest(User user, Interest interest) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User managedUser = session.get(User.class, user.getId());
            Interest managedInterest = session.get(Interest.class, interest.getId());

            if (managedUser == null || managedInterest == null) {
                return false;
            }

            boolean removed = managedUser.removeInterest(managedInterest);

            if (removed) {
                session.update(managedUser);
                user.getInterests().clear();
                user.getInterests().addAll(managedUser.getInterests());
            }

            transaction.commit();
            return removed;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public void setUserInterests(User user, List<Interest> interests) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User managedUser = session.get(User.class, user.getId());
            managedUser.getInterests().clear();

            for (Interest interest : interests) {
                Interest managedInterest = session.get(Interest.class, interest.getId());
                managedUser.getInterests().add(managedInterest);
            }

            session.update(managedUser);

            user.getInterests().clear();
            user.getInterests().addAll(managedUser.getInterests());

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Optional<User> findById(UUID id) {
        return userDAO.findById(id);
    }
}