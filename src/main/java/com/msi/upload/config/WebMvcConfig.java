package com.msi.upload.config;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig {
	@Bean
    public OpenEntityManagerInViewFilter tempOpenEntityManagerInViewFilter() {
        OpenEntityManagerInViewFilter osivFilter = new OpenEntityManagerInViewFilter();
        osivFilter.setEntityManagerFactoryBeanName("tempEntityManagerFactory");
        return osivFilter;
    }
 
    @Bean
    public OpenEntityManagerInViewFilter msOpenEntityManagerInViewFilter() {
        OpenEntityManagerInViewFilter osivFilter = new OpenEntityManagerInViewFilter();
        osivFilter.setEntityManagerFactoryBeanName("msEntityManagerFactory");
        return osivFilter;
    }
}
