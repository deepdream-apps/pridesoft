package cm.antic.pridesoft.remotesrv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EnableCaching
@EntityScan ("cm.antic.pridesoft.datamodel.remote")
@EnableJpaRepositories("cm.antic.pridesoft.remotesrv.repository")
@SpringBootApplication
public class PridesoftRemotesrvApplication {

	public static void main(String[] args) {
		SpringApplication.run(PridesoftRemotesrvApplication.class, args);
	}
	
}
