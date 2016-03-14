package com.minhdt.model.entities;

import java.io.Serializable;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "SYS_GROUP")
public class SysGroup implements Serializable {
    private static final long serialVersionUID = -7759015477152197513L;
    
    @Id
    @SequenceGenerator(name = "SYS_GROUP_SEQ", sequenceName = "SYS_GROUP_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SYS_GROUP_SEQ")
    @Basic(optional = false)
    @Column(name = "GROUP_ID", nullable = false)
    private Long groupId;
    
    @Column(name = "GROUP_NAME", nullable = false)
    private String groupName;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE", nullable = false)
    private Date createDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELETE_DATE")
    private Date deleteDate;
    
    @Column(nullable = false)
    private Integer status;
    
    private String description;
    
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    
    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private SysUser sysUser;
    
    @OneToMany(mappedBy = "sysGroup", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<SysGroupUser> listSysGroupUser;
    
    @OneToMany(mappedBy = "sysGroup", cascade = { CascadeType.ALL },
               orphanRemoval = true)
    private List<SysGroupFunction> listSysGroupFunction;
    
    @Transient
    private boolean check;

    public SysGroup() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public List<SysGroupUser> getListSysGroupUser() {
        return listSysGroupUser;
    }

    public void setListSysGroupUser(List<SysGroupUser> listSysGroupUser) {
        this.listSysGroupUser = listSysGroupUser;
    }

    public List<SysGroupFunction> getListSysGroupFunction() {
        return listSysGroupFunction;
    }

    public void setListSysGroupFunction(List<SysGroupFunction> listSysGroupFunction) {
        this.listSysGroupFunction = listSysGroupFunction;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
