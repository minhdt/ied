package com.minhdt.model.services;

import com.minhdt.model.entities.SysUser;

import java.util.List;

import javax.ejb.Local;

@Local
public interface SB_SystemLocal extends SB_CommonLocal {
    List<SysUser> getAllUser();
}
