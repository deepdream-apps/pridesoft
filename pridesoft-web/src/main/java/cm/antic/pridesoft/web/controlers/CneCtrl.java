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

import cm.antic.pridesoft.datamodel.local.Cne;
import cm.antic.pridesoft.datatransfer.local.DateDebutFinDTO;

@Controller
@SessionAttributes({"utilisateurCourant", "listeCNEs", "localUrl"})
@RequestMapping("/cne")
public class CneCtrl {
	@Autowired
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
	private DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd") ;
	
	@GetMapping ("/liste")
	public String getDefaultCNEs (Model model, @SessionAttribute("localUrl") String localUrl) {
		int year = LocalDate.now().getYear() ;
		LocalDate dateDebut = LocalDate.of(year, Month.JANUARY, 1) ;
		LocalDate dateFin = LocalDate.of(year, Month.DECEMBER, 31) ;
		
		ResponseEntity<Cne[]> response = rest.getForEntity(localUrl+"/ws/cne/periode/{dateDebut}/{dateFin}", 
				Cne[].class, dateDebut.format(formatter), dateFin.format(formatter)) ;
		List<Cne> listeCNEs = Arrays.asList(response.getBody());
		model.addAttribute("listeCNEs", listeCNEs) ;
		model.addAttribute("periode", new DateDebutFinDTO(dateDebut, dateFin)) ;
	    return "cne/liste" ; 
	}
	
	

	@PostMapping ("/liste")
	public String getCNEs (Model model, DateDebutFinDTO periode, @SessionAttribute("localUrl") String localUrl) {
		LocalDate dateDebut = periode.getDateDebut() ;
		LocalDate dateFin = periode.getDateFin() ;
		ResponseEntity<Cne[]> response = rest.getForEntity(localUrl+"/ws/cne/periode/{dateDebut}/{dateFin}", 
				Cne[].class, dateDebut.format(formatter), dateFin.format(formatter)) ;
		List<Cne> listeCNEs = Arrays.asList(response.getBody());
		model.addAttribute("listeCNEs", listeCNEs) ;
		model.addAttribute("periode", new DateDebutFinDTO(dateDebut, dateFin)) ;
		return "cne/liste" ; 
	}
	
	
	
	@GetMapping ("/telechargement")
	public String telecharger (Model model, @SessionAttribute("localUrl") String localUrl)  throws Exception {
		int year = LocalDate.now().getYear() ;
		LocalDate dateDebut = LocalDate.of(year, Month.JANUARY, 1) ;
		LocalDate dateFin = LocalDate.of(year, Month.DECEMBER, 31) ;
		ResponseEntity<Cne[]> response = rest.getForEntity(localUrl+"/ws/cne/telechargement/periode/{dateDebut}/{dateFin}", Cne[].class, 
				 dateDebut.format(formatter), dateFin.format(formatter)) ;
	
		List<Cne> listeCNEs = Arrays.asList(response.getBody());
	
		model.addAttribute("listeCNEs", listeCNEs) ;
		model.addAttribute("periode", new DateDebutFinDTO(dateDebut, dateFin)) ;
		return "cne/telecharges" ;
	}
	
	
	
	@PostMapping ("/telechargement")
	public String telecharger (Model model,  DateDebutFinDTO periode, @SessionAttribute("localUrl") String localUrl)  throws Exception {
		LocalDate dateDebut = periode.getDateDebut() ;
		LocalDate dateFin = periode.getDateFin() ;

		ResponseEntity<Cne[]> response = rest.getForEntity(localUrl+"/ws/cne/telechargement/periode/{dateDebut}/{dateFin}", Cne[].class, 
					 dateDebut.format(formatter), 
					 dateFin.format(formatter)) ;
		
		List<Cne> listeCNEs = Arrays.asList(response.getBody());
		
		model.addAttribute("listeCNEs", listeCNEs) ;
		model.addAttribute("periode", new DateDebutFinDTO(dateDebut, dateFin)) ;
		return "cne/telecharges" ;
	}
	 
	 	
	
	@PostMapping("/validation-masse")
	public String validerEnMasse(Model model, @SessionAttribute("listeCNEs") List<Cne> listeCNEs, 
			@SessionAttribute("localUrl") String localUrl){
	   ResponseEntity<Integer> response =	rest.exchange(localUrl+"/ws/cne/validation-masse", HttpMethod.POST, new HttpEntity<>(listeCNEs), 
			   Integer.class);			
	    return "redirect:/cne/liste";
	}
	
	

	@GetMapping("/validation/{id}")
	public String valider(Model model, @PathVariable("id") Long id, @SessionAttribute("localUrl") String localUrl){
	   Cne cne = Cne.builder()
			  // .code(id)
			   .build() ;
	   ResponseEntity<Cne> response =	rest.exchange(localUrl+"/ws/cne/validation", HttpMethod.PUT, new HttpEntity<>(cne), 
			   Cne.class);			
	    return "redirect:/cne/liste";
	}
	
	
	@GetMapping("/invalidation/{id}")
	public String invalider(Model model, @PathVariable("id") Long id, @SessionAttribute("localUrl") String localUrl){
	   Cne cne = Cne.builder()
			   //.id(id)
			   .build() ;
	   ResponseEntity<Cne> response =	rest.exchange(localUrl+"/ws/cne/invalidation", HttpMethod.PUT, new HttpEntity<>(cne), 
			   Cne.class);			
	    return "redirect:/cne/liste";
	}
	
	 	
}
