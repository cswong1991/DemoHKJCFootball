package com.cswongwd.hkjcfootball;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {
	@Autowired
	DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}
