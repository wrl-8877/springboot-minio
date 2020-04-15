/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.model.Author;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
public interface AuthorityDao extends JpaRepository<Author, String>, 
                                                   JpaSpecificationExecutor<Author>{
	/**
	 * 
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @param appKey
	 * @param signKey
	 * @param dataKey
	 * @return
	 */
	public Author  getByAppKeyAndSignKeyAndDataKey(String appKey, String signKey,String  dataKey);
	
	/**
	 * 
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @param appKey
	 * @return
	 */
	public Author getByAppKey(String appKey);

}
