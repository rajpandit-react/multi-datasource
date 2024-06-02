package multipledatasourcedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = {
//		"multipledatasourcedemo.repo.acc",
//		"multipledatasourcedemo.repo.inv",
//})
//@EntityScan(
//		basePackages = {
//				"multipledatasourcedemo.entity.acc",
//				"multipledatasourcedemo.entity.inv",
//		}
//)
public class MultipleDatasourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultipleDatasourceApplication.class, args);
	}

}
