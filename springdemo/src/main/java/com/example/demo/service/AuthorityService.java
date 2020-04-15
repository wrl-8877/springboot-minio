/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.service;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
public interface AuthorityService {

	boolean get(String appKey, String signKey,String  dataKey);
	
	boolean getByAppKey(String appKey);
}
