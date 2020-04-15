/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.test;

import org.springframework.beans.BeanUtils;

import com.example.demo.model.AttachVo;
import com.example.demo.model.Attachment;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
public class Test001 {

	public static void main(String[] args) {
		Attachment att = new Attachment();
		AttachVo xx = new AttachVo();
		xx.setApplyId("001");
		xx.setFileName("002");
		xx.setUrl("https://baidu.com");
		BeanUtils.copyProperties(xx, att);
		System.out.println(att);
	}
}
