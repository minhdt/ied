package com.minhdt.model.services;

import com.minhdt.model.entities.SysUser;

import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless(name = "SB_System", mappedName = "IED-Model-SB_System")
public class SB_SystemBean extends SB_CommonBean implements SB_SystemLocal {

    public SB_SystemBean() {
    }
    
    public List<SysUser> getAllUser() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o FROM SysUser o");
        
        Query q = getEm().createQuery(sql.toString());
        
        return q.getResultList();
    }
}
