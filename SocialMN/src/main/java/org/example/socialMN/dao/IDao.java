package org.example.socialMN.dao;


import org.example.socialMN.model.User;

import java.util.List;
import java.util.Map;

public interface IDao {

    boolean executeQueryForValidation(String hql, Map<String, Object> parameters);

    <T> void save(T model);

    <T> List<T> executeHqlQuery(String hql, Class<T> modelClass, Map<String, Object> parameters);

    <T> T executeHqlQuerySingleResult(String hql, Class<T> modelClass, Map<String, Object> parameters);

    <T> void executeHqlUpdate(String hql, Class<T> modelClass, Map<String, Object> parameters);

    <T> boolean existsByField(Class<T> entityClass, String fieldName, Object value);

    <T> void merge(T entity);


}
