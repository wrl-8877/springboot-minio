/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
@Data
@Entity
@Table(name = "AUTHORITY")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Author {

	@Id
	@GeneratedValue(generator = "jpa-uuid")
	private String id ;
	
	private String appKey;
	
	private String signKey;
	
	private String dataKey;
}
