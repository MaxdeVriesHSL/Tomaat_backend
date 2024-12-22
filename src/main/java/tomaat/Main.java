package tomaat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EntityScan("tomaat")
@EnableJpaRepositories("tomaat.DAO")
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
