package com.example.demo.common;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

@Configuration
@ConditionalOnClass(MinioClient.class)
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {
	
	@Value("${springboot.minio.buckets}")
	private String[] buckets;
	
	
	/**
	 * 
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @param minioProperties
	 * @return
	 * @throws InvalidEndpointException
	 * @throws InvalidPortException
	 */
	@Bean
	public MinioClient initMinioClient(@Autowired MinioProperties minioProperties) throws InvalidEndpointException, InvalidPortException {
		 /*String url ="https://play.min.io";
		 String accessKey = "Q3AM3UQ867SPQQA43P2F";
		 String secretAccessKey = "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG";*/
		 // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
		 MinioClient minioClient = new MinioClient(minioProperties.getUrl(), minioProperties.getAccessKey(), minioProperties.getSecretAccessKey());
		 minioClient.setTimeout(TimeUnit.SECONDS.toMillis(minioProperties.getConnectTimeout()), 
				 TimeUnit.SECONDS.toMillis(minioProperties.getWriteTimeout()),
				 TimeUnit.SECONDS.toMillis(minioProperties.getReadTimeout()));
		/* MinioClient minioClient = new MinioClient(url, accessKey, secretAccessKey);
		 minioClient.setTimeout(TimeUnit.SECONDS.toMillis(minioProperties.getConnectTimeout()), 
				 TimeUnit.SECONDS.toMillis(minioProperties.getWriteTimeout()),
				 TimeUnit.SECONDS.toMillis(minioProperties.getReadTimeout()));*/	 
		 //检查bucket是否存在，不存在就创建
		 checkBucket(minioClient);
		 return minioClient;
	}
	
	
	/**
	 * 
	 * <B>方法名称：</B>checkBucket<BR>
	 * <B>概要说明：</B>检查buckets桶是否存在，创建<BR>
	 * @param minioClient
	 */
	private void checkBucket(MinioClient minioClient){
		Stream.of(buckets).forEach(bucket ->{
			try {
				if(minioClient.bucketExists(bucket)) return;
				minioClient.makeBucket(bucket);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	
}
