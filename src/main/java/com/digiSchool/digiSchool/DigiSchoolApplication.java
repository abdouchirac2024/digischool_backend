package com.digiSchool.digiSchool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DigiSchoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigiSchoolApplication.class, args);
	}

}
