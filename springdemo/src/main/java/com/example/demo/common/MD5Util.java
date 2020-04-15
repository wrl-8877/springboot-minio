package com.example.demo.common;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * 
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B>MD5工具类<BR>
 * @author 中科软科技 
 * @since 2019年7月3日
 */
public class MD5Util {

	   public static final String MD5(String s) {
	      char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	      try {
	         byte[] e = s.getBytes();
	         MessageDigest mdInst = MessageDigest.getInstance("MD5");
	         mdInst.update(e);
	         byte[] md = mdInst.digest();
	         int j = md.length;
	         char[] str = new char[j * 2];
	         int k = 0;

	         for(int i = 0; i < j; ++i) {
	            byte byte0 = md[i];
	            str[k++] = hexDigits[byte0 >>> 4 & 15];
	            str[k++] = hexDigits[byte0 & 15];
	         }

	         return new String(str);
	      } catch (Exception var10) {
	         var10.printStackTrace();
	         return null;
	      }
	   }

	   public static void main(String[] args) {
	      SecureRandom secureRandom = new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes());
	      String appKey = "yaoHuiZhiHao";
	      long currentTime = System.currentTimeMillis();
	      String uuid = UUID.randomUUID().toString().replace("-", "");
	      String nonce = String.valueOf(secureRandom.nextInt(10000));
	      String signKey = MD5("signKey" + uuid + appKey + currentTime + nonce);
	      String dataKey = MD5("dataKey" + uuid + appKey + currentTime + nonce);
	      System.out.println(signKey);
	      System.out.println(dataKey);
	   }
	}