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
    basePackages = "com.msi.upload.repository.ms",
    entityManagerFactoryRef = "msEntityManagerFactory",
    transactionManagerRef = "msTransactionManager"
)

public class MsDataSourceConfig {
	@Autowired
    private Environment env;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "datasource.ms")
    public DataSourceProperties msDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource ordersDataSource() {
    	byte[] decodedBytes = Base64.getDecoder().decode(env.getProperty("spring.datasource.ms.password"));
		String decodedPass = new String(decodedBytes);
        DataSourceProperties primaryDataSourceProperties = msDataSourceProperties();
        return DataSourceBuilder.create()
            .driverClassName(env.getProperty("spring.datasource.ms.driverClassName"))
            .url(env.getProperty("spring.datasource.ms.url"))
            .username(env.getProperty("spring.datasource.ms.username"))
            .password(decodedPass)
            .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager msTransactionManager() {
        EntityManagerFactory factory = msEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean msEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(ordersDataSource());
        factory.setPackagesToScan(new String[] {
            "com.msi.upload.model.ms"
        });
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
//        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        jpaProperties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        factory.setJpaProperties(jpaProperties);

        return factory;

    }

    @Bean
    @Primary
    public DataSourceInitializer msDataSourceInitializer() {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(ordersDataSource());
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("orders-data.sql"));
        dataSourceInitializer.setDatabasePopulator(databasePopulator);
        dataSourceInitializer.setEnabled(env.getProperty("datasource.orders.initialize", Boolean.class, false));
        return dataSourceInitializer;
    }
}
