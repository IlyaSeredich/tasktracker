package com.tasktracker.tasktrackertaskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tasktracker"})
public class TaskTrackerTaskServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskTrackerTaskServiceApplication.class, args);
	}

}
