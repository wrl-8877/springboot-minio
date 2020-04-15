/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.test;

import com.sinosoft.minio_client_sdk.secrity.exception.SecurityException;
import com.sinosoft.minio_client_sdk.secrity.util.SHA;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
public class Test002 {

	public static void main(String[] args) throws SecurityException {
	   String code1 = SHA.genSign256("code");
	   String code2 = SHA.genSign256("code");
	   String code3 = SHA.genSign256("code");
	   String code4 = SHA.genSign256("code");
	   String code5 = SHA.genSign256("code");
	   String code6 = SHA.genSign256("code");
	   System.out.println("code1:"+code1);
	   System.out.println("code2:"+code2);
	   System.out.println("code3:"+code3);
	   System.out.println("code4:"+code4);
	   System.out.println("code5:"+code5);
	   System.out.println("code6:"+code6);
	}
}
