package com.minhdt.common.user;

import com.minhdt.common.JSFUtils;

import com.minhdt.model.entities.SysUser;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;

public class UserInfo implements Serializable {
    public UserInfo() {
        super();
    }
    
    private SysUser user;
    private String clientIp;
    private String currentView;
    
    private Map<String, Privilege> mapPrivilege;

    public static void goIndex() {
        JSFUtils.redirectUrl("MonitorDashboard");
    }

    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request =
            (HttpServletRequest)context.getExternalContext().getRequest();
        String remoteHost = request.getRemoteHost();

        JSFUtils.getCurrentSessionMap().clear();
        JSFUtils.redirectUrl("login");
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    
    public String getClientIp() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fctx.getExternalContext().getRequest();
        return request.getRemoteAddr();
    }

    public void setCurrentView(String currentView) {
        this.currentView = currentView;
    }

    public String getCurrentView() {
        return FacesContext.getCurrentInstance().getViewRoot().getViewId();
    }

    public void setMapPrivilege(Map<String, Privilege> mapPrivilege) {
        this.mapPrivilege = mapPrivilege;
    }

    public Map<String, Privilege> getMapPrivilege() {
        if (mapPrivilege == null) {
            mapPrivilege = new HashMap<>();
        }
        return mapPrivilege;
    }
    
    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    /**
     * ---------------------------------------------Start define function check permission-------------------------------------------
     */
    
    private Privilege getCurrentFunction() {
        String key = getCurrentView();
        return getCurrentFunction(key);
        
    }
    
    private Privilege getCurrentFunction(String key) {
        if (getCurrentView().lastIndexOf("/") != -1) {
            key = getCurrentView().substring(getCurrentView().lastIndexOf("/"));
        }
        
        return getMapPrivilege().get(key);
    }
    
    public boolean isFuncPerm() {
        try {
            Privilege funcPerm = getCurrentFunction();
            if (funcPerm != null)
                return funcPerm.getIsPermission();
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isFuncPerm(String key) {
        try {
            Privilege funcPerm = getCurrentFunction(key);
            if (funcPerm != null)
                return funcPerm.getIsPermission();
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isActionPerm(String action) {
        try {
            Privilege funcPerm = getCurrentFunction();
            if (funcPerm != null) {
                Privilege taskPerm = funcPerm.getMapTask().get(action);
                if (taskPerm != null) {
                    return taskPerm.getIsPermission();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
//    public boolean isAddPerm() {
//        return isActionPerm(ActionTypes.ADD);
//    }
//    
//    public boolean isApproveRequestPerm() {
//        return isActionPerm(ActionTypes.APPROVE);
//    }
//    
//    public boolean isBuildGraphPerm() {
//        return isActionPerm(ActionTypes.BUILD_GRAPH);
//    }
//    
//    public boolean isCancelPerm() {
//        return isActionPerm(ActionTypes.CANCEL);
//    }
//    
//    public boolean isCancelRejectRequestPerm() {
//        return isActionPerm(ActionTypes.CAN_01);
//    }
//    
//    public boolean isChangePassPerm() {
//        return isActionPerm(ActionTypes.CHANGE_PASS);
//    }
//    
//    public boolean isCopyPerm() {
//        return isActionPerm(ActionTypes.COPY);
//    }
//    
//    public boolean isDeclinePerm() {
//        return isActionPerm(ActionTypes.DECLINE);
//    }
//    
//    public boolean isDeletePerm() {
//        return isActionPerm(ActionTypes.DELETE);
//    }
//    
//    public boolean isEditPerm() {
//        return isActionPerm(ActionTypes.EDIT);
//    }
//    
//    public boolean isEditReportPerm() {
//        return isActionPerm(ActionTypes.EDIT_REPORT);
//    }
//    
//    public boolean isExportPerm() {
//        return isActionPerm(ActionTypes.EXPORT);
//    }
//    
//    public boolean isGrantPerm() {
//        return isActionPerm(ActionTypes.GRANT);
//    }
//    
//    public boolean isImportPerm() {
//        return isActionPerm(ActionTypes.IMPORT);
//    }
//    
//    public boolean isLockPerm() {
//        return isActionPerm(ActionTypes.LOCK);
//    }
//    
//    public boolean isLoginPerm() {
//        return isActionPerm(ActionTypes.LOGIN);
//    }
//    
//    public boolean isMiningReportPerm() {
//        return isActionPerm(ActionTypes.MINING_REPORT);
//    }
//    
//    public boolean isNextPerm() {
//        return isActionPerm(ActionTypes.NEXT);
//    }
//    
//    public boolean isPrintPerm() {
//        return isActionPerm(ActionTypes.PRINT);
//    }
//    
//    public boolean isQueryPerm() {
//        return isActionPerm(ActionTypes.QUERY);
//    }
//    
//    public boolean isReceiveFilePerm() {
//        return isActionPerm(ActionTypes.RECEIVE_FILE);
//    }
//    
//    public boolean isReturnPerm() {
//        return isActionPerm(ActionTypes.RETURN);
//    }
//    
//    public boolean isSelectPerm() {
//        return isActionPerm(ActionTypes.SELECT);
//    }
//    
//    public boolean isSendReportPerm() {
//        return isActionPerm(ActionTypes.SEND_REPORT);
//    }
//    
//    public boolean isSettingPerm() {
//        return isActionPerm(ActionTypes.SETTING);
//    }
//    
//    public boolean isShareReportPerm() {
//        return isActionPerm(ActionTypes.SHARE_REPORT);
//    }
//    
//    public boolean isStopPerm() {
//        return isActionPerm(ActionTypes.STOP);
//    }
//    
//    public boolean isUnlockPerm() {
//        return isActionPerm(ActionTypes.UN_LOCK);
//    }
//    
//    public boolean isViewPerm() {
//        return isActionPerm(ActionTypes.VIEW);
//    }

    /**
     * ---------------------------------------------End define function check permission-------------------------------------------
     */
}
