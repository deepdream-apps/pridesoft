package cm.antic.pridesoft.localsrv;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

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
	
	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.apiInfo(apiInfo())
        		.select()
                	.apis(RequestHandlerSelectors.basePackage("cm.antic.pridesoft.localsrv.webservice"))
                .build();
    }
	
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Pridesoft Local APIs")
								   .description("Pridesoft Local API")
								   .termsOfServiceUrl("https://www.antic.cm")
								   .licenseUrl("https://www.antic.cm")
								   .version("0.0.1-SNAPSHOT")
								   .build() ;
	}

}
