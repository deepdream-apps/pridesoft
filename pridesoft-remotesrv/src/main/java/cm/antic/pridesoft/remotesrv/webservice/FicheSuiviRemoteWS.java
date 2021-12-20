package cm.antic.pridesoft.remotesrv.webservice;

import java.io.Serializable;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cm.antic.pridesoft.datamodel.exceptions.RemoteEntityNotFoundException;
import cm.antic.pridesoft.datamodel.remote.FicheSuiviRemote;
import cm.antic.pridesoft.remotesrv.service.FicheSuiviRemoteService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/fichesuivi")
public class FicheSuiviRemoteWS implements Serializable{
	private FicheSuiviRemoteService ficheSuiviRemoteService ;
	
	public FicheSuiviRemoteWS(FicheSuiviRemoteService ficheSuiviRemoteService) {
		this.ficheSuiviRemoteService = ficheSuiviRemoteService ;
	}
	
	@GetMapping("/fichesuivi/codeprojet/{codeProjet}")
	public Optional<FicheSuiviRemote> getProjet(@PathVariable ("codeProjet") String codeProjet) {
		log.info(String.format("Recherche de fiches de suivi de code projet %s", codeProjet)) ;
		return ficheSuiviRemoteService.rechercher(codeProjet) ;
	}
	
	@GetMapping("/fichesuivi/{annee}")
	public List<FicheSuiviRemote> getListeProjects1(@PathVariable("annee") int annee) {
		log.info(String.format("Recherche de fiches de suivi de l'année %s", annee)) ;
		LocalDate date1 = LocalDate.of(annee, Month.JANUARY, 1) ;
		LocalDate date2 = LocalDate.of(annee, Month.DECEMBER, 31) ;
		return ficheSuiviRemoteService.rechercher(date1, date2) ;
	}
	
	@GetMapping("/fichesuivi/{date1}/{date2}")
	public List<FicheSuiviRemote> getListeProjets2(@PathVariable ("date1") String date1,
			@PathVariable ("date2") String date2) {
		log.info(String.format("Recherche de fiches de suivi du %s au %s", date1, date2)) ;
		LocalDate dateDebut = LocalDate.parse(date1, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
		LocalDate dateFin = LocalDate.parse(date2, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
		return ficheSuiviRemoteService.rechercher(dateDebut, dateFin) ;
	}
	
	@GetMapping("/fichesuivi2/{date1}/{date2}")
	public List<FicheSuiviRemote> getListeProjets(@PathVariable ("date1") String date1,
			@PathVariable ("date2") String date2) {
		
		log.info(String.format("Recherche de fiches de suivi filtrees du %s au %s", date1, date2)) ;
		
		final List<String> listMotsCles = Arrays.asList("informatique", "moteur de recherche", "logiciel", 
				"numérique", "digitalisation", " TIC ", "ordinateur", "laptop", "imprimante", 
				"baie de brassage", "salle serveur" ) ;
		
		LocalDate dateDebut = LocalDate.parse(date1, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
		
		LocalDate dateFin = LocalDate.parse(date2, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
		
		List<FicheSuiviRemote> listeProjets =  ficheSuiviRemoteService.rechercher(dateDebut, dateFin) ;
		
		List<FicheSuiviRemote> listeProjetsTic = listeProjets.stream().filter(p -> {
			return listMotsCles.stream().anyMatch(motCle  ->  p.getDesignation().contains(motCle)) ;
		 }).collect(Collectors.toList()) ;
		
		log.info("Nombre de fiches de suivi téléchargés : "+ listeProjetsTic.size()) ;
		
		return listeProjetsTic ;
	}
	
	
	 @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Fiche de suivi not found")  // 409
	 @ExceptionHandler(RemoteEntityNotFoundException.class)
	 public void handleFicheSuiviNotFound() {
		 log.error("Fiche de suivi not found") ;
	 }
	

}
