package cm.antic.pridesoft.localsrv.startup;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Component
public class CNECommandLineRunner implements CommandLineRunner {
	
	public void run(String...args) throws Exception {
        log.info(String.format("Lancement de la récupération des CNE Pridesoft %s", 
        		LocalDateTime.now()));
        
    }
}
