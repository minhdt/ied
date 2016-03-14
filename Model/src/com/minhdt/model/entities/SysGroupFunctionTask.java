package com.minhdt.model.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_GROUP_FUNCTION_TASK")
public class SysGroupFunctionTask implements Serializable {
    private static final long serialVersionUID = -2523536379641523326L;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "GROUP_FUNCTION_ID", nullable = false)
    private SysGroupFunction sysGroupFunction;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "ROLE_CODE", nullable = false)
    private SysTask sysTaks;

    public SysGroupFunctionTask() {
    }

    public SysGroupFunction getSysGroupFunction() {
        return sysGroupFunction;
    }

    public void setSysGroupFunction(SysGroupFunction sysGroupFunction) {
        this.sysGroupFunction = sysGroupFunction;
    }

    public SysTask getSysTaks() {
        return sysTaks;
    }

    public void setSysTaks(SysTask sysTaks) {
        this.sysTaks = sysTaks;
    }
}
