package com.backbase.interview.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Class to bootstrap the Course Registration System application.
 */
@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.backbase.interview.course.*"})  // scan JPA entities
public class CourseApplication {

    /**
     * Main method to start the application.
     * @param args - String array object.
     */
    public static void main(String[] args) {
        SpringApplication.run(CourseApplication.class, args);
    }

}
