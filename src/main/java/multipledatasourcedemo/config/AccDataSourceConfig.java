package multipledatasourcedemo.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnits;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = {"multipledatasourcedemo.entity.acc"})
@EnableJpaRepositories(transactionManagerRef = "accTransactionManager",
        entityManagerFactoryRef = "accEntityManagerFactory",
        basePackages = {"multipledatasourcedemo.repo.acc"}
)
public class AccDataSourceConfig {

    @Autowired
    private Boxes boxes;


    @Bean(name = "accDataSource")
    @Primary
    public DataSource dataSource(){
        AtomicInteger count = new AtomicInteger();
        AccDynamicRoutingDataSource routingDataSource = new AccDynamicRoutingDataSource();
        Map<Object, Object> targetDataSource = new HashMap<>();
        boxes.getBoxes().stream().peek(boxId -> {
            int index = count.getAndAdd(1);
            targetDataSource.put(boxId, this.dataSourceBox(boxes.getAccUrl().get(index)));
        }).collect(Collectors.toList());


        routingDataSource.setTargetDataSources(targetDataSource);
        routingDataSource.setDefaultTargetDataSource(targetDataSource.get("box1"));
        return routingDataSource;
    }

    private DataSource dataSourceBox(String url){
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url(url)
                .password(boxes.getPassword())
                .username(boxes.getUsername())
                .build();
    }



    @Primary
    @Bean(name = "accEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    accEntityManagerFactory(EntityManagerFactoryBuilder builder,
                            @Qualifier("accDataSource") DataSource dataSource){
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", new MySQLDialect());
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("jpa.show-sql", true);

        LocalContainerEntityManagerFactoryBean accounts = builder.dataSource(dataSource)
                .packages("multipledatasourcedemo.entity.acc")
                .persistenceUnit("accounts")
                .properties(properties).build();
        return accounts;

    }

    @Primary
    @Bean(name = "accTransactionManager")
    public PlatformTransactionManager accTransaction(@Qualifier("accEntityManagerFactory") EntityManagerFactory accEntityManagerFactory){
        return new JpaTransactionManager(accEntityManagerFactory);
    }

    @Primary
    @Bean(name = "accJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("accDataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
