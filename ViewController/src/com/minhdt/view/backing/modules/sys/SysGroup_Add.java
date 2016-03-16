package com.minhdt.view.backing.modules.sys;

import com.minhdt.common.IEDCommon;
import com.minhdt.common.JSFUtils;
import com.minhdt.common.msg.Message;
import com.minhdt.common.user.SessionUser;
import com.minhdt.common.user.UserInfo;
import com.minhdt.model.entities.SysFunction;
import com.minhdt.model.entities.SysGroup;

import com.minhdt.model.entities.SysGroupFunction;
import com.minhdt.model.entities.SysTask;
import com.minhdt.view.dao.DaoFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import javax.annotation.PostConstruct;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.data.RichListView;
import oracle.adf.view.rich.component.rich.input.RichInputText;

public class SysGroup_Add {
    private RichInputText txtName;
    private RichInputText txtDescription;
    
    private List<SysFunction> listFunction;
    
    private UserInfo curUser;
    
    @PostConstruct
    public void initFlow() {
        try {
            curUser = SessionUser.getUser();

            if (curUser == null || curUser.getUser() == null) {
                JSFUtils.redirectUrl("login");
                return;
            }
            if (curUser.getUser() != null && !(curUser.getUser().getUserName().toUpperCase()).equals("SUPERVISOR")) {
                if (!curUser.isFuncPerm()) {
                    JSFUtils.redirectUrl("index");
                }
            }
        } catch (Exception ex) {
            JSFUtils.redirectUrl("login");
            return;
        }
        
        try {
            Map<String, Object> filters = new HashMap<>();
            filters.put("status", IEDCommon.STATUS_ACTIVE);

            setListFunction(DaoFactory.getSB_SystemLocal().getAllBase(SysFunction.class, filters, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String lbtAdd_Action() {
        try {
            if (validate()) {
                SysGroup current = new SysGroup();
                current.setGroupName(txtName.getValue().toString().trim());
                current.setCreateDate(new Date());
                current.setStatus(IEDCommon.STATUS_ACTIVE);
                current.setUserId(curUser.getUser().getUserId());
                
                if (txtDescription != null && txtDescription.getValue() != null && !"".equals(txtDescription.getValue().toString().trim())) {
                    current.setDescription(txtDescription.getValue().toString().trim());
                }
                
                for (SysFunction func : listFunction) {
                    if (func.isCheck()) {
                        SysGroupFunction object = new SysGroupFunction();
                        object.setSysFunction(func);
                        object.setSysGroup(current);
                        
                        for (SysTask task : func.getListTask()) {
                            if (func.getMapTask().get(task.getRoleCode())) {
                                object.getListTask().add(task);
                            }
                        }
                        
                        current.getListSysGroupFunction().add(object);
                    }
                }
                
                current = DaoFactory.getSB_SystemLocal().persistEntity(current);
                
                Message.smallSuccess("Thêm mới nhóm quyền thành công.");
                
                return "back";
            }
        } catch (Exception e) {
            Message.smallError("Thêm mới nhóm quyền không thành công.");
            e.printStackTrace();
        }
        
        return null;
    }
    
    private boolean validate() {
        try {
            if (txtName == null || txtName.getValue() == null || "".equals(txtName.getValue().toString().trim())) {
                Message.smallWarning("Thông tin tên nhóm quyền bắt buộc nhập.");
                return false;
            }
            
            Map<String, Object> filters = new HashMap<>();
            String[] name = {"EQUAL", txtName.getValue().toString().trim()};
            filters.put("groupName", name);
            
            List<SysGroup> listGroup = DaoFactory.getSB_SystemLocal().getAllBase(SysGroup.class, filters, null);
            if (listGroup != null && listGroup.size() > 0) {
                Message.smallWarning("Tên nhóm quyền đã được sử dụng.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    public List<SysFunction> getListFunction() {
        return listFunction;
    }

    public void setListFunction(List<SysFunction> listFunction) {
        this.listFunction = listFunction;
    }

    public UserInfo getCurUser() {
        return curUser;
    }

    public void setCurUser(UserInfo curUser) {
        this.curUser = curUser;
    }

    public RichInputText getTxtName() {
        return txtName;
    }

    public void setTxtName(RichInputText txtName) {
        this.txtName = txtName;
    }

    public RichInputText getTxtDescription() {
        return txtDescription;
    }

    public void setTxtDescription(RichInputText txtDescription) {
        this.txtDescription = txtDescription;
    }
}
