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
import cm.antic.pridesoft.datamodel.local.Categorie;
import cm.antic.pridesoft.localsrv.service.CategorieService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/categorie")
public class CategorieWS {
	private CategorieService categorieService;
	
	public CategorieWS(CategorieService categorieService) {
		super();
		this.categorieService = categorieService;
	}


	@PostMapping("/ajout")
	public Categorie ajouter (@RequestBody Categorie categorie) {
		return categorieService.creer(categorie) ; 
	}
	
	
	@PutMapping("/modification")
	public Categorie modifier (@RequestBody Categorie categorie) {
		return categorieService.modifier(categorie) ;
	}
	
	
	@DeleteMapping("/suppression")
	public void supprimer (@RequestBody Categorie categorie) {
		 categorieService.supprimer(categorie) ;
	}
	
	
	@GetMapping("/id/{id}")
	public Categorie rechercher(@PathVariable("id") String id) {
		log.info(String.format("Recherche de la categorie d'id %s", id));
		return categorieService.rechercher(id)
							   .map(Function.identity())
							   .orElseThrow(LocalEntityNotFoundException::new) ;
	}
	
	@GetMapping("/liste")
	public List<Categorie> getCategories() {
		return categorieService.rechercherTout() ;
	}
	
	@ExceptionHandler(LocalEntityNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public void handeEntityNotFoundException() {
		
	}
}
