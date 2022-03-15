package cm.antic.pridesoft.web.controlers;
import java.util.Arrays;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import cm.antic.pridesoft.datamodel.local.MaitreOuvrage;
import cm.antic.pridesoft.datamodel.local.Utilisateur;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Controller
@SessionAttributes({"utilisateurCourant", "localUrl"})
@RequestMapping("/maitreouvrage")
public class MaitreOuvrageCtrl {
	@Autowired
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	
	@GetMapping ("/liste")
	public String getAll (Model model, @SessionAttribute("localUrl") String localUrl) {
		log.info("Recherche de la liste des maitres d'ouvrage") ;
		ResponseEntity<MaitreOuvrage[]> response = rest.getForEntity(localUrl+"/ws/maitreouvrage/liste", MaitreOuvrage[].class) ;
		List<MaitreOuvrage> listeMaitresOuvrage = Arrays.asList(response.getBody());
		model.addAttribute("listeMaitresOuvrage", listeMaitresOuvrage) ;
	    return "maitreouvrage/liste" ; 
	}
	
	
	@GetMapping ("/init-ajout")
	public String initialiserAjout (Model model) {
		log.info("Initialiser l'ajout d'un maitre d'ouvrage") ;
		model.addAttribute("maitreOuvrage", new MaitreOuvrage()) ;
	    return "maitreouvrage/ajout" ; 
	}
	
	
	@PostMapping("/ajout")
	public String ajouter (Model model, MaitreOuvrage maitreouvrage, @SessionAttribute("localUrl") String localUrl) {
		ResponseEntity<MaitreOuvrage> response = rest.postForEntity(localUrl+"/ws/maitreouvrage/ajout", 
				maitreouvrage, MaitreOuvrage.class) ;
		return "redirect:/maitreouvrage/liste" ;
	}
	
	
	@GetMapping ("/init-modification/{id}")
	public String initialiserModification (Model model, @PathVariable("id") Long id, @SessionAttribute("localUrl") String localUrl) {
		log.info("Initialisation de la modification du maitre d'ouvrage") ;
		MaitreOuvrage maitreOuvrageExistant = rest.getForObject(localUrl+"/ws/maitreouvrage/id/{id}", MaitreOuvrage.class, id) ;
		model.addAttribute("maitreOuvrageExistant", maitreOuvrageExistant) ;
	    return "maitreouvrage/modification" ; 
	}
	
	
	@PutMapping("/modification")
	public String modifier (Model model, MaitreOuvrage maitreouvrage, @SessionAttribute("localUrl") String localUrl) {
		ResponseEntity<MaitreOuvrage> response = rest.exchange(localUrl+"/ws/maitreouvrage/modification", 
				HttpMethod.PUT, new HttpEntity<MaitreOuvrage>(maitreouvrage), MaitreOuvrage.class) ;
		return "redirect:/maitreouvrage/liste" ;
	}
	
	 
	@GetMapping ("/telechargement")
	public String charger (Model model, @SessionAttribute("localUrl") String localUrl)  throws Exception {
		log.info("Chargement des MaitreOuvrages ") ;
		ResponseEntity<MaitreOuvrage[]> response = rest.getForEntity(localUrl+"/ws/maitreouvrage/all", MaitreOuvrage[].class) ;
		List<MaitreOuvrage> listeMaitreOuvrage = Arrays.asList(response.getBody());
		rest.postForEntity(env.getProperty("app.localsrv.host")+":"+env.getProperty("app.localsrv.port")+"/ws/maitreouvrage/telecharger", listeMaitreOuvrage, Integer.class);			
		return "redirect:/maitreouvrage/index" ;
	 }
	
	
	@GetMapping ("/id/{id}")
	public String initialiserDetails (Model model, @PathVariable("id") Long id,  
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute("localUrl") String localUrl) throws Exception {
		MaitreOuvrage maitreOuvrageExistant = rest.getForObject(localUrl+"/ws/maitreouvrage/id/{id}", 
				MaitreOuvrage.class,  id) ;
		model.addAttribute("maitreOuvrageExistant", maitreOuvrageExistant) ;
		return "maitreouvrage/details" ;
	}
	
	
	
	@GetMapping ("/suppression/{id}")
	public String supprimer (Model model, @PathVariable("id") String id,  
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute("localUrl") String localUrl) throws Exception {
		MaitreOuvrage maitreouvrage = MaitreOuvrage.builder()
				.id(id)
				.build() ;
		ResponseEntity<MaitreOuvrage>  response = rest.exchange(localUrl+"/ws/maitreouvrage/suppression", HttpMethod.DELETE,   new HttpEntity<MaitreOuvrage>(maitreouvrage), MaitreOuvrage.class);
		return "redirect:/maitreouvrage/liste" ;
	}
	
}
