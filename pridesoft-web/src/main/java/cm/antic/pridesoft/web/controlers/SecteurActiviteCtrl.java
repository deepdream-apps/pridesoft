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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import cm.antic.pridesoft.datamodel.local.SecteurActivite;
import cm.antic.pridesoft.datamodel.local.Utilisateur;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Controller
@SessionAttributes({"utilisateurCourant", "localUrl"})
@RequestMapping("/secteuractivite")
public class SecteurActiviteCtrl {
	@Autowired
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	
	@GetMapping ("/liste")
	public String getAll (Model model, @SessionAttribute("localUrl") String localUrl) {
		log.info("Recherche de la liste des secteurs d'activite") ;
		ResponseEntity<SecteurActivite[]> response = rest.getForEntity(localUrl+"/ws/secteuractivite/liste", SecteurActivite[].class) ;
		List<SecteurActivite> listeSecteursActivite = Arrays.asList(response.getBody());
		model.addAttribute("listeSecteursActivite", listeSecteursActivite) ;
	    return "secteuractivite/liste" ; 
	}
	
	
	@GetMapping ("/init-ajout")
	public String initialiserAjout (Model model) {
		log.info("Initialiser l'ajout du secteur d'activite") ;
		model.addAttribute("secteurActivite", new SecteurActivite()) ;
	    return "secteuractivite/ajout" ; 
	}
	
	
	@PostMapping("/ajout")
	public String ajouter (Model model, SecteurActivite secteuractivite, @SessionAttribute("localUrl") String localUrl) {
		ResponseEntity<SecteurActivite> response = rest.postForEntity(localUrl+"/ws/secteuractivite/ajout", 
				secteuractivite, SecteurActivite.class) ;
		return "redirect:/secteuractivite/liste" ;
	}
	
	
	@GetMapping ("/init-modification/{id}")
	public String initialiserModification (Model model, @PathVariable("id") String id, 
			@SessionAttribute("localUrl") String localUrl) {
		log.info("Initialisation de la modification du secteur d'activite") ;
		SecteurActivite secteurActiviteExistant = rest.getForObject(localUrl+"/ws/secteuractivite/id/{id}", SecteurActivite.class, id) ;
		model.addAttribute("secteuractiviteExistant", secteurActiviteExistant) ;
	    return "secteuractivite/modification" ; 
	}
	
	
	@PostMapping("/modification")
	public String modifier (Model model, SecteurActivite secteuractivite, @SessionAttribute("localUrl") String localUrl) {
		ResponseEntity<SecteurActivite> response = rest.exchange(localUrl+"/ws/secteuractivite/modification", 
				HttpMethod.PUT, new HttpEntity<SecteurActivite>(secteuractivite), SecteurActivite.class) ;
		return "redirect:/secteuractivite/liste" ;
	}
	
	 
	@GetMapping ("/chargement")
	public String charger (Model model)  throws Exception {
		log.info("Chargement des SecteurActivites ") ;
		ResponseEntity<SecteurActivite[]> response = rest.getForEntity(env.getProperty("app.remotesrv")+"/ws/secteuractivite/all", SecteurActivite[].class) ;
		List<SecteurActivite> listeSecteurActivite = Arrays.asList(response.getBody());
		rest.postForEntity(env.getProperty("app.localsrv.host")+":"+env.getProperty("app.localsrv.port")+"/ws/secteuractivite/telecharger", listeSecteurActivite, Integer.class);			
		return "secteuractivite/index" ;
	 }
	
	
	@GetMapping ("/id/{id}")
	public String initialiserDetails (Model model, @PathVariable("id") String id,  
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute("localUrl") String localUrl) throws Exception {
		SecteurActivite secteurActiviteExistant = rest.getForObject(localUrl+"/ws/secteuractivite/id/{id}", 
				SecteurActivite.class,  id) ;
		model.addAttribute("secteurActiviteExistant", secteurActiviteExistant) ;
		return "secteuractivite/details" ;
	}
	
	
	@GetMapping ("/init-suppression/{id}")
	public String initierSuppression (Model model,  @PathVariable("id") String id,
			@SessionAttribute("localUrl") String localUrl) {
		SecteurActivite secteurActiviteExistant = rest.getForObject(localUrl+"/ws/secteuractivite/id/{id}", 
				SecteurActivite.class,  id) ;
		model.addAttribute("secteurActiviteExistant", secteurActiviteExistant) ;
		return "secteuractivite/suppression" ;
	}
	
	
	@PostMapping ("/suppression")
	public String supprimer (Model model, SecteurActivite secteurActiviteExistant, @SessionAttribute("localUrl") String localUrl) throws Exception {
		SecteurActivite secteuractivite = SecteurActivite.builder()
				.id(secteurActiviteExistant.getId())
				.build() ;
		ResponseEntity<SecteurActivite>  response = rest.exchange(localUrl+"/ws/secteuractivite/suppression", HttpMethod.DELETE,   new HttpEntity<SecteurActivite>(secteuractivite), SecteurActivite.class);
		return "redirect:/secteuractivite/liste" ;
	}
	
}
