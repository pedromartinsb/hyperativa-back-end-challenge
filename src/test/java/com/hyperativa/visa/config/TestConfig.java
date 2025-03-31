package com.hyperativa.visa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyperativa.visa.common.exception.GlobalExceptionHandler;
import com.hyperativa.visa.domain.service.CryptoService;
import com.hyperativa.visa.infrastructure.service.CryptoServiceImpl;
import com.hyperativa.visa.application.usecase.CreateUserUseCase;
import com.hyperativa.visa.domain.port.UserRepositoryPort;
import com.hyperativa.visa.infrastructure.repository.UserRepositoryImpl;
import com.hyperativa.visa.infrastructure.repository.jpa.UserJpaRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@TestConfiguration
@EnableWebMvc
@EntityScan(basePackages = "com.hyperativa.visa.infrastructure.repository.jpa")
@EnableJpaRepositories(basePackages = "com.hyperativa.visa.infrastructure.repository.jpa")
public class TestConfig implements WebMvcConfigurer {

    private static final String TEST_KEY = "1234567890123456"; // 16 bytes for AES-128

    @MockBean
    private UserJpaRepository userJpaRepository;

    @Bean
    @Primary
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.setProperty("hibernate.show_sql", "true");

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.hyperativa.visa.infrastructure.repository.jpa");
        factory.setDataSource(dataSource());
        factory.setJpaProperties(properties);
        return factory;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public CryptoService cryptoService() {
        CryptoServiceImpl cryptoService = new CryptoServiceImpl();
        // Set the crypto key using reflection since it's @Value injected
        try {
            java.lang.reflect.Field field = CryptoServiceImpl.class.getDeclaredField("cryptoKey");
            field.setAccessible(true);
            field.set(cryptoService, TEST_KEY);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set crypto key", e);
        }
        return cryptoService;
    }

    @Bean
    @Primary
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @Primary
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    @Primary
    public UserRepositoryPort userRepositoryPort() {
        return new UserRepositoryImpl(userJpaRepository);
    }

    @Bean
    @Primary
    public CreateUserUseCase createUserUseCase(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder) {
        return new CreateUserUseCase(userRepositoryPort, passwordEncoder);
    }
} 