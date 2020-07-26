package com.example.solace.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

//Configuration class to disable the execution of spring boot application when running the unit tests
// Refer to
// https://stackoverflow.com/questions/29344313/prevent-application-commandlinerunner-classes-from-executing-during-junit-test


@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CommandLineRunner.class))
@EnableAutoConfiguration
public class TestApplicationConfiguration {

}
