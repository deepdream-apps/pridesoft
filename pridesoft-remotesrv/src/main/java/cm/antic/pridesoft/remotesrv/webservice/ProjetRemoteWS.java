package cm.antic.pridesoft.remotesrv.webservice;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cm.antic.pridesoft.datamodel.exceptions.RemoteEntityNotFoundException;
import cm.antic.pridesoft.datamodel.remote.ProjetRemote;
import cm.antic.pridesoft.remotesrv.service.ProjetRemoteService;
import lombok.extern.log4j.Log4j2;
@Log4j2
@RestController
@RequestMapping("/ws/projet")
public class ProjetRemoteWS {
	private ProjetRemoteService projetRemoteService ;

	public ProjetRemoteWS(ProjetRemoteService projetRemoteService) {
		this.projetRemoteService = projetRemoteService ;
	}
	
	
	@GetMapping("/codeprojet/{codeProjet}")
	public ProjetRemote getProjets (@PathVariable("codeProjet") String codeProjet) {
		log.info(String.format("Recherche des projets de code %s", codeProjet));
		return projetRemoteService.rechercher(codeProjet) 
				                  .map(Function.identity())
				                  .orElseThrow(RemoteEntityNotFoundException::new) ;
	}
	
	
	@GetMapping("/periode/{dateDebut}/{dateFin}")
	public List<ProjetRemote> getProjets (@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin) {
		log.info(String.format("Recherche des projets du %s au %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		return projetRemoteService.rechercher(debut, fin) ;
	}
	
	
	@GetMapping("/year/{year}")
	public List<ProjetRemote> getProjets (@PathVariable("year") int year) {
		log.info(String.format("Recherche des projets de %s", year));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") ;
		LocalDate debut = LocalDate.parse(year+"-01-01", formatter) ;
		LocalDate fin = LocalDate.parse(year+"-12-31", formatter) ;
		return projetRemoteService.rechercher(debut, fin) ;
	}
	
	
	 @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Projet not found")  // 409
	 @ExceptionHandler(RemoteEntityNotFoundException.class)
	 public void handleProjetNotFound() {
		 log.error("Projet not found") ;
	 }
}
