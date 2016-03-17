package com.minhdt.model.services;

import com.minhdt.model.entities.SysParam;
import com.minhdt.model.entities.SysTask;

import com.minhdt.model.services.common.FilterModel;

import java.sql.SQLException;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface SB_CommonLocal {
    Object queryByRange(String jpqlStmt, int firstResult, int maxResults);

    <T> T persistEntity(T entity);

    <T> T mergeEntity(T entity);

    <T> void removeEntity(T entity);

    <T> T findById(Class<T> entity, Object id);

    <T> List<T> getAllBase(Class<T> entity, Map<String, Object> filters, Map<String, Object> orders, int start,
                           int range);

    <T> List<T> getAllBase(Class<T> entity, List<FilterModel> filters, Map<String, Object> orders, int start,
                           int range);

    <T> List<T> getAllBase(Class<T> entity, Map<String, Object> filters, Map<String, Object> orders);

    <T> List<T> getAllBase(Class<T> entity, List<FilterModel> filters, Map<String, Object> orders);

    <T> T getSingleBase(Class<T> entity, Map<String, Object> filters);

    <T> void persistListEntity(List<T> listEntity);

    <T> void removeListBase(List<T> listEntity);

    <T> void mergeListEntity(List<T> listEntity);
}
