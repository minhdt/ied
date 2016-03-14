package com.minhdt.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_PARAM")
public class SysParam implements Serializable {
    private static final long serialVersionUID = -3656577663020405004L;
    
    @Id
    @Column(name = "PARAM_CODE", nullable = false)
    private String paramCode;
    
    @Column(name = "PARAM_NAME", nullable = false)
    private String paramName;
    
    @Column(name = "PARAM_VALUE", nullable = false)
    private String paramValue;
    
    @Column(name = "PARAM_FORMAT", nullable = false)
    private Integer paramFormat;
    
    private String description;
    
    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    public SysParam() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParamFormat() {
        return paramFormat;
    }

    public void setParamFormat(Integer paramFormat) {
        this.paramFormat = paramFormat;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }
}
