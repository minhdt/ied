package com.minhdt.model.services;

import com.minhdt.model.entities.SysParam;
import com.minhdt.model.entities.SysTask;

import java.lang.reflect.Field;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.naming.InitialContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import javax.sql.DataSource;

@Stateless(name = "SB_Common", mappedName = "IED-Model-SB_Common")
public class SB_CommonBean implements SB_CommonLocal {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "IED_Model")
    private EntityManager em;

    public SB_CommonBean() {
    }

    public EntityManager getEm() {
        return this.em;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object queryByRange(String jpqlStmt, int firstResult, int maxResults) {
        Query query = em.createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    @Override
    public <T> T persistEntity(T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public <T> T mergeEntity(T entity) {
        return em.merge(entity);
    }

    @Override
    public <T> void removeEntity(T entity) {
        em.remove(em.merge(entity));
    }

    @Override
    public <T> T findById(Class<T> entity, Object id) {
        return em.find(entity, id);
    }

    @Override
    public <T> List<T> getAllBase(Class<T> entity, Map<String, Object> filters, Map<String, Object> orders, int start,
                                  int range) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entity);
        Root<T> root = cq.from(entity);
        cq.select(root);
        cq.distinct(true);

        List<Predicate> predicates = buildConditions(filters, root, cb);

        if (predicates != null && !predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[] { }));
        }

        List<Order> listOrder = buildOrderBy(orders, cb, root);

        if (listOrder != null && !listOrder.isEmpty()) {
            cq.orderBy(listOrder.toArray(new Order[] { }));
        }

        TypedQuery<T> q = em.createQuery(cq);

        if (start > 0) {
            q.setFirstResult(start);
        }
        if (range > 0) {
            q.setMaxResults(range);
        }

        return q.getResultList();
    }
    
    @Override
    public <T> T getSingleBase(Class<T> entity, Map<String, Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entity);
        Root<T> root = cq.from(entity);
        cq.select(root);
        cq.distinct(true);

        List<Predicate> predicates = buildConditions(filters, root, cb);

        if (predicates != null && !predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[] { }));
        }

        TypedQuery<T> q = em.createQuery(cq);
        q.setMaxResults(1);
        
        try {
            return q.getSingleResult();
        } catch (Exception e) {
            
        }

        return null;
    }

    @Override
    public <T> List<T> getAllBase(Class<T> entity, Map<String, Object> filters, Map<String, Object> orders) {
        return getAllBase(entity, filters, orders, 0, 0);
    }

    protected <T> List<Predicate> buildConditions(Map<String, Object> filters, Root<T> root, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filters != null) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                Predicate predicate = buildCondition(entry, root, cb);
                if (predicate != null) {
                    predicates.add(predicate);
                }
            }
        }

        return predicates;
    }

    protected <T> Predicate buildCondition(Map.Entry<String, Object> entry, Root<T> root, CriteriaBuilder cb) {
        if (entry.getValue() instanceof String) {
            if ("NULL".equals(entry.getValue().toString().trim().toUpperCase())) {
                Expression<?> field = root.get(entry.getKey());
                return cb.isNull(field);
            } else if ("NOTNULL".equals(entry.getValue().toString().trim().toUpperCase())) {
                Expression<?> field = root.get(entry.getKey());
                return cb.isNotNull(field);
            } else {
                Expression<String> field = root.get(entry.getKey());
                return cb.like(cb.upper(field), "%" + entry.getValue().toString().trim().toUpperCase() + "%");
            }
        } else if (entry.getValue() instanceof List) {
            Expression<?> field = root.get(entry.getKey());
            return field.in((List<?>) entry.getValue());
        } else if (entry.getValue() instanceof String[]) {
            String[] condition = (String[]) entry.getValue();
            if (condition[0] != null && "EQUAL".equals(condition[0].toUpperCase())) {
                Expression<String> field = root.get(entry.getKey());
                return cb.equal(field, condition[1]);
            } else if (condition[0] != null && "NOTEQUAL".equals(condition[0].toUpperCase())) {
                Expression<String> field = root.get(entry.getKey());
                return cb.notEqual(field, condition[1]);
            } else if (condition[0] != null && "LIKE".equals(condition[0].toUpperCase())) {
                Expression<String> field = root.get(entry.getKey());
                return cb.like(cb.upper(field), condition[1].trim().toUpperCase());
            } else if (condition[0] != null && "NOTLIKE".equals(condition[0].toUpperCase())) {
                Expression<String> field = root.get(entry.getKey());
                return cb.notLike(cb.upper(field), condition[1].trim().toUpperCase());
            } else {
                return null;
            }
        } else if (entry.getValue() instanceof Date[]) {
            Date[] condition = (Date[]) entry.getValue();
            Expression<String> field = root.get(entry.getKey());
            Predicate clause = null;
            Predicate clauseStart = null;

            if (condition[0] != null && condition[1] != null) {
                clause =
                    cb.and(cb.greaterThanOrEqualTo(cb.function("TO_DATE", Date.class, field, cb.literal("yyyyMMdd")),
                                                   condition[0]),
                           cb.lessThanOrEqualTo(cb.function("TO_DATE", Date.class, field, cb.literal("yyyyMMdd")),
                                                condition[1]));
            } else if (condition[0] != null) {
                clause =
                    cb.greaterThanOrEqualTo(cb.function("TO_DATE", Date.class, field, cb.literal("yyyyMMdd")),
                                            condition[0]);
            } else if (condition[1] != null) {
                clause =
                    cb.lessThanOrEqualTo(cb.function("TO_DATE", Date.class, field, cb.literal("yyyyMMdd")),
                                         condition[1]);
            }
            return clause;
        } else {
            Expression<?> field = root.get(entry.getKey());
            return cb.equal(field, entry.getValue());
        }
    }

    protected <T> List<Order> buildOrderBy(Map<String, Object> orders, CriteriaBuilder cb, Root<T> root) {
        List<Order> listOrder = null;

        if (orders != null) {
            listOrder = new ArrayList<>();
            for (Map.Entry<String, Object> entry : orders.entrySet()) {
                Expression<?> field = root.get(entry.getKey());
                switch (entry.getValue().toString().toUpperCase()) {
                case "ASC":
                    listOrder.add(cb.asc(field));
                    break;
                case "DESC":
                    listOrder.add(cb.desc(field));
                    break;
                }
            }
        }

        return listOrder;
    }

    @Override
    public <T> void persistListEntity(List<T> listEntity) {
        int count = 0;
        for (T item : listEntity) {
            em.persist(item);
            count++;
            if ((count % 1000) == 0) {
                em.flush();
                em.clear();
            }
        }
    }

    @Override
    public <T> void removeListBase(List<T> listEntity) {
        for (T item : listEntity) {
            removeEntity(item);
        }
    }

    @Override
    public <T> void mergeListEntity(List<T> listEntity) {
        for (T item : listEntity) {
            mergeEntity(item);
        }
    }
}
