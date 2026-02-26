package com.seaside;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class SeasideApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeasideApplication.class, args);
	}

}
