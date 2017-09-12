package ru.ulmc.telegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = "ru.ulmc.telegram")
@EnableAutoConfiguration
@EnableAsync
@EnableScheduling
public class JosephBot {
    public static void main(String[] args) {
        SpringApplication.run(JosephBot.class, args);
    }
}

