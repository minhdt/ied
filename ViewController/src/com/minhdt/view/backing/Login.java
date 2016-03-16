package com.minhdt.view.backing;

import com.minhdt.common.JSFUtils;
import com.minhdt.common.Security;

import com.minhdt.common.msg.Message;
import com.minhdt.common.user.Privilege;
import com.minhdt.common.user.SessionUser;
import com.minhdt.common.user.UserInfo;
import com.minhdt.model.entities.SysGroupFunction;
import com.minhdt.model.entities.SysGroupUser;
import com.minhdt.model.entities.SysTask;
import com.minhdt.model.entities.SysUser;

import com.minhdt.model.services.SB_SystemLocal;

import com.minhdt.view.dao.DaoFactory;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.output.RichMessages;

public class Login {
    private String txtUserName;
    private String txtPassword;
    private Boolean sbcRemember;

    public void lbtLogin_Action() {
        try {
            if (getTxtUserName() == null || "".equals(getTxtUserName().trim())) {
                Message.smallWarning("Thông tin tên đăng nhập bắt buộc nhập.");
            } else if (getTxtPassword() == null || "".equals(getTxtPassword().trim())) {
                Message.smallWarning("Thông tin mật khẩu bắt buộc nhập.");
            } else {
                    String password = Security.md5(getTxtUserName().toUpperCase() + getTxtPassword().toString());
                    
                    Map<String, Object> filters = new HashMap<>();
                    String[] nameFilter = {"EQUAL", getTxtUserName()};
                    filters.put("userName", nameFilter);
                    
                    String[] passFilter = {"EQUAL", password};
                    filters.put("password", passFilter);
        
                    SysUser user = DaoFactory.getSB_SystemLocal().getSingleBase(SysUser.class, filters);
        
                    if (user != null) {
                        Object o = JSFUtils.getManagedBeanValue("userInfo");
                        UserInfo userInfo = new UserInfo();
                        userInfo.setUser(user);
                        
//                        if (user.getSysGroupUserList() != null && user.getSysGroupUserList().size() > 0) {
//                            for (SysGroupUser group : user.getSysGroupUserList()) {
//                                if (group.getSysGroup().getSysGroupFunctionList() != null && group.getSysGroup().getSysGroupFunctionList().size() > 0) {
//                                    for (SysGroupFunction groupFunction : group.getSysGroup().getSysGroupFunctionList()) {
//                                        if (("NGHIEPVU".equals(groupFunction.getSysFunction().getFunctionLevel()) &&
//                                             user.getUserLevel() == 3) ||
//                                            ("HETHONG".equals(groupFunction.getSysFunction().getFunctionLevel()) &&
//                                             (user.getUserLevel() == 1 || user.getUserLevel() == 2)) ||
//                                            "DUNGCHUNG".equals(groupFunction.getSysFunction().getFunctionLevel())) {
//                                            Privilege funcPerm = new Privilege();
//                                            if (groupFunction.getSysFunction().getPath() != null &&
//                                                !"".equals(groupFunction.getSysFunction().getPath())) {
//                                                String path = String.valueOf(groupFunction.getSysFunction().getPath());
//                                                String key = path;
//                                                if (path.lastIndexOf("/") != -1) {
//                                                    key = path.substring(path.lastIndexOf("/"));
//                                                }
//
//                                                funcPerm.setKey(key);
//                                                funcPerm.setIsPermission(true);
//
//                                                if (groupFunction.getListTask() != null &&
//                                                    groupFunction.getListTask().size() > 0) {
//                                                    for (SysTask task : groupFunction.getListTask()) {
//                                                        Privilege taskPerm = new Privilege();
//                                                        taskPerm.setKey(task.getRoleCode());
//                                                        taskPerm.setIsPermission(true);
//
//                                                        funcPerm.getMapTask().put(taskPerm.getKey(), taskPerm);
//                                                    }
//                                                }
//
//                                                userInfo.getMapPrivilege().put(funcPerm.getKey(), funcPerm);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        
                        SessionUser.setUserInfoManagedBean(userInfo);
                        SessionUser.setUserInfoSession(userInfo);
                        o = SessionUser.getUser();
                        o = SessionUser.getUserInfoSession();
                        o = SessionUser.getUserInfoManagedBean();
//                        //Write log
//                        UserInfo curUser = clsUser.getUser();
//                        DaoFactory.getSB_SystemLocal().writeSysTraceLog(curUser.getClientIp(), curUser.getCurrentView(), ActionTypes.LOGIN, curUser,
//                                             curUser.getUserId(),
//                                             "Người dùng:" + curUser.getUserName() + " truy cập hệ thống");
//                        
                        FacesContext vFacesContext = FacesContext.getCurrentInstance();
                        ExternalContext vExternalContext = vFacesContext.getExternalContext();
                        HttpServletResponse vResponse = (HttpServletResponse) vExternalContext.getResponse();
                        if (getSbcRemember()) {
                            Cookie cookie = new Cookie("CAMELS_LOGIN_COOKIE_UCODE", getTxtUserName());
                            cookie.setMaxAge(-1);
                            vResponse.addCookie(cookie);
                            cookie = new Cookie("CAMELS_LOGIN_COOKIE_REMEMBER", "1");
                            cookie.setMaxAge(-1);
                            vResponse.addCookie(cookie);
                        } else {
                            Map vRequestCookieMap = vExternalContext.getRequestCookieMap();
                            Cookie cookie = (Cookie) vRequestCookieMap.get("CAMELS_LOGIN_COOKIE_UCODE");
                            if (cookie != null) {
                                cookie.setMaxAge(0);
                                cookie.setValue("");
                                vResponse.addCookie(cookie);
                            }
                            cookie = (Cookie) vRequestCookieMap.get("CAMELS_LOGIN_COOKIE_REMEMBER");
                            if (cookie != null) {
                                cookie.setMaxAge(0);
                                cookie.setValue("");
                                vResponse.addCookie(cookie);
                            }
                        }
//                        
                        JSFUtils.redirectUrl("index");
                    } else {
                        Message.smallWarning("Thông tin người dùng không chính xác.");
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSFUtils.redirectUrl("error");
        }
    }

    public String getTxtUserName() {
        return txtUserName;
    }

    public void setTxtUserName(String txtUserName) {
        this.txtUserName = txtUserName;
    }

    public String getTxtPassword() {
        return txtPassword;
    }

    public void setTxtPassword(String txtPassword) {
        this.txtPassword = txtPassword;
    }

    public Boolean getSbcRemember() {
        return sbcRemember;
    }

    public void setSbcRemember(Boolean sbcRemember) {
        this.sbcRemember = sbcRemember;
    }
}
