package com.minhdt.common.msg;

import javax.faces.context.FacesContext;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class Message {
    public Message() {
        super();
    }
    
    public static void smallBoxInfo(String content) {
        smallBox(MessageType.INFO, "Thông báo!", content, "zmdi-alert-polygon", 5000);
    }
    
    public static void smallWarning(String content) {
        smallBox(MessageType.WARNING, "Thông báo!", content, "zmdi-alert-polygon", 5000);
    }
    
    public static void smallError(String content) {
        smallBox(MessageType.ERROR, "Thông báo!", content, "zmdi-alert-polygon", 5000);
    }
    
    public static void smallSuccess(String content) {
        smallBox(MessageType.SUCCESS, "Thông báo!", content, "zmdi-alert-polygon", 5000);
    }
    
    public static void smallBox(String type, String title, String content, String icon, int timeout) {
        FacesContext context = FacesContext.getCurrentInstance();

        StringBuilder script = new StringBuilder();
        script.append("if ($.noty) {");
        script.append("    var smallBox = function smallBox(type, text, timeout) {");
        script.append("        var n = noty({");
        script.append("            text: text,");
        script.append("            type: type,");
        script.append("            dismissQueue: true,");
        script.append("            layout: 'topRight',");
        script.append("            closeWith: ['click'],");
        script.append("            theme: 'ThemeNoty',");
        script.append("            maxVisible: 10,");
        script.append("            animation: {");
        script.append("                open: 'noty_animated bounceInRight',");
        script.append("                close: 'noty_animated bounceOutRight',");
        script.append("                easing: 'swing',");
        script.append("                speed: 500");
        script.append("            }");
        script.append("        });");
        script.append("        setTimeout(function () {");
        script.append("            n.close();");
        script.append("        },timeout);");
        script.append("    }");
        script.append("} ");
        
        script.append("    smallBox(");
        script.append("\"" + type + "\",");
        script.append("\"<div class='activity-item'><i class='zmdi " + icon + "'></i><div class='activity'>" + title + "<span>" + content + "</span></div></div>\",");
        script.append(timeout).append(");");        
        
        ExtendedRenderKitService erks =
            Service.getService(context.getRenderKit(),
                               ExtendedRenderKitService.class);
        erks.addScript(context, script.toString());
    }
    
    public static void confirmBox(String title, String content, String controlId, String serverListenerType) {
        FacesContext context = FacesContext.getCurrentInstance();

        StringBuilder script = new StringBuilder();
        
        script.append("var control = AdfPage.PAGE.findComponent('" + controlId + "'); ");
        script.append("swal({");
        script.append("    title: '" + title + "',");
        script.append("    text: '" + content + "',");
        script.append("    type: 'warning',");
        script.append("    showCancelButton: true,");
        script.append("    confirmButtonColor: '#DD6B55',");
        script.append("    confirmButtonText: 'Đồng ý',");
        script.append("    cancelButtonText: 'Không'");
        script.append("}, function (isConfirm) {");
        script.append("    if (isConfirm) {");
        script.append("        AdfCustomEvent.queue(control,");
        script.append("                             '" + serverListenerType + "',");
        script.append("                             {},");
        script.append("                             false);");
        script.append("    } ");
        script.append("});");

        ExtendedRenderKitService erks =
            Service.getService(context.getRenderKit(),
                               ExtendedRenderKitService.class);
        erks.addScript(context, script.toString());        
    }
}
