package com.minhdt.view.backing.modules.sys;

import com.minhdt.common.AppUtils;
import com.minhdt.common.IEDCommon;
import com.minhdt.common.JSFUtils;
import com.minhdt.common.msg.Message;
import com.minhdt.common.user.SessionUser;
import com.minhdt.common.user.UserInfo;

import com.minhdt.model.entities.DicItemDefine;
import com.minhdt.model.entities.SysGroup;
import com.minhdt.model.entities.SysUser;

import com.minhdt.model.services.common.ExpressionFilter;
import com.minhdt.model.services.common.FilterModel;
import com.minhdt.view.dao.DaoFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.TreeMap;

import javax.annotation.PostConstruct;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.render.ClientEvent;

public class SysUser_List {
    private RichInputText txtUserName;
    private RichInputText txtFullName;
    private RichSelectOneChoice socStatus;
    
    private List<SelectItem> listStatusItem;
    private List<SysUser> listUser;
    
    private Map<Integer, String> mapStatusItem;
    
    private UserInfo curUser;
    
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
            Map<String, Object> filters = new HashMap<>();
            String[] statusFilter = {"EQUAL", "STATUS"};
            filters.put("itemGroup", statusFilter);
            
            List<DicItemDefine> listItem = DaoFactory.getSB_SystemLocal().getAllBase(DicItemDefine.class, filters, null);
            if (listItem != null && listItem.size() > 0) {
                listStatusItem = new ArrayList<>();
                mapStatusItem = new HashMap<>(); 
                for (DicItemDefine object : listItem) {
                    Integer itemCode = Integer.valueOf(object.getItemCode());
                    
                    SelectItem item = new SelectItem();
                    item.setLabel(object.getItemValue());
                    item.setValue(itemCode);
                    getListStatusItem().add(item);

                    getMapStatusItem().put(itemCode, object.getItemValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void lbtSearch_Action(ActionEvent evt) {
        try {
            setListUser(resetModel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String lbtAdd_Action() {
        return "add";
    }
    
    public String lbtEdit_Action() {
        try {
            List<SysUser> listEdit = new ArrayList<>();
            for (SysUser item : listUser) {
                if (item.isCheck()) {
                    listEdit.add(item);
                }
            }
            if (listEdit.size() != 1) {
                Message.smallWarning("Yêu cầu chọn một báo cáo để chỉnh sửa.");
            } else {
                AppUtils.setPageFlowValue("EditItem", listEdit.get(0));
                return "edit";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void lbtDelete_Action(ActionEvent evt) {
        try {
            int count = 0;
            for (SysUser item : listUser) {
                if (item.isCheck()) {
                    count++;
                }
            }
            if (count == 0) {
                Message.smallWarning("Yêu cầu chọn một báo cáo để chỉnh sửa.");
            } else {
                String controlId = evt.getComponent().getClientId();

                Message.confirmBox("Thông báo!", "Xác nhận xóa nhóm quyền?", controlId, "handleConfirmDelete");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void handleConfirmDelete_action(ClientEvent ce) {
        try {
            List<SysUser> listDelete = new ArrayList<>();
            for (SysUser item : listUser) {
                if (item.isCheck()) {
                    listDelete.add(item);
                }
            }
            
            if (listDelete.size() > 0) {
                for (SysUser user : listDelete) {
                    user.setStatus(IEDCommon.STATUS_SUSPENDED);
                    user.setDeleteDate(new Date());
                }
                
                DaoFactory.getSB_SystemLocal().mergeListEntity(listDelete);
                
                Message.smallSuccess("Xóa nhóm quyền thành công.");
            } else {
                Message.smallWarning("Chọn nhóm quyền trước khi xóa.");
            }
        } catch (Exception e) {
            Message.smallError("Xóa nhóm quyền không thành công.");
            e.printStackTrace();
        } finally {
            setListUser(resetModel());
        }
    }
    
    public List<SysUser> resetModel() {
        try {
            List<FilterModel> filters = new ArrayList<>();
            
            if (txtFullName != null && txtFullName.getValue() != null && !"".equals(txtFullName.getValue().toString().trim())) {
                FilterModel filter = new FilterModel();
                filter.setColumn("fullName");
                filter.setExpression(ExpressionFilter.LIKE);
                filter.setCondition("%" + txtFullName.getValue().toString().trim().toUpperCase() + "%");
            }
            
            if (txtUserName != null && txtUserName.getValue() != null && !"".equals(txtUserName.getValue().toString().trim())) {
                FilterModel filter = new FilterModel();
                filter.setColumn("userName");
                filter.setExpression(ExpressionFilter.LIKE);
                filter.setCondition("%" + txtUserName.getValue().toString().trim().toUpperCase() + "%");
            }
            
            if (socStatus != null && socStatus.getValue() != null && !"ALL".equals(socStatus.getValue().toString().trim())) {
                FilterModel filter = new FilterModel();
                filter.setColumn("status");
                filter.setExpression(ExpressionFilter.EQUAL);
                filter.setCondition(socStatus.getValue());
            }
            
            if (curUser.getUser().getUserId().intValue() == 0) {
                FilterModel filter = new FilterModel();
                filter.setColumn("userId");
                filter.setExpression(ExpressionFilter.NOT_EQUAL);
                filter.setCondition(curUser.getUser().getUserId());
            } else {
                FilterModel filter = new FilterModel();
                filter.setColumn("manager");
                filter.setExpression(ExpressionFilter.EQUAL);
                filter.setCondition(curUser.getUser());
            }
            
            Map<String, Object> orders = new TreeMap<>();
            orders.put("fullName", "ASC");
            
            return DaoFactory.getSB_SystemLocal().getAllBase(SysUser.class, filters, orders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public RichInputText getTxtUserName() {
        return txtUserName;
    }

    public void setTxtUserName(RichInputText txtUserName) {
        this.txtUserName = txtUserName;
    }

    public RichInputText getTxtFullName() {
        return txtFullName;
    }

    public void setTxtFullName(RichInputText txtFullName) {
        this.txtFullName = txtFullName;
    }

    public RichSelectOneChoice getSocStatus() {
        return socStatus;
    }

    public void setSocStatus(RichSelectOneChoice socStatus) {
        this.socStatus = socStatus;
    }

    public List<SelectItem> getListStatusItem() {
        return listStatusItem;
    }

    public void setListStatusItem(List<SelectItem> listStatusItem) {
        this.listStatusItem = listStatusItem;
    }

    public List<SysUser> getListUser() {
        if (listUser == null) {
            listUser = resetModel();
        }
        return listUser;
    }

    public void setListUser(List<SysUser> listUser) {
        this.listUser = listUser;
    }

    public Map<Integer, String> getMapStatusItem() {
        return mapStatusItem;
    }

    public void setMapStatusItem(Map<Integer, String> mapStatusItem) {
        this.mapStatusItem = mapStatusItem;
    }

    public UserInfo getCurUser() {
        return curUser;
    }

    public void setCurUser(UserInfo curUser) {
        this.curUser = curUser;
    }
}
