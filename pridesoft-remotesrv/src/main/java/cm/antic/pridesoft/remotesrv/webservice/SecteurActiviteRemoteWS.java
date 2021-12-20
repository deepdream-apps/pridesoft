package cm.antic.pridesoft.remotesrv.webservice;
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
import cm.antic.pridesoft.datamodel.remote.SecteurActiviteRemote;
import cm.antic.pridesoft.remotesrv.service.SecteurActiviteRemoteService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/secteuractivite")
public class SecteurActiviteRemoteWS {
	private SecteurActiviteRemoteService secteurActiviteRemoteService ;
	
	public SecteurActiviteRemoteWS(SecteurActiviteRemoteService secteurActiviteRemoteService) {
		this.secteurActiviteRemoteService = secteurActiviteRemoteService ;
	}
	

	@GetMapping("/id/{id}")
	public SecteurActiviteRemote getRegion (@PathVariable("id") Long id) {
		log.info(String.format("Recherche du secteur d'activite d'id %s", id));
		return secteurActiviteRemoteService.rechercher(id)
				                  .map(Function.identity())
				                  .orElseThrow(RemoteEntityNotFoundException::new) ;
	}
	
	
	@GetMapping("/sigle/{sigle}")
	public SecteurActiviteRemote getRegion (@PathVariable("sigle") String sigle) {
		log.info(String.format("Recherche de la region de sigle %s", sigle));
		return secteurActiviteRemoteService.rechercher(sigle) 
				                  .map(Function.identity())
				                  .orElseThrow(RemoteEntityNotFoundException::new) ;
	}


	@GetMapping("/all")
	public List<SecteurActiviteRemote> getRegion() {
		log.info("Recherche de tous les secteurs d'activite ");
		return secteurActiviteRemoteService.rechercherTout() ;
	}
	
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Secteur d'activite not found")  // 409
	@ExceptionHandler(RemoteEntityNotFoundException.class)
	public void handleSecteurNotFound() {
		log.error("Secteur d'activite not found") ;
	}
	
}
