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
import cm.antic.pridesoft.datamodel.local.SecteurActivite;
import cm.antic.pridesoft.localsrv.service.SecteurActiviteService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/secteuractivite")
public class SecteurActiviteWS {
	private SecteurActiviteService secteurActiviteService ;
	
	public SecteurActiviteWS(SecteurActiviteService secteurActiviteService) {
		this.secteurActiviteService = secteurActiviteService;
	}


	@PostMapping("/ajout")
	public SecteurActivite ajouter (@RequestBody SecteurActivite secteurActivite) {
		return secteurActiviteService.creer(secteurActivite) ; 
	}
	
	
	@PutMapping("/modification")
	public SecteurActivite modifier (@RequestBody SecteurActivite secteurActivite) {
		return secteurActiviteService.modifier(secteurActivite) ;
	}
	
	
	@DeleteMapping("/suppression")
	public void supprimer (@RequestBody SecteurActivite secteurActivite) {
		secteurActiviteService.supprimer(secteurActivite) ;
	}
	
	
	@GetMapping("/id/{id}")
	public SecteurActivite rechercher(@PathVariable("id") String id) {
		log.info(String.format("Recherche de la categorie d'id %s", id));
		return secteurActiviteService.rechercher(id)
							   .map(Function.identity())
							   .orElseThrow(LocalEntityNotFoundException::new) ;
	}
	
	@GetMapping("/liste")
	public List<SecteurActivite> getSecteursActivite() {
		return secteurActiviteService.rechercherTout() ;
	}
	
	@ExceptionHandler(LocalEntityNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public void handeEntityNotFoundException() {
		
	}
}
