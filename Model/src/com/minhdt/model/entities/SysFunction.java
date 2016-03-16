package com.minhdt.model.entities;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "SYS_FUNCTION")
public class SysFunction implements Serializable {
    private static final long serialVersionUID = 3742909189274677535L;
    
    @Id
    @SequenceGenerator(name = "SYS_FUNCTION_SEQ", sequenceName = "SYS_FUNCTION_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SYS_FUNCTION_SEQ")
    @Basic(optional = false)
    @Column(name = "FUNCTION_ID", nullable = false)
    private Long functionId;
    
    @Column(name = "FUNCTION_NAME", nullable = false)
    private String functionName;
    
    @Column(name = "SHORT_NAME", nullable = false)
    private String shortName;
    
    @Column(name = "PARENT_ID")
    private Long parentId;
    
    private String icon;
    
    private String description;
    
    private String path;
    
    private Integer status;
    
    @ManyToMany(targetEntity = SysTask.class)
    @JoinTable(name = "SYS_FUNCTION_TASK", joinColumns = @JoinColumn(name = "FUNCTION_ID"),
               inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"))
    private List<SysTask> listTask;
    
    @ManyToOne
    @JoinColumn(name = "PARENT_ID", insertable = false, updatable = false)
    private SysFunction parent;
    
    @OneToMany(mappedBy = "sysFunction")
    private List<SysGroupFunction> listSysGroupFunction;
    
    @Transient
    private boolean check;
    
    @Transient
    private Map<String, Boolean> mapTask;

    public SysFunction() {
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.functionId);
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
        final SysFunction other = (SysFunction) obj;
        if (!Objects.equals(this.functionId, other.functionId)) {
            return false;
        }
        return true;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    

    public SysFunction getParent() {
        return parent;
    }

    public void setParent(SysFunction parent) {
        this.parent = parent;
    }

    public List<SysGroupFunction> getListSysGroupFunction() {
        return listSysGroupFunction;
    }

    public void setListSysGroupFunction(List<SysGroupFunction> listSysGroupFunction) {
        this.listSysGroupFunction = listSysGroupFunction;
    }

    public List<SysTask> getListTask() {
        if (listTask == null) {
            listTask = new ArrayList<>();
        }
        return listTask;
    }

    public void setListTask(List<SysTask> listTask) {
        this.listTask = listTask;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map<String, Boolean> getMapTask() {
        if (mapTask == null) {
            mapTask = new HashMap<>();
            for (SysTask task : getListTask()) {
                mapTask.put(task.getRoleCode(), new Boolean(false));
            }
        }
        return mapTask;
    }

    public void setMapTask(Map<String, Boolean> mapTask) {
        this.mapTask = mapTask;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
