package cm.antic.pridesoft.localsrv.webservice;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cm.antic.pridesoft.datamodel.exceptions.LocalEntityNotFoundException;
import cm.antic.pridesoft.datamodel.local.Cne;
import cm.antic.pridesoft.datamodel.remote.CneRemote;
import cm.antic.pridesoft.localsrv.service.CneService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/cne")
public class CneWS {
	private CneService cneService ;
	private RestTemplate restTemplate ;
	private Environment env ;
	
	public CneWS(CneService cneService, RestTemplate restTemplate, Environment env) {
		this.cneService = cneService;
		this.restTemplate = restTemplate ;
		this.env = env ;
	}
	
	

	@GetMapping("/id/{id}")
	public Cne getCne (@PathVariable("id") Long id) {
		log.info(String.format("Recherche du cne d'id %s", id));
		return cneService.rechercher(id)
					     .map(Function.identity())
					     .orElseThrow(LocalEntityNotFoundException::new);
	}
	
	

	@GetMapping("/all")
	public List<Cne> getCnes() {
		log.info("Recherche de toutes les CNE ") ;
		return cneService.rechercherTout() ;
	}
	
	
	@GetMapping("/{dateDebut}/{dateFin}")
	public List<Cne> rechercherPlusieurs(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		return cneService.rechercher(debut, fin) ;
	}
	
	
	@GetMapping("/validation-masse/{dateDebut}/{dateFin}")
	public List<Cne> validerMasse(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin) {
		log.info(String.format("Validation des CNE en masse de %s a %s ", dateDebut, dateFin));
		LocalDate debut = LocalDate.parse(dateDebut, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
		LocalDate fin = LocalDate.parse(dateFin, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
		return cneService.validerEnMasse(debut, fin) ;
	}
	
	
	@PutMapping("/validation")
	public Cne valider(@RequestBody Cne cne) {
		log.info(String.format("Validation de CNI d'id %s ", cne.getId()));
		return cneService.valider(cne) ;
	}
	
	
	@PutMapping("/invalidation")
	public Cne invalider(@RequestBody Cne cne) {
		log.info(String.format("Invalidation de CNE d'id %s ", cne.getId()));
		return cneService.invalider(cne) ;
	}
	
	
	@GetMapping("/telechargement/{dateDebut}/{dateFin}")
	public List<Cne> telecharger (@PathVariable("dateDebut") String dateDebut, 
			@PathVariable("dateFin") String dateFin) throws IOException{
		try (Stream<String> streamMotsCle = Files.lines(Paths.get(new ClassPathResource(
	    	      "data/keywords").getFile().getAbsolutePath()))){
			log.info(String.format("Recherche de la liste des projets de %s  a %s", dateDebut, dateFin)) ;
					
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
			LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
			LocalDate fin = LocalDate.parse(dateFin, formatter) ;
			
			ResponseEntity<CneRemote[]> response = restTemplate.getForEntity("http://"+env.getProperty("app.remotesrv")+"/ws/cne/{date1}/{date2}", 
					CneRemote[].class, debut, fin) ;
			
			List<CneRemote> listeCNEs = Arrays.asList(response.getBody());
			
			log.info(String.format("Nombre de cne recuperees : %s", listeCNEs.size())) ;
			
			List<CneRemote> listeCNEsFiltres = listeCNEs.stream()
					.filter(cneR -> {
						return streamMotsCle.anyMatch(motCle ->cneR.getObjetCommande().contains(motCle)) ;
					}).collect(Collectors.toList()) ;
			
			return listeCNEsFiltres.stream()
							   .map(cneR  -> {
								    Cne cne = Cne.builder()
								    		.libelle(cneR.getObjetCommande())
								    		.dateSignature(cneR.getDateSignature())
								    		.montant(cneR.getMontantTtcCommande())
								    		.idRegion(cneR.getRegion() == null ? null : cneR.getRegion())
								    		.libelleRegion(null)
								    		.idMaitreOuvrage(cneR.getMaitreOuvrage())
								    		.sigleMaitreOuvrage(null)
								    		.build() ;
								    return cneService.creer(cne) ;
							   }).collect(Collectors.toList()) ;
			
		}catch(IOException ex) {
			log.error(ex.getMessage(), ex) ;
			throw ex ;
		}
	}

}
