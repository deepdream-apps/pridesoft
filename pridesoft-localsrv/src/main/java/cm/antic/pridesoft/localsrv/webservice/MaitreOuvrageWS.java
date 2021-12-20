package cm.antic.pridesoft.localsrv.webservice;

import java.util.List;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cm.antic.pridesoft.datamodel.exceptions.LocalEntityNotFoundException;
import cm.antic.pridesoft.datamodel.local.MaitreOuvrage;
import cm.antic.pridesoft.localsrv.service.MaitreOuvrageService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/maitreouvrage")
public class MaitreOuvrageWS {
	private MaitreOuvrageService maitreOuvrageService ;
	
	
	public MaitreOuvrageWS(MaitreOuvrageService maitreOuvrageService) {
		this.maitreOuvrageService = maitreOuvrageService;
	}


	@PostMapping("/creation")
	public MaitreOuvrage creer (@RequestBody MaitreOuvrage maitreOuvrage) {
		return  maitreOuvrageService.creer(maitreOuvrage) ;
	}
	
	
	@PutMapping("/modification")
	public MaitreOuvrage modifier (@RequestBody MaitreOuvrage maitreOuvrage) {
		return  maitreOuvrageService.modifier(maitreOuvrage) ;
	}
	
	
	@GetMapping("/id/{id}")
	public MaitreOuvrage getMaitreOuvrage (@PathVariable("id") Long id) {
		log.info(String.format("Recherche du maître d'ouvrage d'id %s ", id));
		return maitreOuvrageService.rechercher(id) 
				                   .map(Function.identity())
				                   .orElseThrow(LocalEntityNotFoundException::new);
	}
	
	
	@GetMapping("/all")
	public List<MaitreOuvrage> getMaitreOuvrages() {
		log.info("Recherche de tous les maîtres d'ouvrage") ;
		return maitreOuvrageService.rechercherTout() ;
	}
	
	
	@DeleteMapping("/id/{id}")
	public void supprimer (@PathVariable("id") Long id) {
		log.info(String.format("Suppression du maître d'ouvre d'id %s ", id)) ;
		MaitreOuvrage maitreOuvrage = MaitreOuvrage.builder()
				.id(id)
				.build() ;
		maitreOuvrageService.supprimer(maitreOuvrage) ;
	}
	
	
	@ExceptionHandler(LocalEntityNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public void handeEntityNotFoundException() {
		
	}
		
}
