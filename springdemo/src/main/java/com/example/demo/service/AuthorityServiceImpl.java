/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.AuthorityDao;
import com.example.demo.model.Author;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {
	@Autowired
	AuthorityDao authorityDao;
	/**
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see com.example.demo.service.AuthorityService#get(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean get(String appKey, String signKey, String dataKey) {
		// TODO Auto-generated method stub
		Author author  = authorityDao.getByAppKeyAndSignKeyAndDataKey(appKey, signKey, dataKey);		
		return (author!=null && !"".equals(author))?true:false;
	}


	/**
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see com.example.demo.service.AuthorityService#getByAppKey(java.lang.String)
	 */
	@Override
	public boolean getByAppKey(String appKey) {
		// TODO Auto-generated method stub
		Author author  = authorityDao.getByAppKey(appKey);
		return (author!=null && !"".equals(author))?true:false;
	}
}
