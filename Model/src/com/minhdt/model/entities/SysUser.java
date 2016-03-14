package com.minhdt.model.entities;

import java.io.Serializable;

import java.util.Date;

import java.util.List;

import java.util.Objects;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "SYS_USER")
public class SysUser implements Serializable {
    private static final long serialVersionUID = 3018199733633174166L;
    @Id
    @SequenceGenerator(name = "SYS_USER_SEQ", sequenceName = "SYS_USER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SYS_USER_SEQ")
    @Basic(optional = false)
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    
    @Column(name = "USER_NAME", nullable = false)
    private String userName;
    
    @Column(name = "FULL_NAME", nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "USER_LEVEL", nullable = false)
    private Integer userLevel;
    
    private Integer gender;
    
    @Column(name = "USER_IMAGE")
    private String userImage;
    
    @Column(nullable = false)
    private Integer status;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE", nullable = false)
    private Date createDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELETE_DATE")
    private Date deleteDate;
    
    @ManyToOne
    @JoinColumn(name = "MANAGER_ID")
    private SysUser manager;
    
    @OneToMany(mappedBy = "sysUser", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<SysGroupUser> listSysGroupUser;
    
    @OneToMany(mappedBy = "sysUser", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<SysGroup> listSysGroup;
    
    @Transient
    private boolean check;
    
    public SysUser() {
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.userId);
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
        final SysUser other = (SysUser) obj;
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        return true;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SysUser getManager() {
        return manager;
    }

    public void setManager(SysUser manager) {
        this.manager = manager;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public List<SysGroupUser> getListSysGroupUser() {
        return listSysGroupUser;
    }

    public void setListSysGroupUser(List<SysGroupUser> listSysGroupUser) {
        this.listSysGroupUser = listSysGroupUser;
    }

    public List<SysGroup> getListSysGroup() {
        return listSysGroup;
    }

    public void setListSysGroup(List<SysGroup> listSysGroup) {
        this.listSysGroup = listSysGroup;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
