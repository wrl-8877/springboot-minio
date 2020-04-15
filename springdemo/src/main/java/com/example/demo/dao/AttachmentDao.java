/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.model.Attachment;





/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
public interface AttachmentDao extends JpaRepository<Attachment, String>, JpaSpecificationExecutor<Attachment>{

	/**
	 * 
	 * <B>方法名称：</B>getByApplyId<BR>
	 * <B>概要说明：</B>根据applyId获取attachment信息<BR>
	 * @param applyId
	 * @return
	 */
	List<Attachment> getByApplyId(String applyId);
}
