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

import cm.antic.pridesoft.datamodel.local.Region;
import cm.antic.pridesoft.datamodel.local.Utilisateur;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Controller
@SessionAttributes({"utilisateurCourant", "localUrl"})
@RequestMapping("/region")
public class RegionCtrl {
	@Autowired
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	
	@GetMapping ("/liste")
	public String getAll (Model model, @SessionAttribute("localUrl") String localUrl) {
		log.info("Recherche de la liste des regions") ;
		ResponseEntity<Region[]> response = rest.getForEntity(localUrl+"/ws/region/liste", Region[].class) ;
		List<Region> listeRegions = Arrays.asList(response.getBody());
		model.addAttribute("listeRegions", listeRegions) ;
	    return "region/liste" ; 
	}
	
	
	@GetMapping ("/init-ajout")
	public String initialiserAjout (Model model) {
		log.info("Initialiser l'ajout de region") ;
		model.addAttribute("region", new Region()) ;
	    return "region/ajout" ; 
	}
	
	
	@PostMapping("/ajout")
	public String ajouter (Model model, Region region, @SessionAttribute("localUrl") String localUrl) {
		ResponseEntity<Region> response = rest.postForEntity(localUrl+"/ws/region/ajout", 
				region, Region.class) ;
		return "redirect:/region/liste" ;
	}
	
	
	@GetMapping ("/init-modification/{id}")
	public String initialiserModification (Model model, @PathVariable("id") Long id, 
			@SessionAttribute("localUrl") String localUrl) {
		log.info("Initialisation de la modification de la region") ;
		Region regionExistante = rest.getForObject(localUrl+"/ws/region/id/{id}", Region.class, id) ;
		model.addAttribute("regionExistante", regionExistante) ;
	    return "region/modification" ; 
	}
	
	
	@PutMapping("/modification")
	public String modifier (Model model, Region region, @SessionAttribute("localUrl") String localUrl) {
		ResponseEntity<Region> response = rest.exchange(localUrl+"/ws/region/modification", 
				HttpMethod.PUT, new HttpEntity<Region>(region), Region.class) ;
		return "redirect:/region/liste" ;
	}
	
	 
	@GetMapping ("/chargement")
	public String charger (Model model, @SessionAttribute("localUrl") String localUrl)  throws Exception {
		log.info("Chargement des Regions ") ;
		ResponseEntity<Region[]> response = rest.getForEntity(env.getProperty("app.remotesrv")+"/ws/region/all", Region[].class) ;
		List<Region> listeRegion = Arrays.asList(response.getBody());
		rest.postForEntity(env.getProperty("app.localsrv.host")+":"+env.getProperty("app.localsrv.port")+"/ws/region/telecharger", listeRegion, Integer.class);			
		return "redirect:/region/index" ;
	 }
	
	
	@GetMapping ("/id/{id}")
	public String initialiserDetails (Model model, @PathVariable("id") Long id,  
			@SessionAttribute ("utilisateurCourant") Utilisateur utilisateurCourant,
			@SessionAttribute("localUrl") String localUrl) throws Exception {
		Region regionExistante = rest.getForObject(localUrl+"/ws/region/id/{id}", 
				Region.class,  id) ;
		model.addAttribute("regionExistante", regionExistante) ;
		return "region/details" ;
	}
	
	
	
	@GetMapping ("/suppression/{id}")
	public String supprimer (Model model, @PathVariable("id") String id, @SessionAttribute("localUrl") String localUrl) throws Exception {
		Region region = Region.builder()
				.id(id)
				.build() ;
		ResponseEntity<Region>  response = rest.exchange(localUrl+"/ws/region/suppression", HttpMethod.DELETE,   new HttpEntity<Region>(region), Region.class);
		return "redirect:/region/liste" ;
	}
	
}
