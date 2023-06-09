package com.idea.recon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import java.util.HashMap;
import java.util.Map;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;



/*@Configuration
//@EnableJpaRepositories(basePackages = "com.idea.recon.repositories")
//@EntityScan(basePackages = {"com.idea.recon.entities"}) 
public class JpaConfig {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Lazy
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public JpaTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan("com.idea.recon.entities");
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaProperties(jpaProperties());

        return entityManagerFactory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://database-1.chwwf3hktlmf.us-east-2.rds.amazonaws.com:3306/recon");
        dataSource.setUsername("admin");
        dataSource.setPassword("password");

        return dataSource;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();

        properties.setProperty("hibernate.physical_naming_strategy",
                "com.idea.recon.config.LowercasePhysicalNamingStrategy");
        properties.setProperty("hibernate.implicit_naming_strategy",
                "com.idea.recon.config.CamelCaseToSnakeCaseNamingStrategy");
        properties.setProperty("hibernate.physical_naming_strategy_override", "true");
        properties.setProperty("hibernate.physical_naming_strategy_override.Trainee", "trainee");
        properties.setProperty("hibernate.physical_naming_strategy_override.com.idea.recon.entities.TraineeLogin", "trainee_login");

        properties.setProperty("hibernate.show_sql", Boolean.toString(true));
        //properties.setProperty("hibernate.hbm2ddl.auto", jpaDdlAuto);
        
        logger.warn(properties.toString());
        
        return properties;
    }

}*/
