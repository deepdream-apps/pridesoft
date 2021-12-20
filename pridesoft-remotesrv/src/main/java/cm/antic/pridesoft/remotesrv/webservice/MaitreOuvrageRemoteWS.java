package cm.antic.pridesoft.remotesrv.webservice;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cm.antic.pridesoft.datamodel.exceptions.RemoteEntityNotFoundException;
import cm.antic.pridesoft.datamodel.remote.MaitreOuvrageRemote;
import cm.antic.pridesoft.remotesrv.service.MaitreOuvrageRemoteService;
import lombok.extern.log4j.Log4j2;
@Log4j2
@RestController
@RequestMapping("/ws/maitreouvrage")
public class MaitreOuvrageRemoteWS {
	private MaitreOuvrageRemoteService maitreOuvrageRemoteService ;
	
	public MaitreOuvrageRemoteWS(MaitreOuvrageRemoteService maitreOuvrageRemoteService) {
		this.maitreOuvrageRemoteService = maitreOuvrageRemoteService ;
	}
	
	@GetMapping("/id/{id}")
	public Optional<MaitreOuvrageRemote> getMaitreOuvrage (@PathVariable("id") Long id) {
		log.info(String.format("Recherche du maitre d'ouvrage ", id));
		return maitreOuvrageRemoteService.rechercher(id) ;
	}
	
	
	@GetMapping("/all")
	public List<MaitreOuvrageRemote> getMaitreOuvrages() {
		log.info("Recherche de tous les maitres d'ouvrage ");
		return maitreOuvrageRemoteService.rechercherTout() ;
	}
	
	
	 @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Maître d'ouvrage not found")  // 409
	 @ExceptionHandler(RemoteEntityNotFoundException.class)
	 public void handleMaitreOuvrageNotFound() {
		 log.error("Maître d'ouvrage not found") ;
	 }
	
}
