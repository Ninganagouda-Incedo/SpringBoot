package com.boot.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.SpringServletContainerInitializer;

// Boot application starts from here.
@SpringBootApplication
@ComponentScan( basePackages = {"com.boot"})
public class Application extends SpringServletContainerInitializer {

	/*
	 * Because By default everything is created
	 */
	/*protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}*/
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
