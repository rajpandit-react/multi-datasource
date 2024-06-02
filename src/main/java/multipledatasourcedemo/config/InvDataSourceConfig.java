package multipledatasourcedemo.config;


import jakarta.persistence.EntityManagerFactory;
import org.hibernate.dialect.MySQLDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = {"multipledatasourcedemo.entity.inv"})
@EnableJpaRepositories(transactionManagerRef = "invTransactionManager",
                        entityManagerFactoryRef = "invEntityManagerFactory",
                        basePackages = {"multipledatasourcedemo.repo.inv"}
)
public class InvDataSourceConfig {


    @Autowired
    private Boxes boxes;

    @Autowired
    private EntityManagerFactoryBuilder builder;

    @Bean(name = "invDataSource")
    public DataSource dataSource(){
        InvDynamicRoutingDataSource dynamicRoutingDataSource = new InvDynamicRoutingDataSource();
        Map<Object, Object> targetDataSource = new HashMap<>();
        AtomicInteger count = new AtomicInteger(0);
        boxes.getBoxes().stream()
                .peek(boxid -> {
                    targetDataSource.put(boxid, this.dataSourceBox(boxes.getInvUrl().get(count.getAndAdd(1))));
                }).collect(Collectors.toList());
        dynamicRoutingDataSource.setTargetDataSources(targetDataSource);
        dynamicRoutingDataSource.setDefaultTargetDataSource(targetDataSource.get("box2"));
        return dynamicRoutingDataSource;
    }

    private DataSource dataSourceBox(String url){
        return DataSourceBuilder.create()
                .username(boxes.getUsername())
                .password(boxes.getPassword())
                .url(url)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }


//    @Primary
    @Bean(name = "invEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean invEntityManagerFactory(@Qualifier("invDataSource") DataSource dataSource){
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", new MySQLDialect());
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("jpa.show-sql", true);

        return builder.dataSource(dataSource)
                .packages("multipledatasourcedemo.entity.inv")
                .properties(properties)
                .persistenceUnit("inventory")
                .build();
    }


//    @Primary
    @Bean(name = "invTransactionManager")
    public PlatformTransactionManager invTransaction(@Qualifier("invEntityManagerFactory") EntityManagerFactory entityManagerFactory){
        return new JpaTransactionManager(entityManagerFactory);
    }
}
