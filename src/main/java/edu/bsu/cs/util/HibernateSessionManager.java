package edu.bsu.cs.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateSessionManager {
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private static final ThreadLocal<Session> threadLocalSession = new ThreadLocal<>();
    private static final ThreadLocal<Transaction> threadLocalTransaction = new ThreadLocal<>();

    public static void beginTransaction() {
        Session session = sessionFactory.openSession();
        threadLocalSession.set(session);
        Transaction transaction = session.beginTransaction();
        threadLocalTransaction.set(transaction);
    }

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

    public static Session getCurrentSession() {
        Session session = threadLocalSession.get();
        if (session == null) {
            session = sessionFactory.openSession();
            threadLocalSession.set(session);
        }
        return session;
    }

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

    public interface TransactionOperation<T> {
        T execute(Session session) throws Exception;
    }
}
