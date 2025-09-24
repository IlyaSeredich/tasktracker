package com.tasktracker.tasktrackeruserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tasktracker"})
//@EntityScan(basePackages = {"com.tasktracker.tasktrackerentity"})
//@EnableJpaRepositories(basePackages = {"com.tasktracker.tasktrackeruserservice.repository"})
public class TaskTrackerUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskTrackerUserServiceApplication.class, args);
	}

}
