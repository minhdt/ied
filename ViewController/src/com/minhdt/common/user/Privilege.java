package com.minhdt.common.user;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

public class Privilege implements Serializable {
    public Privilege() {
        super();
    }

    private String key;

    private Boolean isPermission;

    private Map<String, Privilege> mapTask;


    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setIsPermission(Boolean isPermission) {
        this.isPermission = isPermission;
    }

    public Boolean getIsPermission() {
        return isPermission;
    }

    public void setMapTask(Map<String, Privilege> mapTask) {
        this.mapTask = mapTask;
    }

    public Map<String, Privilege> getMapTask() {
        if (mapTask == null) {
            mapTask = new HashMap<>();
        }
        return mapTask;
    }
}

