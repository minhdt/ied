package com.minhdt.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.bean.DCDataRow;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichCommandToolbarButton;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.component.UIXValue;
import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class AppUtils {
    public static Object getValueFromIterator(BindingContainer bindingContainer,
                                              String iterator,
                                              String attribute) {

        DCIteratorBinding iterBind =
            (DCIteratorBinding)bindingContainer.get(iterator);
        return iterBind.getCurrentRow().getAttribute(attribute);
    }

    public static Object[] getValueFromIterator(BindingContainer bindingContainer,
                                                String iterator,
                                                String[] attribute) {
        try {
            DCIteratorBinding iterBind =
                (DCIteratorBinding)bindingContainer.get(iterator);
            Object[] result = new Object[attribute.length];

            for (int i = 0; i < attribute.length; i++)
                if (iterBind.getCurrentRow().getAttribute(attribute[i]) ==
                    null) {
                    result[i] = "";
                } else {
                    result[i] =
                            iterBind.getCurrentRow().getAttribute(attribute[i]);
                }
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    //    public static String[] getNameFromPagingView(DCBindingContainer dcBindings,
    //                                                 String pagingView) {
    //        FacesCtrlHierBinding treeData =
    //            (FacesCtrlHierBinding)dcBindings.getControlBinding(pagingView);
    //        String[] attribute = treeData.getAttributeNames();
    //        return attribute;
    //    }

    public static BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public static ViewObject getViewObjectFromIterator(String iterator) {
        DCBindingContainer dcb =
            (DCBindingContainer)ADFUtils.evaluateEL("#{bindings}");
        DCIteratorBinding dciter = dcb.findIteratorBinding(iterator);
        ViewObject vorci = dciter.getViewObject();
        return vorci;
    }

    public static void closePopup(RichPopup popupAddMenu) {
        FacesContext context = FacesContext.getCurrentInstance();
        String popupId = popupAddMenu.getClientId(context);

        StringBuilder script = new StringBuilder();
        script.append("var popup = AdfPage.PAGE.findComponent('").append(popupId).append("'); ").append("if (popup.isPopupVisible()) { ").append("popup.hide();}");
        ExtendedRenderKitService erks =
            Service.getService(context.getRenderKit(),
                               ExtendedRenderKitService.class);
        erks.addScript(context, script.toString());
    }

    public static void showPopup(RichPopup popupAddMenu) {
        FacesContext context = FacesContext.getCurrentInstance();
        String popupId = popupAddMenu.getClientId(context);

        StringBuilder script = new StringBuilder();
        script.append("var popup = AdfPage.PAGE.findComponent('").append(popupId).append("'); ").append("if (!popup.isPopupVisible()) { ").append("popup.show();}");
        ExtendedRenderKitService erks =
            Service.getService(context.getRenderKit(),
                               ExtendedRenderKitService.class);
        erks.addScript(context, script.toString());
    }

    public static void setFocus(UIComponent popupAddMenu) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService service =
            Service.getRenderKitService(facesContext,
                                        ExtendedRenderKitService.class);

        String clientId = popupAddMenu.getClientId(facesContext);
        service.addScript(facesContext,
                          "sys_setFocusId('" + clientId + "::content');");
    }

    public static String getFirstUppperWord(String str) {
        if (str.length() == 0)
            return "";

        if (str.length() == 1)
            return str.toUpperCase();

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static void setValue(UIComponent popupAddMenu, String iValue) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService service =
            Service.getRenderKitService(facesContext,
                                        ExtendedRenderKitService.class);

        String clientId = popupAddMenu.getClientId(facesContext);
        service.addScript(facesContext,
                          "sys_setValueId('" + clientId + "::content','" +
                          iValue + "');");
    }

    public static void setValue(UIComponent popupAddMenu, long iValue) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService service =
            Service.getRenderKitService(facesContext,
                                        ExtendedRenderKitService.class);

        String clientId = popupAddMenu.getClientId(facesContext);
        service.addScript(facesContext,
                          "sys_setValueId('" + clientId + "::content'," +
                          iValue + ");");
    }

    public static void setValueForRichInputText(UIXValue component,
                                                String iValue) {

        if (iValue == null)
            setValue(component, "");
        else
            setValue(component, iValue.toString());

        component.setValue(iValue);
        refreshControl(component);
    }

    public static void setValueOver(RichSelectBooleanRadio uIXValue,
                                    String pageFlowScope_RichSelectBooleanRadio_Init,
                                    boolean value) {
        if (uIXValue != null) {
            uIXValue.resetValue();
            uIXValue.setSelected(value);
            refreshControl(uIXValue);
        }
        ADFUtils.setEL("#{pageFlowScope." +
                       pageFlowScope_RichSelectBooleanRadio_Init + "_Init}",
                       value);
    }

    public static void setValueOver(RichInputText uIXValue,
                                    String pageFlowScope_RichInputText_Init,
                                    Object value) {
        if (uIXValue != null) {
            uIXValue.resetValue();
            uIXValue.setValue(value);
            refreshControl(uIXValue);
        }
        ADFUtils.setEL("#{pageFlowScope." + pageFlowScope_RichInputText_Init +
                       "_Init}", value);
    }

    public static void setValueOver(RichInputText uIXValue,
                                    String pageFlowScope_RichInputText_Init,
                                    String value) {
        if (uIXValue != null) {
            uIXValue.resetValue();
            uIXValue.setValue(value);
            refreshControl(uIXValue);
        }
        ADFUtils.setEL("#{pageFlowScope." + pageFlowScope_RichInputText_Init +
                       "_Init}", value);
    }

    public static void setValueOver(RichInputText uIXValue,
                                    String pageFlowScope_RichInputText_Init,
                                    Long value) {
        if (uIXValue != null) {
            uIXValue.resetValue();
            uIXValue.setValue(value);
            refreshControl(uIXValue);
        }
        ADFUtils.setEL("#{pageFlowScope." + pageFlowScope_RichInputText_Init +
                       "_Init}", value);
    }

    public static void setValueOver(UIXValue uIXValue,
                                    String pageFlowScope_UIXValue_Init,
                                    String value) {
        if (uIXValue != null) {
            uIXValue.setValue(value);
            refreshControl(uIXValue);
        }
        ADFUtils.setEL("#{pageFlowScope." + pageFlowScope_UIXValue_Init +
                       "_Init}", value);
    }

    public static void setValueOver(RichInputDate uIXValue,
                                    String pageFlowScope_RichInputDate_Init,
                                    String value) {
        if (uIXValue != null) {
            uIXValue.resetValue();
            uIXValue.setValue(value);
            refreshControl(uIXValue);
        }
        ADFUtils.setEL("#{pageFlowScope." + pageFlowScope_RichInputDate_Init +
                       "_Init}", value);
    }

    public static void setValueOver(RichSelectOneChoice uIXValue,
                                    String pageFlowScope_RichSelectOneChoice_Init,
                                    String value) {
        if (uIXValue != null) {
            uIXValue.resetValue();
            uIXValue.setValue(value);
            refreshControl(uIXValue);
        }
        ADFUtils.setEL("#{pageFlowScope." +
                       pageFlowScope_RichSelectOneChoice_Init + "_Init}",
                       value);
    }

    public static void setValueOver(UIXValue uIXValue,
                                    String pageFlowScope_UIXValue_Init,
                                    Long value) {
        if (uIXValue != null) {
            uIXValue.setValue(value);
            refreshControl(uIXValue);
        }
        ADFUtils.setEL("#{pageFlowScope." + pageFlowScope_UIXValue_Init +
                       "_Init}", value);
    }

    public static void setVisibleButtonOver(RichCommandToolbarButton rctb,
                                            String pageFlowScope_RichCommandToolbarButton,
                                            boolean value) {
        if (rctb != null)
            rctb.setVisible(value);
        ADFUtils.setEL("#{pageFlowScope." +
                       pageFlowScope_RichCommandToolbarButton + "}", value);
    }

    public static void setVisibleButtonOver(RichPanelGroupLayout rpgl,
                                            String pageFlowScope_RichPanelGroupLayout,
                                            boolean value) {
        if (rpgl != null)
            rpgl.setVisible(value);
        ADFUtils.setEL("#{pageFlowScope." +
                       pageFlowScope_RichPanelGroupLayout + "}", value);
    }

    public static void setDisableOver(String iPageFlow, Object isType,
                                      boolean value) {
        if (isType instanceof RichInputText)
            ((RichInputText)ADFUtils.evaluateEL("#{" + iPageFlow +
                                                "}")).setDisabled(value);
        if (isType instanceof RichInputDate)
            ((RichInputDate)ADFUtils.evaluateEL("#{" + iPageFlow +
                                                "}")).setDisabled(value);
        if (isType instanceof RichSelectOneChoice)
            ((RichSelectOneChoice)ADFUtils.evaluateEL("#{" + iPageFlow +
                                                      "}")).setDisabled(value);
        if (isType instanceof RichSelectBooleanRadio)
            ((RichSelectBooleanRadio)ADFUtils.evaluateEL("#{" + iPageFlow +
                                                         "}")).setDisabled(value);
    }

    public static void setDisableButtonOver(RichCommandToolbarButton rctb,
                                            String pageFlowScope_RichCommandToolbarButton,
                                            boolean value) {
        if (rctb != null)
            rctb.setDisabled(value);
        ADFUtils.setEL("#{pageFlowScope." +
                       pageFlowScope_RichCommandToolbarButton + "}", value);
    }

    public static void setDisableSelectOneChoiceOver(RichSelectOneChoice rctb,
                                                     String pageFlowScope_RichSelectOneChoice,
                                                     boolean value) {
        if (rctb != null)
            rctb.setDisabled(value);
        ADFUtils.setEL("#{pageFlowScope." + pageFlowScope_RichSelectOneChoice +
                       "}", value);
    }

    public static void setDisableButtonOver(RichInputText rit,
                                            String pageFlowScope_RichCommandToolbarButton,
                                            boolean value) {
        if (rit != null)
            rit.setDisabled(value);
        ADFUtils.setEL("#{pageFlowScope." +
                       pageFlowScope_RichCommandToolbarButton + "}", value);
    }

    public static void setDisableButtonOver(String pageFlowScope_RichCommandToolbarButton,
                                            boolean value) {
        ADFUtils.setEL("#{pageFlowScope." +
                       pageFlowScope_RichCommandToolbarButton + "}", value);
    }

    public static void setDisableButtonOver(RichSelectBooleanRadio rsbr,
                                            String pageFlowScope_RichSelectBooleanRadio,
                                            boolean value) {
        if (rsbr != null)
            rsbr.setDisabled(value);
        ADFUtils.setEL("#{pageFlowScope." +
                       pageFlowScope_RichSelectBooleanRadio + "}", value);
    }

    public static void setDisableButtonOver(RichSelectOneChoice rsoc,
                                            String pageFlowScope_RichSelectOneChoice,
                                            boolean value) {
        if (rsoc != null)
            rsoc.setDisabled(value);
        ADFUtils.setEL("#{pageFlowScope." + pageFlowScope_RichSelectOneChoice +
                       "}", value);
    }

    public static void setDisableButtonOver(RichInputDate rid,
                                            String pageFlowScope_RichCommandToolbarButton,
                                            boolean value) {
        if (rid != null)
            rid.setDisabled(value);
        ADFUtils.setEL("#{pageFlowScope." +
                       pageFlowScope_RichCommandToolbarButton + "}", value);
    }

    public static void setValueForRichInputText(String richInputTextInPageFlow,
                                                String iValue) {
        RichInputText ritT =
            (RichInputText)ADFUtils.evaluateEL("#{" + richInputTextInPageFlow +
                                               "}");

        if (ritT == null)
            return;

        ritT.resetValue();
        ritT.setValue(iValue);

        if (iValue == null)
            setValue(ritT, "");
        else
            setValue(ritT, iValue.toString());

        refreshControl(ritT);
    }

    public static void setLongValueForRichInputText(String richInputTextInPageFlow,
                                                    Long iValue) {
        RichInputText ritT =
            (RichInputText)ADFUtils.evaluateEL("#{" + richInputTextInPageFlow +
                                               "}");

        if (ritT == null)
            return;

        ritT.resetValue();
        ritT.setValue(iValue);

        /*
        if( iValue== null )
            setValue(ritT, "");
        else
            setValue(ritT, iValue.longValue());
        */

            refreshControl(ritT);
    }


    public static void setValueForRichInputDate(String richInputTextInPageFlow,
                                                String iValue) {

        RichInputDate ritT =
            (RichInputDate)ADFUtils.evaluateEL("#{" + richInputTextInPageFlow +
                                               "}");

        if (ritT == null)
            return;

        ritT.resetValue();
        ritT.setValue(iValue);

        if (iValue == null)
            setValue(ritT, "");
        else
            setValue(ritT, iValue.toString());

        refreshControl(ritT);
    }

    public static String getValueForRichInputText(String richInputTextInPageFlow) {

        RichInputText ritT =
            (RichInputText)ADFUtils.evaluateEL("#{" + richInputTextInPageFlow +
                                               "}");

        if (ritT == null || ritT.getValue() == null)
            return "";
        else
            return ritT.getValue().toString();
    }

    public static String getValueForRichInputDate(String richInputDateInPageFlow) {

        RichInputDate ritT =
            (RichInputDate)ADFUtils.evaluateEL("#{" + richInputDateInPageFlow +
                                               "}");

        if (ritT == null || ritT.getValue() == null)
            return "";
        else
            return ritT.getValue().toString();
    }

    public static void setCheck(UIComponent popupAddMenu, boolean iValue) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService service =
            Service.getRenderKitService(facesContext,
                                        ExtendedRenderKitService.class);

        String clientId = popupAddMenu.getClientId(facesContext);
        service.addScript(facesContext,
                          "sys_setCheckId('" + clientId + "::content'," +
                          iValue + ");");
    }

    public static void setCheckClient(String id, boolean iValue) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService service =
            Service.getRenderKitService(facesContext,
                                        ExtendedRenderKitService.class);

        service.addScript(facesContext,
                          "sys_setCheckId('" + id + "'," + iValue + ");");
    }

    public static String getMessage(String[] key) {
        String out = "";

        for (int i = 0; i < key.length; i++)
            out = out + JSFUtils.getMessage(key[i]) + " - ";

        if (!out.equals(""))
            out = out.substring(0, out.length() - 3);

        return out;
    }

    public static String getMessage(String[] key, boolean[] bundle) {
        String out = "";

        for (int i = 0; i < key.length; i++)
            if (bundle[i])
                out = out + JSFUtils.getMessage(key[i]) + " - ";
            else
                out = out + key[i] + " - ";

        if (!out.equals(""))
            out = out.substring(0, out.length() - 3);

        return out;
    }

    public static String getMessageNotBot(String[] key, boolean[] bundle) {
        String out = "";

        for (int i = 0; i < key.length; i++)
            if (bundle[i])
                out = out + JSFUtils.getMessage(key[i]) + " ";
            else
                out = out + key[i] + " ";

        if (!out.equals(""))
            out = out.substring(0, out.length() - 1);

        return out;
    }

    public static void excuteMethod(String methodName) {
        BindingContainer bindings = getBindings();

        OperationBinding operationBinding =
            bindings.getOperationBinding(methodName);

        Object result = operationBinding.execute();
    }

    public static void refreshControl(UIComponent component) {
        if (component != null)
            AdfFacesContext.getCurrentInstance().addPartialTarget(component);
    }

    public static void showPopup(UIComponent popup) {
        FacesContext context = FacesContext.getCurrentInstance();
        String id = popup.getClientId(context);

        ExtendedRenderKitService erks =
            Service.getRenderKitService(FacesContext.getCurrentInstance(),
                                        ExtendedRenderKitService.class);
        StringBuilder strb =
            new StringBuilder("AdfPage.PAGE.findComponent(\"" + id +
                              "\").show();");
        erks.addScript(FacesContext.getCurrentInstance(), strb.toString());
    }

    
    public static boolean isPostback() {
        return Boolean.TRUE.equals(JSFUtils.resolveExpression("#{requestContext.postback}"));
    }

    public static Object getDataControl(String dataControl) {
        BindingContext bctx = BindingContext.getCurrent();
        oracle.adf.model.binding.DCDataControl dc =
            bctx.findDataControl(dataControl);
        return dc.getAdaptedDC().getDataProvider();
    }

    public static void removePageLoad(String pageload) {
        if (!pageload.substring(0, 1).equals("#")) {
            pageload = "#{processScope." + pageload + "}";
        }
        FacesContext fctx = FacesContext.getCurrentInstance();
        ELContext elctx = fctx.getELContext();
        ExpressionFactory ef = fctx.getApplication().getExpressionFactory();
        ValueExpression valPageload =
            ef.createValueExpression(elctx, pageload, String.class);

        valPageload.setValue(fctx.getELContext(), "");
    }

    public static boolean isPageLoad(String pageload) {
        if (!pageload.substring(0, 1).equals("#")) {
            pageload = "#{processScope." + pageload + "}";
        }
        boolean returnValue = true;

        FacesContext fctx = FacesContext.getCurrentInstance();
        ELContext elctx = fctx.getELContext();
        ExpressionFactory ef = fctx.getApplication().getExpressionFactory();
        ValueExpression valPageload =
            ef.createValueExpression(elctx, pageload, String.class);

        String beforPageload = (String)valPageload.getValue(elctx);

        if ("".equals(beforPageload)) {

            ValueExpression afterPageload =
                ef.createValueExpression(fctx.getELContext(), pageload,
                                         String.class);
            afterPageload.setValue(fctx.getELContext(), "1");

            returnValue = false;
        }
        return returnValue;
    }

    public static void setPageFlowValue(String key, Object value) {
        Map pageFlowScope =
            RequestContext.getCurrentInstance().getPageFlowScope();
        pageFlowScope.put(key, value);
    }

    public static Object getPageFlowValue(String key) {
        Map pageFlowScope =
            RequestContext.getCurrentInstance().getPageFlowScope();
        Object myObject = pageFlowScope.get(key);
        return myObject;
    }

    public static void setProcessValue(String id, Object value) {
        setValue("processScope." + id, value);
    }

    public static Object getProcessValue(String id) {
        return getValue("processScope." + id);
    }

    public static void setValue(String id, Object value) {
        FacesContext fctx = FacesContext.getCurrentInstance();

        ExpressionFactory ef = fctx.getApplication().getExpressionFactory();

        ValueExpression ve =
            ef.createValueExpression(fctx.getELContext(), "#{" + id + "}",
                                     String.class);

        ve.setValue(fctx.getELContext(), value);
    }

    public static Object getValue(String id) {
        FacesContext fctx = FacesContext.getCurrentInstance();

        ExpressionFactory ef = fctx.getApplication().getExpressionFactory();

        ValueExpression ve =
            ef.createValueExpression(fctx.getELContext(), "#{" + id + "}",
                                     Object.class);

        return ve.getValue(fctx.getELContext());
    }

    public static List getListEntity(String iteratorName) {
        List lst = new ArrayList();
        DCIteratorBinding iter = ADFUtils.getIterator(iteratorName);
        for (Row row : iter.getAllRowsInRange()) {
            DCDataRow dataRow = (DCDataRow)row;
            lst.add(dataRow.getDataProvider());
        }
        return lst;
    }

    public static List getSelectedEntity(String iteratorName) {
        List lst = new ArrayList();
        DCIteratorBinding iter = ADFUtils.getIterator(iteratorName);
        for (Row row : iter.getAllRowsInRange()) {
            if (row.getAttribute("isSelected").equals(true)) {
                DCDataRow dataRow = (DCDataRow)row;
                lst.add(dataRow.getDataProvider());
            }
        }
        return lst;
    }
    
    /**
     * Function: doUpload
     * Content: thuc hien upload file len server
     * @param: fileUploaded
     * @param: strFilePath
     **/
    public static String doUpload(UploadedFile fileUploaded,
                                  String strFilePath) {
        UploadedFile file = fileUploaded;
        // ... and process it in some way
        try {
            InputStream inputStream = file.getInputStream();
            if (inputStream == null) {
                return null;
            }
            File f = new File(strFilePath);
            OutputStream out = new FileOutputStream(f);
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                out.write(buf, 0, len);
            out.close();
            inputStream.close();
        } catch (IOException e) {
            return null;
        }
        return "ok";
    }
    
    public static void js(String content) {
        FacesContext context = FacesContext.getCurrentInstance();

        ExtendedRenderKitService erks =
            Service.getService(context.getRenderKit(),
                               ExtendedRenderKitService.class);
        erks.addScript(context, content);
    }
}
