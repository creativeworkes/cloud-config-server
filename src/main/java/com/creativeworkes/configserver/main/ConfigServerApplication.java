package com.creativeworkes.configserver.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableConfigServer
@SpringBootApplication
@EntityScan(basePackages = { "com.creativeworkes.configserver" })
@ComponentScan(basePackages = { "com.creativeworkes.configserver" })
@EnableJpaRepositories(basePackages = { "com.creativeworkes.configserver.repository" })
public class ConfigServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConfigServerApplication.class, args);
  }
}
