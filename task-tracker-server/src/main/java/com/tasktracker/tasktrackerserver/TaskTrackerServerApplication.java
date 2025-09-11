package com.tasktracker.tasktrackerserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class TaskTrackerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerServerApplication.class, args);
    }

}
