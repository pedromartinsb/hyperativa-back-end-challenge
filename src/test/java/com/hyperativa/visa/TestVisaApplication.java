package com.hyperativa.visa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(
    basePackages = "com.hyperativa.visa",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = VisaApplication.class
    )
)
@EnableJpaRepositories(basePackages = "com.hyperativa.visa.infrastructure.repository")
@EntityScan(basePackages = "com.hyperativa.visa.domain.model")
public class TestVisaApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TestVisaApplication.class);
        application.setAllowBeanDefinitionOverriding(true);
        application.run(args);
    }
} 