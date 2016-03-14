package com.minhdt.view.dao;

import com.minhdt.common.AppUtils;
import com.minhdt.model.services.SB_SystemBean;
import com.minhdt.model.services.SB_SystemLocal;

public class DaoFactory {
    public DaoFactory() {
        super();
    }
    
    public static SB_SystemLocal getSB_SystemLocal() {
        return (SB_SystemLocal) AppUtils.getDataControl(SB_SystemBean.class.getSimpleName());
    }
}

