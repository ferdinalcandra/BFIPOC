package com.msi.upload.config;

import java.util.Base64;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
  basePackages = "com.msi.upload.repository.temp",
        entityManagerFactoryRef = "tempEntityManagerFactory",
        transactionManagerRef = "tempTransactionManager"
)

public class TempDataSourceConfig {
	@Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties(prefix="datasource.temp")
    public DataSourceProperties tempDataSourceProperties() {
        return new DataSourceProperties();
    }
 
    @Bean
    public DataSource tempDataSource() {
    	byte[] decodedBytes = Base64.getDecoder().decode(env.getProperty("spring.datasource.temp.password"));
		String decodedPass = new String(decodedBytes);
		DataSourceProperties tempDataSourceProperties = tempDataSourceProperties();
        return DataSourceBuilder.create()
		.driverClassName(env.getProperty("spring.datasource.temp.driverClassName"))
        .url(env.getProperty("spring.datasource.temp.url"))
        .username(env.getProperty("spring.datasource.temp.username"))
        .password(decodedPass)
        .build();
    }
 
    @Bean
    public PlatformTransactionManager tempTransactionManager() {
        EntityManagerFactory factory = tempEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean tempEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(tempDataSource());
        factory.setPackagesToScan(new String[]{"com.msi.upload.model.temp"});
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
     
        Properties jpaProperties = new Properties();
//        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        jpaProperties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        factory.setJpaProperties(jpaProperties);
     
        return factory;
    }
 
    @Bean
    public DataSourceInitializer tempDataSourceInitializer() {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(tempDataSource());
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("security-data.sql"));
        dataSourceInitializer.setDatabasePopulator(databasePopulator);
        dataSourceInitializer.setEnabled(env.getProperty("datasource.security.initialize", Boolean.class, false));
        return dataSourceInitializer;
    }   
}
