package cm.antic.pridesoft.web.controlers;
import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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

import cm.antic.pridesoft.datamodel.local.Categorie;

import cm.antic.pridesoft.datamodel.local.Utilisateur;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@SessionAttributes({"listeCategories", "localUrl"})
@RequestMapping("/categorie")
public class CategorieCtrl {
	@Autowired
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	

	@GetMapping ("/liste")
	public String getListe (Model model,  @SessionAttribute("localUrl") String localUrl) {
		log.info("Recuperation de la liste des categories ") ;
		ResponseEntity<Categorie[]> response = rest.getForEntity(localUrl+"/ws/categorie/liste", Categorie[].class) ;
		List<Categorie> listeCategories = Arrays.asList(response.getBody());
		model.addAttribute("listeCategories", listeCategories) ;
	    return "categorie/liste" ; 
	}
	 
	 
	@GetMapping ("/init-ajout")
	public String initierAjout (Model model) {
		model.addAttribute("categorie", new Categorie()) ;
		return "categorie/ajout" ;
	}
	
	
	@PostMapping ("/ajout")
	public String ajouter (Model model, @SessionAttribute("localUrl") String localUrl, 
			@SessionAttribute ("listeCategories") List<Categorie> listeCategories,  Categorie categorie)  {
		ResponseEntity<Categorie> response = rest.postForEntity(localUrl+"/ws/categorie/ajout", categorie, Categorie.class);			
		return "redirect:/categorie/liste" ;
	}
	
	
	@GetMapping ("/init-modification/{id}")
	public String initierModification (Model model, @SessionAttribute("localUrl") String localUrl, 
			@SessionAttribute("utilisateurCourant") Utilisateur utilisateurCourant,  
			@PathVariable("id") String id) {
		Categorie categorieExistante = rest.getForObject(localUrl+"/ws/categorie/id/{id}", 
				Categorie.class,  id) ;
		model.addAttribute("categorieExistante", categorieExistante) ;
		return "categorie/modification" ;
	}
	
	
	@PostMapping ("/modification")
	public String modifier (Model model,  @SessionAttribute("localUrl") String localUrl,
			Categorie categorieExistante,  @SessionAttribute ("listeCategories") List<Categorie> listeCategories) {
		ResponseEntity<Categorie> response = rest.exchange(localUrl+"/ws/categorie/modification", 
				HttpMethod.PUT, new HttpEntity<Categorie>(categorieExistante), Categorie.class) ;
		
		List<Categorie> listeCategories2 = listeCategories.stream()
							.map(categorie -> {
								return categorie.equals(response.getBody()) ? response.getBody() : categorie ;
							})
							.collect(Collectors.toList()) ;
		model.addAttribute("listeCategories", listeCategories2) ;
		return "redirect:/categorie/liste" ;
	}
	
	
	@GetMapping ("/init-suppression/{id}")
	public String initierSuppression (Model model, @SessionAttribute("localUrl") String localUrl, 
			@SessionAttribute("utilisateurCourant") Utilisateur utilisateurCourant,  
			@PathVariable("id") String id) {
		Categorie categorieExistante = rest.getForObject(localUrl+"/ws/categorie/id/{id}", 
				Categorie.class,  id) ;
		model.addAttribute("categorieExistante", categorieExistante) ;
		return "categorie/suppression" ;
	}
	
	
	@PostMapping ("/suppression")
	public String supprimer (Model model,   @SessionAttribute("localUrl") String localUrl, 
			Categorie categorieExistante, @SessionAttribute ("listeCategories") List<Categorie> listeCategories) {
		ResponseEntity<Categorie> response = rest.exchange(localUrl+"/ws/categorie/suppression", 
				HttpMethod.DELETE, new HttpEntity<Categorie>(categorieExistante), Categorie.class) ;
		return "redirect:/categorie/liste" ;
	}
	
	
	@GetMapping ("/id/{id}")
	public String getCategorie (Model model,  @SessionAttribute("localUrl") String localUrl, @PathVariable("id") String id,  @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) {
		Categorie categorie = rest.getForObject(localUrl+"/ws/categorie/id/{id}", Categorie.class,  id) ;
		model.addAttribute("categorieExistante", categorie) ;
		return "categorie/details" ;
	}
	
	
	@GetMapping ("/delete/{id}")
	public String supprimer (Model model, @SessionAttribute("localUrl") String localUrl, 
			@PathVariable("id") String id,  @SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant) {
		Categorie categorie = Categorie.builder()
				.id(id)
				.build() ;
		rest.delete(localUrl+"/ws/categorie/suppression", categorie, Categorie.class);
		return "redirect:/categorie/liste" ;
	}
	
}
