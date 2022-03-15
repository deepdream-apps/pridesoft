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
import cm.antic.pridesoft.datamodel.remote.PaysRemote;
import cm.antic.pridesoft.datamodel.remote.RegionRemote;
import cm.antic.pridesoft.remotesrv.service.RegionRemoteService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/region")
public class RegionRemoteWS {
	private RegionRemoteService regionRemoteService ;
	
	public RegionRemoteWS(RegionRemoteService regionRemoteService) {
		this.regionRemoteService = regionRemoteService ;
	}
	

	@GetMapping("/id/{id}")
	public RegionRemote getRegion (@PathVariable("id") Long id) {
		log.info(String.format("Recherche de la region d'id %s", id));
		return regionRemoteService.rechercher(id)
				                  .map(Function.identity())
				                  .orElseThrow(RemoteEntityNotFoundException::new) ;
	}
	
	
	@GetMapping("/code/{code}")
	public RegionRemote getRegion (@PathVariable("code") String code) {
		log.info(String.format("Recherche de la region de code %s", code));
		return regionRemoteService.rechercher(code) 
				                  .map(Function.identity())
				                  .orElseThrow(RemoteEntityNotFoundException::new) ;
	}


	@GetMapping("/pays/{idPays}")
	public List<RegionRemote> getRegions(@PathVariable("idPays") Long idPays) {
		log.info("Recherche des regions ");
		return regionRemoteService.rechercher(new PaysRemote(idPays, null)) ;
	}
	
	
	@GetMapping("/all")
	public List<RegionRemote> getRegions() {
		log.info("Recherche des regions ");
		return regionRemoteService.rechercher(new PaysRemote(1L, null)) ;
	}
	
	
	
	 @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Region not found")  // 409
	 @ExceptionHandler(RemoteEntityNotFoundException.class)
	 public void handleRegionNotFound() {
		 log.error("Region not found") ;
	 }
	
}
