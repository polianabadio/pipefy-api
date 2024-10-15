package com.katiamaria.pipefy_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PipefyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PipefyApiApplication.class, args);
	}
}
