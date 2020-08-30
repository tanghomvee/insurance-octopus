package com.homvee.insuranceoctopus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.homvee.insuranceoctopus.dao"})
@EntityScan(basePackages = {"com.homvee.insuranceoctopus.dao.entities"})
@ComponentScan(basePackages = {"com.homvee"})
@EnableTransactionManagement
public class OctopusApp {


    public static void main(String[] args) {
        SpringApplication.run(OctopusApp.class, args);
    }



}
