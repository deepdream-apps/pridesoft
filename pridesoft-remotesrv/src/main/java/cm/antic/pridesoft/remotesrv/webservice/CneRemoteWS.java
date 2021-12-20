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
import cm.antic.pridesoft.datamodel.remote.CneRemote;
import cm.antic.pridesoft.remotesrv.service.CneRemoteService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/cne")
public class CneRemoteWS {
	private CneRemoteService cneRemoteService ;
	
	public CneRemoteWS(CneRemoteService cneRemoteService) {
		this.cneRemoteService = cneRemoteService ;
	}
	
	@GetMapping("/id/{id}")
	public CneRemote getCne (@PathVariable("id") Long id) {
		log.info("Recherche du cne d'id "+id);
		return cneRemoteService.rechercher(id)
				               .map(Function.identity())
				               .orElseThrow(RemoteEntityNotFoundException::new) ;
	}

	
	 @GetMapping("/{date1}/{date2}")
	 public List<CneRemote> getListeCne2( @PathVariable ("date1") String date1, @PathVariable ("date2") String date2){
		 log.info(String.format("Recherche de CNEs de  %s a %s", date1, date2)) ;
	     LocalDate dateDebut = LocalDate.parse(date1, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
	     LocalDate dateFin = LocalDate.parse(date2, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
	     return cneRemoteService.rechercher(dateDebut, dateFin);
	 }
	 
	 
	 @GetMapping("/year/{year}")
	 public List<CneRemote> getListeCne3( @PathVariable ("year") Integer year){
		 log.info(String.format("Recherche de CNEs de l'année  %s", year)) ;
	     LocalDate dateDebut = LocalDate.parse("01-01-"+year, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
	     LocalDate dateFin = LocalDate.parse("31-12-"+year, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
	     return cneRemoteService.rechercher(dateDebut, dateFin);
	 }
	 
	 
	 @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="CNE not found")  // 409
	 @ExceptionHandler(RemoteEntityNotFoundException.class)
	 public void handleCNENotFound() {
		 log.error("CNE not found") ;
	 }
}
