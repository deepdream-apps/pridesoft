package cm.antic.pridesoft.localsrv.startup;
import java.io.IOException;
import java.math.BigDecimal;
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
import cm.antic.pridesoft.datamodel.local.Cne;
import cm.antic.pridesoft.datamodel.local.MaitreOuvrage;
import cm.antic.pridesoft.datamodel.local.Region;
import cm.antic.pridesoft.datamodel.remote.CneRemote;
import cm.antic.pridesoft.localsrv.service.CneService;
import cm.antic.pridesoft.localsrv.service.MaitreOuvrageService;
import cm.antic.pridesoft.localsrv.service.RegionService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CNECommandLineRunner implements CommandLineRunner {
	@Autowired
	private CneService cneService ;
	@Autowired
	private MaitreOuvrageService maitreOuvrageService ;
	@Autowired
	private RegionService regionService ;
	@Autowired
	private RestTemplate restTemplate ;
	@Autowired
	private Environment env ;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
	
	
    public void run(String...args) throws Exception {
        log.info(String.format("Lancement de la récupération des bons de commande Pridesoft %s", LocalDateTime.now())) ;
        ScheduledExecutorService   executorService = Executors.newSingleThreadScheduledExecutor() ;
        executorService.scheduleWithFixedDelay(() -> {
        	IntStream.rangeClosed(Integer.valueOf(env.getProperty("app.localsrv.recuperation_projets.annee_depart")), LocalDate.now().getYear())
        	         .forEach((year)  -> {
        	        	 CompletableFuture.supplyAsync(() -> this.telechargerCNEs (year))
        				 				  .thenAccept(this::enregisterProjetsTic) ;
        	         }) ;
        }, 0, Long.valueOf(env.getProperty("app.localsrv.recuperation_projets.periodicite")), TimeUnit.HOURS) ;
        
    }
     
    
    
    private List<CneRemote> telechargerCNEs (int year){
    	log.info(String.format("Lancement de la récupération des bons de commande Pridesoft pour l'année %s", year));
    	LocalDate dateDebut = LocalDate.of(year, Month.JANUARY, 1) ;
    	LocalDate dateFin = LocalDate.of(year, Month.DECEMBER, 31) ;
    	
    	String remoteUrl = "http://"+env.getProperty("app.remotesrv.host")+":"+env.getProperty("app.remotesrv.port") ;
    	
    	ResponseEntity<CneRemote[]> response = restTemplate.getForEntity(remoteUrl+"/ws/cne/periode/{date1}/{date2}", 
				CneRemote[].class, dateDebut.format(formatter), dateFin.format(formatter)) ;
		
		List<CneRemote> listeProjetsRemote = Arrays.asList(response.getBody());
		
		
		try {
			Stream<String> streamMotsCle = Files.lines(Paths.get(env.getProperty("app.localsrv.path.data.keyworks"))) ; 
		
			List<String> listeMotsCles = streamMotsCle.collect(Collectors.toList()) ;
		
			return listeProjetsRemote.stream()
				.filter(projet -> {
					return listeMotsCles.parallelStream().anyMatch(motCle -> projet.getObjetCommande().toLowerCase().contains(motCle)) ;
				}).collect(Collectors.toList()) ;	
		}catch(IOException ex) {
			log.error(String.format("Erreur survenue lors de la récupération  des CNE Pridesoft pour l'année %s", year), ex);
			return new ArrayList<CneRemote>() ;
		}
    }
    
    
   private Long enregisterProjetsTic (List<CneRemote> listeCNEsRemote){
	   
	   log.info(String.format("Lancement de l'enregistrement des bons de commande récupérés %s", listeCNEsRemote.size()));
	   

	   final List<Region> listeRegions = regionService.rechercherTout() ;
	   final List<MaitreOuvrage> listeMaitresOuvrage = maitreOuvrageService.rechercherTout() ;
		
	   return listeCNEsRemote.parallelStream()
			   		.map(cneR  -> {
			   		 String regionSelectionnee = listeRegions.stream()
								.peek(System.out::println)
								.filter(region -> region.getId().equals(String.valueOf(cneR.getRegion())))
								.map(Region::getLibelle)
								.findFirst()
								.orElse(null) ;

						String maitreOuvrageSelectionne = listeMaitresOuvrage.stream()
								.filter(maitreOuvrage -> maitreOuvrage.getId().equals(String.valueOf(cneR.getMaitreOuvrage())))
								.map(MaitreOuvrage::getLibelle)
								.findFirst()
								.orElse(null) ;
			   				Cne cne = Cne.builder()
			   						.libelle(cneR.getObjetCommande())
			   						.codeProjet(String.valueOf(cneR.getId()))
			   						.dateSignature(cneR.getDateSignature())
			   						.montant(BigDecimal.valueOf(cneR.getMontant()))
			   						.idRegion(cneR.getRegion() == null ? null : String.valueOf(cneR.getRegion()))
			   						.libelleRegion(regionSelectionnee)
			   						.idMaitreOuvrage(cneR.getMaitreOuvrage() == null ? null :  String.valueOf(cneR.getMaitreOuvrage()))
			   						.libelleMaitreOuvrage(maitreOuvrageSelectionne)
			   						.build() ;
			   				return cneService.creerEtValider(cne) ;
			   		}).count() ;
    }
   
   
}
