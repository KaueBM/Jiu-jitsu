package com.dev.jiujitsu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JiujitsuApplication {

	public static void main(String[] args) {
		SpringApplication.run(JiujitsuApplication.class, args);
	}

}
