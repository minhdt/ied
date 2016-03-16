package com.minhdt.view.backing.modules.sys;

import com.minhdt.common.AppUtils;

import com.minhdt.common.IEDCommon;
import com.minhdt.common.JSFUtils;
import com.minhdt.common.msg.Message;

import com.minhdt.common.user.SessionUser;

import com.minhdt.common.user.UserInfo;

import com.minhdt.model.entities.DicItemDefine;
import com.minhdt.view.dao.DaoFactory;

import com.minhdt.model.entities.SysGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import java.util.TreeMap;

import javax.annotation.PostConstruct;

import javax.faces.event.ActionEvent;

import javax.faces.model.SelectItem;

import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;

import oracle.adf.view.rich.render.ClientEvent;

public class SysGroup_List {
    private RichInputText txtName;
    private RichSelectOneChoice socStatus;
    private RichInputDate ipdCreateDateFrom;
    private RichInputDate ipdCreateDateTo;
    
    private List<SelectItem> listStatusItem;
    private List<SysGroup> listGroup;
    
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
                    listStatusItem.add(item);

                    getMapStatusItem().put(itemCode, object.getItemValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void lbtSearch_Action(ActionEvent evt) {
        try {
            setListGroup(resetModel());
            AppUtils.refreshControl(getPglTable());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String lbtAdd_Action() {
        setListGroup(null);
        return "add";
    }
    
    public String lbtEdit_Action() {
        try {
            List<SysGroup> listEdit = new ArrayList<>();
            for (SysGroup item : listGroup) {
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
            for (SysGroup item : listGroup) {
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
            List<SysGroup> listDelete = new ArrayList<>();
            for (SysGroup item : listGroup) {
                if (item.isCheck()) {
                    listDelete.add(item);
                }
            }
            
            if (listDelete.size() > 0) {
                for (SysGroup group : listDelete) {
                    group.setStatus(IEDCommon.STATUS_SUSPENDED);
                    group.setDeleteDate(new Date());
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
            setListGroup(resetModel());
        }
    }
    
    public List<SysGroup> resetModel() {
        try {
            Map<String, Object> filters = new HashMap<>();
            
            if (txtName != null && txtName.getValue() != null && !"".equals(txtName.getValue().toString().trim())) {
                filters.put("groupName", txtName.getValue().toString());
            }
            
            if (socStatus != null && socStatus.getValue() != null && !"ALL".equals(socStatus.getValue().toString().trim())) {
                filters.put("status", socStatus.getValue());
            }
            
            Date createDateFrom = null;
            Date createDateTo = null;
            
            if (ipdCreateDateFrom != null && ipdCreateDateFrom.getValue() != null && !"".equals(ipdCreateDateFrom.getValue().toString().trim())) {
                createDateFrom = (Date) ipdCreateDateFrom.getValue();
            }
            
            if (ipdCreateDateTo != null && ipdCreateDateTo.getValue() != null && !"".equals(ipdCreateDateTo.getValue().toString().trim())) {
                createDateTo = (Date) ipdCreateDateTo.getValue();
            }
            
            Date[] dateFilter = {createDateFrom, createDateTo};
            filters.put("createDate", dateFilter);
            
            Map<String, Object> orders = new TreeMap<>();
            orders.put("createDate", "ASC");
            
            return DaoFactory.getSB_SystemLocal().getAllBase(SysGroup.class, filters, orders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }


    public List<SysGroup> getListGroup() {
        if (listGroup == null) {
            listGroup = resetModel();
        }
        return listGroup;
    }

    public void setListGroup(List<SysGroup> listGroup) {
        this.listGroup = listGroup;
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

    public RichSelectOneChoice getSocStatus() {
        return socStatus;
    }

    public void setSocStatus(RichSelectOneChoice socStatus) {
        this.socStatus = socStatus;
    }

    public RichInputDate getIpdCreateDateFrom() {
        return ipdCreateDateFrom;
    }

    public void setIpdCreateDateFrom(RichInputDate ipdCreateDateFrom) {
        this.ipdCreateDateFrom = ipdCreateDateFrom;
    }

    public RichInputDate getIpdCreateDateTo() {
        return ipdCreateDateTo;
    }

    public void setIpdCreateDateTo(RichInputDate ipdCreateDateTo) {
        this.ipdCreateDateTo = ipdCreateDateTo;
    }

    public List<SelectItem> getListStatusItem() {
        return listStatusItem;
    }

    public void setListStatusItem(List<SelectItem> listStatusItem) {
        this.listStatusItem = listStatusItem;
    }

    public Map<Integer, String> getMapStatusItem() {
        return mapStatusItem;
    }

    public void setMapStatusItem(Map<Integer, String> mapStatusItem) {
        this.mapStatusItem = mapStatusItem;
    }
}
