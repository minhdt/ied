package com.minhdt.model.entities;

import java.io.Serializable;

import java.util.List;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * To create ID generator sequence "SYS_GROUP_FUNCTION_SEQ":
 * CREATE SEQUENCE "SYS_GROUP_FUNCTION_SEQ" INCREMENT BY 50 START WITH 50;
 */
@Entity
@Table(name = "SYS_GROUP_FUNCTION")
public class SysGroupFunction implements Serializable {
    private static final long serialVersionUID = 8358878529183535766L;

    @Id
    @SequenceGenerator(name = "SYS_GROUP_FUNCTION_SEQ", sequenceName = "SYS_GROUP_FUNCTION_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SYS_GROUP_FUNCTION_SEQ")
    @Basic(optional = false)
    @Column(name = "GROUP_FUNCTION_ID", nullable = false)
    private Long groupFunctionId;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID", nullable = false)
    private SysGroup sysGroup;

    @ManyToOne
    @JoinColumn(name = "FUNCTION_ID", nullable = false)
    private SysFunction sysFunction;

    @ManyToMany(targetEntity = SysTask.class)
    @JoinTable(name = "SYS_GROUP_FUNCTION_TASK", joinColumns = @JoinColumn(name = "GROUP_FUNCTION_ID"),
               inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"))
    private List<SysTask> listTask;

    public SysGroupFunction() {
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.groupFunctionId);
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
        final SysGroupFunction other = (SysGroupFunction) obj;
        if (!Objects.equals(this.groupFunctionId, other.groupFunctionId)) {
            return false;
        }
        return true;
    }

    public Long getGroupFunctionId() {
        return groupFunctionId;
    }

    public void setGroupFunctionId(Long groupFunctionId) {
        this.groupFunctionId = groupFunctionId;
    }

    public SysGroup getSysGroup() {
        return sysGroup;
    }

    public void setSysGroup(SysGroup sysGroup) {
        this.sysGroup = sysGroup;
    }

    public SysFunction getSysFunction() {
        return sysFunction;
    }

    public void setSysFunction(SysFunction sysFunction) {
        this.sysFunction = sysFunction;
    }

    public List<SysTask> getListTask() {
        return listTask;
    }

    public void setListTask(List<SysTask> listTask) {
        this.listTask = listTask;
    }
}
