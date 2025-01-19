package com.project2.backemd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BackemdApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackemdApplication.class, args);
	}

}
