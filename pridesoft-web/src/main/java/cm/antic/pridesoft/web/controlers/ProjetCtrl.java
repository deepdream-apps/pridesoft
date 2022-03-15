package cm.antic.pridesoft.web.controlers;
import java.time.LocalDate;

import java.time.Month;
import java.time.format.DateTimeFormatter;
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

import cm.antic.pridesoft.datamodel.local.Projet;
import cm.antic.pridesoft.datatransfer.local.DateDebutFinDTO;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@SessionAttributes({"utilisateurCourant", "listeProjets", "localUrl"})
@RequestMapping("/projet")
public class ProjetCtrl {
	@Autowired
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
	private DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd") ;
	
	@GetMapping ("/liste")
	public String getDefaultProjets (Model model, @SessionAttribute("localUrl") String localUrl) {
		int year = LocalDate.now().getYear() ;
		LocalDate dateDebut = LocalDate.of(year, Month.JANUARY, 1) ;
		LocalDate dateFin = LocalDate.of(year, Month.DECEMBER, 31) ;
		
		ResponseEntity<Projet[]> response = rest.getForEntity(localUrl+"/ws/projet/periode/{dateDebut}/{dateFin}", 
				Projet[].class, dateDebut.format(formatter), dateFin.format(formatter)) ;
		List<Projet> listeProjets = Arrays.asList(response.getBody());
		model.addAttribute("listeProjets", listeProjets) ;
		model.addAttribute("periode", new DateDebutFinDTO(dateDebut, dateFin)) ;
	    return "projet/liste" ; 
	}
	
	

	@PostMapping ("/liste")
	public String getProjets (Model model, DateDebutFinDTO periode, @SessionAttribute("localUrl") String localUrl) {
		LocalDate dateDebut = periode.getDateDebut() ;
		LocalDate dateFin = periode.getDateFin() ;
		ResponseEntity<Projet[]> response = rest.getForEntity(localUrl+"/ws/projet/periode/{dateDebut}/{dateFin}", 
				Projet[].class, dateDebut.format(formatter), dateFin.format(formatter)) ;
		List<Projet> listeProjets = Arrays.asList(response.getBody());
		model.addAttribute("listeProjets", listeProjets) ;
		model.addAttribute("periode", new DateDebutFinDTO(dateDebut, dateFin)) ;
		return "projet/liste" ; 
	}
	
	
	@GetMapping ("/telechargement")
	public String telecharger (Model model, @SessionAttribute("localUrl") String localUrl)  throws Exception {
		int year = LocalDate.now().getYear() ;
		LocalDate dateDebut = LocalDate.of(year, Month.JANUARY, 1) ;
		LocalDate dateFin = LocalDate.of(year, Month.DECEMBER, 31) ;
		ResponseEntity<Projet[]> response = rest.getForEntity(localUrl+"/ws/projet/telechargement/periode/{dateDebut}/{dateFin}", Projet[].class, 
				 dateDebut.format(formatter), 
				 dateFin.format(formatter)) ;
	
		List<Projet> listeProjets = Arrays.asList(response.getBody());
	
		model.addAttribute("listeProjets", listeProjets) ;
		model.addAttribute("periode", new DateDebutFinDTO(dateDebut, dateFin)) ;
		return "projet/telecharges" ;
	}
	
	
	
	@PostMapping ("/telechargement")
	public String telecharger (Model model,  DateDebutFinDTO periode, @SessionAttribute("localUrl") String localUrl)  throws Exception {
		LocalDate dateDebut = periode.getDateDebut() ;
		LocalDate dateFin = periode.getDateFin() ;

		ResponseEntity<Projet[]> response = rest.getForEntity(localUrl+"/ws/projet/telechargement/periode/{dateDebut}/{dateFin}", Projet[].class, 
					 dateDebut.format(formatter), 
					 dateFin.format(formatter)) ;
		
		List<Projet> listeProjets = Arrays.asList(response.getBody());
		
		model.addAttribute("listeProjets", listeProjets) ;
		model.addAttribute("periode", new DateDebutFinDTO(dateDebut, dateFin)) ;
		return "projet/telecharges" ;
	}
	 
	
	 	
	
	@PostMapping("/validation-masse")
	public String validerEnMasse(Model model, @SessionAttribute("listeProjets") List<Projet> listeProjets,
			@SessionAttribute("localUrl") String localUrl){
	   ResponseEntity<Integer> response = rest.exchange(localUrl+"/ws/projet/validation-masse", HttpMethod.POST, new HttpEntity<>(listeProjets), 
			   Integer.class);			
	    return "redirect:/projet/liste";
	 }
	
	 
	@GetMapping("/validation/{id}")
	public String valider(Model model, @PathVariable("id") Long id, @SessionAttribute("localUrl") String localUrl){
		Projet projet = Projet.builder()
			   .build() ;
	   ResponseEntity<Projet> response = rest.exchange(localUrl+"/ws/projet/validation", HttpMethod.PUT, new HttpEntity<>(projet), 
			   Projet.class);			
	    return "redirect:/projet/liste";
	}
	
	
	@GetMapping("/invalidation/{id}")
	public String invalider(Model model, @PathVariable("id") Long id, @SessionAttribute("localUrl") String localUrl){
	   Projet projet = Projet.builder()
			   .build() ;
	   ResponseEntity<Projet> response = rest.exchange(localUrl+"/ws/projet/invalidation", HttpMethod.PUT, new HttpEntity<>(projet), 
			   Projet.class);			
	    return "redirect:/projet/liste";
	}
	
}
