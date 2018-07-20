package com.ppc.eligibility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ppc.eligibility"})
@EnableJpaRepositories(basePackages = "com.ppc.eligibility.repository")
@EntityScan(basePackages = "com.ppc.eligibility.entity")
@PropertySources({@PropertySource(value = {"classpath:eligibilityservice.properties"}), @PropertySource(value = {"file:${eligibilityservice.properties}"}, ignoreResourceNotFound = true)})
public class EligibilityappApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(EligibilityappApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EligibilityappApplication.class);
    }
}
