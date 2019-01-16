package com.kol.pes.service;

import com.kol.pes.item.DataLoginItem;

public interface LoginService extends DataEnableService {
	
	public DataLoginItem login(String userId, String password);//登录
	
	public boolean setPassword(String userId, String password);
}
