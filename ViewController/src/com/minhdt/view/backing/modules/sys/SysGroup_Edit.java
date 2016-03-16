package com.minhdt.view.backing.modules.sys;

import com.minhdt.common.AppUtils;
import com.minhdt.common.IEDCommon;
import com.minhdt.common.JSFUtils;
import com.minhdt.common.msg.Message;
import com.minhdt.common.user.SessionUser;
import com.minhdt.common.user.UserInfo;
import com.minhdt.model.entities.DicItemDefine;
import com.minhdt.model.entities.SysFunction;
import com.minhdt.model.entities.SysGroup;

import com.minhdt.model.entities.SysGroupFunction;
import com.minhdt.model.entities.SysTask;
import com.minhdt.view.dao.DaoFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import java.util.concurrent.Exchanger;

import javax.annotation.PostConstruct;

import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.data.RichListView;

public class SysGroup_Edit {
    private List<SelectItem> listStatusItem;
    private List<SysFunction> listFunction;
    
    private UserInfo curUser;
    private SysGroup current;
    
    private String lastName;
    
    @PostConstruct
    public void initFlow() {
        try {
            setCurUser(SessionUser.getUser());

            if (getCurUser() == null || getCurUser().getUser() == null) {
                JSFUtils.redirectUrl("login");
                return;
            }
            if (getCurUser().getUser() != null && !(getCurUser().getUser().getUserName().toUpperCase()).equals("SUPERVISOR")) {
                if (!getCurUser().isFuncPerm()) {
                    JSFUtils.redirectUrl("index");
                }
            }
        } catch (Exception ex) {
            JSFUtils.redirectUrl("login");
            return;
        }
        
        try {
            if (AppUtils.getPageFlowValue("EditItem") != null) {
                setCurrent((SysGroup) AppUtils.getPageFlowValue("EditItem"));
                AppUtils.setPageFlowValue("EditItem", null);

                setLastName(getCurrent().getGroupName());
                
                Map<String, Object> filters = new HashMap<>();
                filters.put("status", IEDCommon.STATUS_ACTIVE);

                setListFunction(DaoFactory.getSB_SystemLocal().getAllBase(SysFunction.class, filters, null));
                
                if (getListFunction() != null && getListFunction().size() > 0) {
                    for (SysFunction function : getListFunction()) {
                        SysGroupFunction groupFunction = getCurrent().getMapFunction().get(function.getFunctionId());
                        if (groupFunction != null) {
                            function.setCheck(true);
                            
                            for (Map.Entry<String, Boolean> entry : groupFunction.getMapTask().entrySet()) {
                                function.getMapTask().put(entry.getKey(), new Boolean(true));
                            }
                        }
                    }
                }
                
                filters = new HashMap<>();
                String[] statusFilter = {"EQUAL", "STATUS"};
                filters.put("itemGroup", statusFilter);
                
                List<DicItemDefine> listItem = DaoFactory.getSB_SystemLocal().getAllBase(DicItemDefine.class, filters, null);
                if (listItem != null && listItem.size() > 0) {
                    listStatusItem = new ArrayList<>();
                    for (DicItemDefine object : listItem) {
                        SelectItem item = new SelectItem();
                        item.setLabel(object.getItemValue());
                        item.setValue(Integer.valueOf(object.getItemCode()));
                        listStatusItem.add(item);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String lbtEdit_Action() {
        try {
            if (validate()) {
                getCurrent().setUserId(getCurUser().getUser().getUserId());

                getCurrent().getListSysGroupFunction().clear();
                
                for (SysFunction func : getListFunction()) {
                    if (func.isCheck()) {
                        SysGroupFunction object = new SysGroupFunction();
                        object.setSysFunction(func);
                        object.setSysGroup(getCurrent());
                        
                        
                        for (SysTask task : func.getListTask()) {
                            if (func.getMapTask().get(task.getRoleCode())) {
                                object.getListTask().add(task);
                            }
                        }

                        getCurrent().getListSysGroupFunction().add(object);
                    }
                }

                setCurrent(DaoFactory.getSB_SystemLocal().mergeEntity(getCurrent()));
                
                Message.smallSuccess("Chỉnh sửa nhóm quyền thành công.");
                
                return "back";
            }
        } catch (Exception e) {
            Message.smallError("Chỉnh sửa nhóm quyền không thành công.");
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean validate() {
        try {
            if (getCurrent().getGroupName() == null || "".equals(getCurrent().getGroupName())) {
                Message.smallWarning("Thông tin tên nhóm quyền bắt buộc nhập.");
                return false;
            }
            
            if (!getCurrent().getGroupName().equals(getLastName())) {
                Map<String, Object> filters = new HashMap<>();
                String[] name = {"EQUAL", getCurrent().getGroupName().toString().trim()};
                filters.put("groupName", name);
                
                List<SysGroup> listGroup = DaoFactory.getSB_SystemLocal().getAllBase(SysGroup.class, filters, null);
                if (listGroup != null && listGroup.size() > 0) {
                    Message.smallWarning("Tên nhóm quyền đã được sử dụng.");
                    return false;
                }
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

    public SysGroup getCurrent() {
        return current;
    }

    public void setCurrent(SysGroup current) {
        this.current = current;
    }

    public List<SelectItem> getListStatusItem() {
        return listStatusItem;
    }

    public void setListStatusItem(List<SelectItem> listStatusItem) {
        this.listStatusItem = listStatusItem;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
