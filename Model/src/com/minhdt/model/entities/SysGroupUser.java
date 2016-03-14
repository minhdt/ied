package com.minhdt.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_GROUP_USER")
public class SysGroupUser implements Serializable {
    private static final long serialVersionUID = 63411592931734894L;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "GROUP_ID", nullable = false)
    private SysGroup sysGroup;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private SysUser sysUser;

    public SysGroupUser() {
    }

    public SysGroup getSysGroup() {
        return sysGroup;
    }

    public void setSysGroup(SysGroup sysGroup) {
        this.sysGroup = sysGroup;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }
}
