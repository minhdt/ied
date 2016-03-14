package com.minhdt.common.user;

import com.minhdt.common.JSFUtils;
import com.minhdt.model.entities.SysUser;

public class SessionUser {
    private static String mBean = "userInfo";

    public SessionUser() {
        super();
    }

    public static void initMode(Long userId, Integer userLevel) {
        UserInfo userInfo = getUser();
        if(userInfo == null) 
            return;
        
        SysUser sysUser = userInfo.getUser();
        if(sysUser == null) 
            sysUser = new SysUser();
        
        sysUser.setUserId(userId);
        sysUser.setUserLevel(userLevel);
        try
        {
            setUserInfoManagedBean(userInfo);
            setUserInfoSession(userInfo);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static UserInfo getUser() {
        UserInfo user = (UserInfo)JSFUtils.getFromSession(mBean + "Session");
        return user;
    }

    public static UserInfo getUserInfoManagedBean() {
        UserInfo user = (UserInfo)JSFUtils.getManagedBeanValue(mBean);
        return user;
    }

    public static void setUserInfoManagedBean(UserInfo userInfo) {
        JSFUtils.setManagedBeanValue(mBean, userInfo);
    }

    public static UserInfo getUserInfoSession() {
        UserInfo user = (UserInfo)JSFUtils.getFromSession(mBean + "Session");
        return user;
    }

    public static void removeUserInfo() {
        JSFUtils.setManagedBeanValue(mBean, null);
        JSFUtils.setToSession(mBean + "Session", null);
    }

    public static void setUserInfoSession(UserInfo userInfo) {
        JSFUtils.setToSession(mBean + "Session", userInfo);
    }
    
    public static void setUser(SysUser user){
        Object o = JSFUtils.getManagedBeanValue("userInfo");
        UserInfo userInfo = getUser();
        userInfo.setUser(user);
        SessionUser.setUserInfoManagedBean(userInfo);
        SessionUser.setUserInfoSession(userInfo);
    }
}
