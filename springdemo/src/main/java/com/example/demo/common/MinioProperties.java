package com.example.demo.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "springboot.minio")
public class MinioProperties {

	private String url;
	
	private String accessKey;
	
	private String secretAccessKey;
	
	private Integer connectTimeout;
	
	private Integer writeTimeout;
	
	private Integer readTimeout;
	
}
