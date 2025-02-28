package edu.bsu.cs.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Utility class for managing Hibernate sessions.
 * Helps ensure collections are properly initialized and sessions are managed correctly.
 */
public class HibernateSessionManager {
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private static final ThreadLocal<Session> threadLocalSession = new ThreadLocal<>();
    private static final ThreadLocal<Transaction> threadLocalTransaction = new ThreadLocal<>();

    /**
     * Opens a new session and begins a transaction.
     * Should be called at the beginning of an operation that requires database access.
     */
    public static void beginTransaction() {
        Session session = sessionFactory.openSession();
        threadLocalSession.set(session);
        Transaction transaction = session.beginTransaction();
        threadLocalTransaction.set(transaction);
    }

    /**
     * Commits the current transaction and closes the session.
     * Should be called at the end of a successful operation.
     */
    public static void commitTransaction() {
        Transaction transaction = threadLocalTransaction.get();
        if (transaction != null) {
            transaction.commit();
            threadLocalTransaction.remove();
        }

        Session session = threadLocalSession.get();
        if (session != null) {
            session.close();
            threadLocalSession.remove();
        }
    }

    /**
     * Rolls back the current transaction and closes the session.
     * Should be called when an operation fails.
     */
    public static void rollbackTransaction() {
        Transaction transaction = threadLocalTransaction.get();
        if (transaction != null) {
            transaction.rollback();
            threadLocalTransaction.remove();
        }

        Session session = threadLocalSession.get();
        if (session != null) {
            session.close();
            threadLocalSession.remove();
        }
    }

    /**
     * Gets the current session.
     * Creates a new session if none exists.
     *
     * @return The current session
     */
    public static Session getCurrentSession() {
        Session session = threadLocalSession.get();
        if (session == null) {
            session = sessionFactory.openSession();
            threadLocalSession.set(session);
        }
        return session;
    }

    /**
     * Executes the given operation within a transaction.
     * Handles the session and transaction management automatically.
     *
     * @param operation The operation to execute
     * @param <T> The return type of the operation
     * @return The result of the operation
     */
    public static <T> T executeWithTransaction(TransactionOperation<T> operation) {
        beginTransaction();
        try {
            T result = operation.execute(getCurrentSession());
            commitTransaction();
            return result;
        } catch (Exception e) {
            rollbackTransaction();
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        }
    }

    /**
     * Interface for operations to be executed within a transaction.
     *
     * @param <T> The return type of the operation
     */
    public interface TransactionOperation<T> {
        T execute(Session session) throws Exception;
    }
}