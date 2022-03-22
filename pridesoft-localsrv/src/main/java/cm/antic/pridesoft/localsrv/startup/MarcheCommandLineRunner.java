package cm.antic.pridesoft.localsrv.startup;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cm.antic.pridesoft.datamodel.local.Projet;
import cm.antic.pridesoft.datamodel.remote.ProjetRemote;
import cm.antic.pridesoft.localsrv.service.ProjetService;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Component
public class MarcheCommandLineRunner implements CommandLineRunner {
	@Autowired
	private ProjetService projetService ;
	@Autowired
	private RestTemplate restTemplate ;
	@Autowired
	private Environment env ;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
	
	
    public void run(String...args) throws Exception {
        log.info(String.format("Lancement de la récupération des marchés Pridesoft %s", LocalDateTime.now())) ;
        ScheduledExecutorService   executorService = Executors.newSingleThreadScheduledExecutor() ;
        executorService.scheduleWithFixedDelay(() -> {
        	IntStream.rangeClosed(Integer.valueOf(env.getProperty("app.localsrv.recuperation_projets.annee_depart")), LocalDate.now().getYear())
        	         .forEach((year)  -> {
        	        	 CompletableFuture.supplyAsync(() -> this.telechargerMarchesTic (year))
        				 				  .thenAccept(this::enregisterMarchesTic) ;
        	         }) ;
        }, 0, Long.valueOf(env.getProperty("app.localsrv.recuperation_projets.periodicite")), TimeUnit.HOURS) ;
        
    }
     
    
    
    private List<ProjetRemote> telechargerMarchesTic (int year){
    	log.info(String.format("Lancement de la récupération des marchés Pridesoft pour l'année %s", year));
    	LocalDate dateDebut = LocalDate.of(year, Month.JANUARY, 1) ;
    	LocalDate dateFin = LocalDate.of(year, Month.DECEMBER, 31) ;
    	
    	String remoteUrl = "http://"+env.getProperty("app.remotesrv.host")+":"+env.getProperty("app.remotesrv.port") ;
    	
    	ResponseEntity<ProjetRemote[]> response = restTemplate.getForEntity(remoteUrl+"/ws/projet/periode/{date1}/{date2}", 
				ProjetRemote[].class, dateDebut.format(formatter), dateFin.format(formatter)) ;
		
		List<ProjetRemote> listeProjetsRemote = Arrays.asList(response.getBody());
		
		try {
			Stream<String> streamMotsCle = Files.lines(Paths.get(env.getProperty("app.localsrv.path.data.keyworks"))) ; 
		
			List<String> listeMotsCles = streamMotsCle.collect(Collectors.toList()) ;
		
			return listeProjetsRemote.stream()
				.filter(projet -> {
					return listeMotsCles.parallelStream().anyMatch(motCle -> projet.getDesignation().toLowerCase().contains(motCle)) ;
				}).collect(Collectors.toList()) ;	
		}catch(IOException ex) {
			log.info(String.format("Erreur survenue lors de la récupération  des marchés Pridesoft pour l'année %s", year));
			return new ArrayList<ProjetRemote>() ;
		}
    }
    
    
   private Long enregisterMarchesTic (List<ProjetRemote> listeProjetsRemote){
	   
	   log.info(String.format("Lancement de l'enregistrement des marchés récupérés %s", listeProjetsRemote.size()));
	   
	   return listeProjetsRemote.parallelStream()
			   		.map(projetR  -> {
			   				Projet projet = Projet.builder()
			   						.libelle(projetR.getDesignation())
			   						.codeProjet(projetR.getCodeProjet())
			   						.dateSignature(projetR.getDateSignature())
			   						.montant(projetR.getMontant())
			   						.idRegion(projetR.getRegion() == null ? null : projetR.getRegion().getId())
			   						.libelleRegion(projetR.getRegion() == null ? null : projetR.getRegion().getLibelle())
			   						.idMaitreOuvrage(projetR.getMaitreOuvrage() == null ? null :  projetR.getMaitreOuvrage().getId())
			   						.libelleMaitreOuvrage(projetR.getMaitreOuvrage() == null ? null : projetR.getMaitreOuvrage().getLibelle())
			   						.build() ;
			   				return projetService.creerEtValider(projet) ;
			   		}).count() ;
    }
   
   
}
