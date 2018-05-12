package com.creativeworkes.configserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * WEB页面访问配置
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    super.addViewControllers(registry);
    registry.addViewController("/").setViewName("redirect:/index.html");
  }

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.favorParameter(Boolean.FALSE);
    configurer.favorPathExtension(Boolean.FALSE);
    configurer.ignoreAcceptHeader(Boolean.FALSE);
    configurer.mediaType("all", MediaType.ALL);
  }
}
