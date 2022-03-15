package cm.antic.pridesoft.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
@EnableCaching
@SpringBootApplication
public class PridesoftWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(PridesoftWebApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate() ;
		return restTemplate ;
	}

}
