package edu.bsu.cs.dao;

import edu.bsu.cs.model.Interest;
import edu.bsu.cs.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InterestDAOImpl extends AbstractDAO<Interest, UUID> implements InterestDAO {

    @Override
    public Optional<Interest> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Interest i WHERE lower(i.name) = lower(:name)";
            Interest interest = session.createQuery(hql, Interest.class)
                    .setParameter("name", name)
                    .uniqueResult();
            return Optional.ofNullable(interest);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Interest> findByNameContaining(String nameContains) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Interest i WHERE lower(i.name) LIKE lower(:namePattern)";
            return session.createQuery(hql, Interest.class)
                    .setParameter("namePattern", "%" + nameContains + "%")
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}