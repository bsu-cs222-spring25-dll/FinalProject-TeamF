package edu.bsu.cs.dao;

import edu.bsu.cs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

/**
 * Abstract implementation of the GenericDAO interface.
 * Provides common CRUD operations using Hibernate.
 *
 * @param <T> The entity type
 * @param <ID> The type of the entity's ID
 */
public abstract class AbstractDAO<T, ID extends Serializable> implements GenericDAO<T, ID> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDAO.class);
    protected final Class<T> entityClass;

    /**
     * Constructor that determines the entity class from the generic parameter.
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    /**
     * Executes a transaction with a return value.
     *
     * @param <R> Return type
     * @param daoFunction Function to execute within the transaction
     * @return Result of the function
     */
    protected <R> R executeWithResult(TransactionFunction<R> daoFunction) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            R result = daoFunction.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Transaction failed", e);
            throw new RuntimeException("Failed to execute transaction", e);
        }
    }

    /**
     * Executes a transaction without a return value.
     *
     * @param daoAction Action to execute within the transaction
     */
    protected void executeWithoutResult(TransactionAction daoAction) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            daoAction.accept(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Transaction failed", e);
            throw new RuntimeException("Failed to execute transaction", e);
        }
    }

    @Override
    public T save(T entity) {
        return executeWithResult(session -> {
            session.persist(entity);
            return entity;
        });
    }

    @Override
    public T update(T entity) {
        return executeWithResult(session -> session.merge(entity));
    }

    @Override
    public Optional<T> findById(ID id) {
        return executeWithResult(session ->
                Optional.ofNullable(session.get(entityClass, id))
        );
    }

    @Override
    public List<T> findAll() {
        return executeWithResult(session ->
                session.createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList()
        );
    }

    @Override
    public void delete(T entity) {
        executeWithoutResult(session -> session.remove(entity));
    }

    @Override
    public boolean deleteById(ID id) {
        return executeWithResult(session -> {
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.remove(entity);
                return true;
            }
            return false;
        });
    }

    /**
     * Functional interface for operations with Session that return a result.
     */
    @FunctionalInterface
    protected interface TransactionFunction<R> {
        R apply(Session session);
    }

    /**
     * Functional interface for operations with Session that don't return a result.
     */
    @FunctionalInterface
    protected interface TransactionAction {
        void accept(Session session);
    }
}
