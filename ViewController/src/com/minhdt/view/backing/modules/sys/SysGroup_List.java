package com.minhdt.view.backing.modules.sys;

import apple.applescript.AppleScriptEngine;

import com.minhdt.common.AppUtils;

import com.minhdt.common.msg.Message;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.component.rich.data.RichListItem;
import oracle.adf.view.rich.component.rich.data.RichListView;
import oracle.adf.view.rich.component.rich.layout.RichGridCell;
import oracle.adf.view.rich.component.rich.layout.RichGridRow;
import oracle.adf.view.rich.component.rich.layout.RichPanelGridLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;

import oracle.adf.view.rich.render.ClientEvent;

import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class SysGroup_List {
    private List<String[]> demoData;
    
    private String[] current;

    @PostConstruct
    public void initFlow() {
        demoData = new ArrayList<>();

        String[] data1 = { "Shona", "Woldt", "3 Oct 1981", "1" };
        demoData.add(data1);

        String[] data2 = { "Shona2", "Woldt", "2 Oct 1981", "0" };
        demoData.add(data2);

        String[] data3 = { "Shona3", "Woldt", "1 Oct 1981", "-1" };
        demoData.add(data3);
    }

    public void lbtSearch_Action(ActionEvent evt) {
        StringBuilder script = new StringBuilder();
        script.append( "$('#sysGroupData').empty();");
        
        demoData = new ArrayList<>();

        String[] data1 = { "Shona", "Woldt", "3 Oct 1981", "1" };
        demoData.add(data1);
        
        String[] data2 = { "Shona2", "Woldt", "2 Oct 1981", "0" };
        demoData.add(data2);
    }
    
    public String lbtAdd_Action() {
        return "add";
    }
    
    public void lbtEdit_Action(ActionEvent evt) {
        try {
            Object[] selected = (Object[]) evt.getComponent().getAttributes().get("SelectedRowData");
            if (selected != null) {
                current = (String[]) selected;
            } else {
                Message.smallWarning("Chọn nhóm quyền trước khi thực hiện sửa.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String lbtEdit_Action() {
        if (current == null) {
            Message.smallWarning("Chọn nhóm quyền trước khi thực hiện sửa.");
        } else {
            AppUtils.setPageFlowValue("EditItem", current);
            return "edit";
        }
        return null;
    }
    
    public void lbtDelete_Action(ActionEvent evt) {
        try {
            String[] selected = (String[]) evt.getComponent().getAttributes().get("SelectedRowData");
            if (selected == null) {
                Message.smallWarning("Chọn nhóm quyền trước khi thực hiện xoá.");
            } else {
                current = (String[]) selected;
                String controlId = evt.getComponent().getClientId();

                Message.confirmBox("Thông báo!", "Xác nhận xóa nhóm quyền?", controlId, "handleConfirmDelete");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void handleConfirmDelete_action(ClientEvent ce) {
        try {
            if (current != null) {
                Message.smallSuccess("Xóa nhóm quyền thành công.");
            } else {
                Message.smallWarning("Chọn nhóm quyền trước khi xóa.");
            }
        } catch (Exception e) {
            Message.smallError("Xóa nhóm quyền không thành công.");
            e.printStackTrace();
        } finally {
        }
    }

    private void writeJavaScriptToClient(String script) {
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExtendedRenderKitService erks = Service.getRenderKitService(fctx, ExtendedRenderKitService.class);
        erks.addScript(fctx, script);
    }

    public void setDemoData(List<String[]> demoData) {
        this.demoData = demoData;
    }

    public List<String[]> getDemoData() {
        return demoData;
    }
}
