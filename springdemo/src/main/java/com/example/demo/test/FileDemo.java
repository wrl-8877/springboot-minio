/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
@Slf4j
public class FileDemo {

	public static void main(String[] args) throws IllegalStateException, IOException {
		String strUrl = "F://1/1.gif";
        File file = new File(strUrl);
        InputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
        log.info("file转multipartFile成功. {}",multipartFile);
        System.out.println(multipartFile.getName());
        String filePath = "F://1/4.gif";
        multipartFile.transferTo(new File(filePath.toString()));
	}
}
