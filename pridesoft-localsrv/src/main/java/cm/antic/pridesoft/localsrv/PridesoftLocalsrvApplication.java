package cm.antic.pridesoft.localsrv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

@EntityScan ("cm.antic.pridesoft.datamodel.local")
@EnableMongoRepositories("cm.antic.pridesoft.localsrv.repository")
@SpringBootApplication
public class PridesoftLocalsrvApplication {

	public static void main(String[] args) {
		SpringApplication.run(PridesoftLocalsrvApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate() ;
		return restTemplate ;
	}

}
