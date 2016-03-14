package com.minhdt.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DIC_ITEM_DEFINE")
public class DicItemDefine implements Serializable {
    private static final long serialVersionUID = -3245129765854801236L;
    
    @Id
    @Column(name = "ITEM_GROUP", nullable = false)
    private String itemGroup;
    
    @Id
    @Column(name = "ITEM_CODE", nullable = false)
    private String itemCode;
    
    @Column(name = "ITEM_VALUE", nullable = false)
    private String itemValue;
    
    private String description;

    public DicItemDefine() {
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
}
