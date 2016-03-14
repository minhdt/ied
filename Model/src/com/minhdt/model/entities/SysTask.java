package com.minhdt.model.entities;

import java.io.Serializable;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "SYS_TASK")
public class SysTask implements Serializable {
    private static final long serialVersionUID = 288094613409331185L;
    
    @Id
    @Column(name = "ROLE_CODE", nullable = false)
    private String roleCode;
    
    @Column(name = "ROLE_NAME", nullable = false)
    private String roleName;
    
    @Column(nullable = false)
    private Integer status;
    
    private String description;
    
    private Integer loggable;
    
    @Transient
    private boolean check;
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.roleCode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SysTask other = (SysTask) obj;
        if (!Objects.equals(this.roleCode, other.roleCode)) {
            return false;
        }
        return true;
    }
    
    public SysTask() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLoggable() {
        return loggable;
    }

    public void setLoggable(Integer loggable) {
        this.loggable = loggable;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
