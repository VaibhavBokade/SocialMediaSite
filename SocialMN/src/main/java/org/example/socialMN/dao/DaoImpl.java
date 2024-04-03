package org.example.socialMN.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DaoImpl implements IDao {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Executes an HQL query for validation, user is registered or not.
     * hql        The HQL query to be executed for validation.
     * parameters A Map containing parameters to be set in the query.
     * return True if the count of results is greater than 0, indicating user exist; otherwise, false.
     */
    @Override
    public boolean executeQueryForValidation(String hql, Map<String, Object> parameters) {
        Session session = sessionFactory.openSession();
        Query<Long> query = session.createQuery(hql, Long.class);

        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        Long count = query.uniqueResult();
        return count != null && count > 0;
    }

    /**
     * Executes an HQL query and returns a list of results based on the provided parameters.
     * hql        The HQL query to be executed.
     * modelClass The class representing the model for which the query is executed.
     * parameters A Map containing parameters to be set in the query.
     * <p>
     * return A list of results based on the executed HQL query.
     */
    @Override
    public <T> List<T> executeHqlQuery(String hql, Class<T> modelClass, Map<String, Object> parameters) {

        Session session = sessionFactory.openSession();
        Query<T> query = session.createQuery(hql, modelClass);
        if (null != parameters && !parameters.isEmpty()) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                query.setParameter(key, value);
            }
        }
        return query.list();
    }

    /**
     * Executes an HQL query and returns a single result based on the provided parameters.
     * <p>
     * hql        The HQL query to be executed.
     * modelClass The class representing the model for which the query is executed.
     * parameters A Map containing parameters to be set in the query.
     * return A single result based on the executed HQL query.
     */
    @Override
    public <T> T executeHqlQuerySingleResult(String hql, Class<T> modelClass, Map<String, Object> parameters) {
        Session session = sessionFactory.openSession();
        try {
            Query<T> query = session.createQuery(hql, modelClass);
            if (null != parameters && !parameters.isEmpty()) {
                parameters.forEach((key, value) -> query.setParameter(key, value));
            }
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public <T> void executeHqlUpdate(String sql, Class<T> modelClass, Map<String, Object> parameters) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            NativeQuery<?> query = session.createNativeQuery(sql);
            if (parameters != null) {
                parameters.forEach((key, value) -> query.setParameter(key, value));
            }
            int rowsAffected = query.executeUpdate();   
            if (rowsAffected > 0) {
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //     Checks if a record with the specified field value exists in the database.
    @Override
    public <T> boolean existsByField(Class<T> entityClass, String fieldName, Object value) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(e) FROM " + entityClass.getName() + " e WHERE e." + fieldName + " = :value";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("value", value);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Saves a model entity to the database.
     * model The model entity to be saved.
     */
    @Override
    public <T> void save(T model) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(model);
        transaction.commit();
        session.close();
    }

    @Override
    public <T> void merge(T entity) {
        Session session = sessionFactory.openSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}



