package com.example;

import com.example.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class FileDemoApplication {

	public static void main(String[] args) {
		System.out.println("开始启动......");
		SpringApplication.run(FileDemoApplication.class, args);
		System.out.println("启动成功！");
	}

}
