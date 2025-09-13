package com.example.BrightAid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.BrightAid")
public class BrightAidApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrightAidApplication.class, args);
	}

}
