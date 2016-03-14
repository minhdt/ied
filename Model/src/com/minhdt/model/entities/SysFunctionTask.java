package com.minhdt.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_FUNCTION_TASK")
public class SysFunctionTask implements Serializable {
    private static final long serialVersionUID = 1831250814641147485L;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "FUNCTION_ID", nullable = false)
    private SysFunction sysFunction;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "ROLE_CODE", nullable = false)
    private SysTask sysTask;

    public SysFunctionTask() {
    }

    public void setSysFunction(SysFunction sysFunction) {
        this.sysFunction = sysFunction;
    }

    public SysFunction getSysFunction() {
        return sysFunction;
    }

    public void setSysTask(SysTask sysTask) {
        this.sysTask = sysTask;
    }

    public SysTask getSysTask() {
        return sysTask;
    }
}
