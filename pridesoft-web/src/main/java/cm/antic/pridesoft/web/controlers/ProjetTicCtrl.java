package cm.antic.pridesoft.web.controlers;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import cm.antic.pridesoft.datamodel.local.MaitreOuvrage;
import cm.antic.pridesoft.datamodel.local.ProjetTic;
import cm.antic.pridesoft.datamodel.local.Region;
import cm.antic.pridesoft.datamodel.local.SecteurActivite;
import cm.antic.pridesoft.datatransfer.local.DateDebutFinDTO;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Controller
@SessionAttributes({"utilisateurCourant", "listeRegions", "listeCategories", "listeSecteursActivite", 
	"listeMaitresOuvrage", "localUrl"})
@RequestMapping("/projettic")
public class ProjetTicCtrl implements Serializable{
	@Autowired
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
	private DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd") ;
	
	public void initialiser(Model model, String localUrl) {
		model.addAttribute("listeRegions", 
				Arrays.asList(rest.getForEntity(localUrl+"/ws/region/liste", Region[].class).getBody())) ;
		model.addAttribute("listeCategories", 
				Arrays.asList(rest.getForEntity(localUrl+"/ws/categorie/liste", Categorie[].class).getBody())) ;
		model.addAttribute("listeSecteursActivite",
				Arrays.asList(rest.getForEntity(localUrl+"/ws/secteuractivite/liste", SecteurActivite[].class).getBody())) ;
		model.addAttribute("listeMaitresOuvrage", 
				Arrays.asList(rest.getForEntity(localUrl+"/ws/maitreouvrage/liste", MaitreOuvrage[].class).getBody())) ;
	}
	
	
	@GetMapping ("/liste")
	public String getDefaultProjets (Model model, @SessionAttribute("localUrl") String localUrl) {
		int year = LocalDate.now().getYear() ;
		LocalDate dateDebut = LocalDate.of(year, Month.JANUARY, 1) ;
		LocalDate dateFin = LocalDate.of(year, Month.DECEMBER, 31) ;
		
		ResponseEntity<ProjetTic[]> response = rest.getForEntity(localUrl+"/ws/projettic/data/periode/{dateDebut}/{dateFin}", 
				ProjetTic[].class, dateDebut.format(formatter), dateFin.format(formatter)) ;
		List<ProjetTic> listeProjets = Arrays.asList(response.getBody());
		model.addAttribute("listeProjets", listeProjets) ;
		model.addAttribute("periode", new DateDebutFinDTO(dateDebut, dateFin)) ;
		initialiser(model, localUrl) ;
	    return "projettic/liste" ; 
	}
	
	

	@PostMapping ("/liste")
	public String getProjets (Model model, DateDebutFinDTO periode, @SessionAttribute("localUrl") String localUrl) {
		LocalDate dateDebut = periode.getDateDebut() ;
		LocalDate dateFin = periode.getDateFin() ;
		ResponseEntity<ProjetTic[]> response = rest.getForEntity(localUrl+"/ws/projettic/data/periode/{dateDebut}/{dateFin}", 
				ProjetTic[].class, dateDebut.format(formatter), dateFin.format(formatter)) ;
		List<ProjetTic> listeProjets = Arrays.asList(response.getBody());
		model.addAttribute("listeProjets", listeProjets) ;
		model.addAttribute("periode", new DateDebutFinDTO(dateDebut, dateFin)) ;
		return "projettic/liste" ; 
	}
	
	
	@GetMapping ("/modification-projettic/{codeProjet}")
	public String initialiserModification (Model model, @PathVariable("codeProjet") String codeProjet,
			@SessionAttribute("localUrl") String localUrl) {
		ProjetTic projetTic = rest.getForObject(localUrl+"/ws/projettic/data/codeProjet/{codeProjet}", 
				ProjetTic.class, codeProjet) ;
		model.addAttribute("projetTicExistant", projetTic) ;
	    return "projettic/edit" ; 
	}
	
	
	
	@PostMapping ("/modification")
	public String modifier (Model model, ProjetTic projetTicExistant, 
			@SessionAttribute("listeRegions") List<Region> listeRegions, 
			@SessionAttribute("listeCategories") List<Categorie> listeCategories, 
			@SessionAttribute("listeSecteursActivite") List<SecteurActivite> listeSecteursActivite,
			@SessionAttribute("listeMaitresOuvrage") List<MaitreOuvrage> listeMaitresOuvrage,
			@SessionAttribute("localUrl") String localUrl) {
		log.info("projetTicExistant="+projetTicExistant) ;
		String regionSelectionnee = listeRegions.stream()
				.peek(System.out::println)
				.filter(region -> region.getId().equals(projetTicExistant.getIdRegion()))
				.map(Region::getLibelle)
				.findFirst()
				.orElse(null) ;
		String categorieSelectionnee = listeCategories.stream()
				.filter(categorie -> categorie.getId().equals(projetTicExistant.getIdCategorie()))
				.map(Categorie::getLibelle)
				.findFirst()
				.orElse(null) ;
		String secteurActiviteSelectionne = listeSecteursActivite.stream()
				.filter(secteurActivite -> secteurActivite.getId().equals(projetTicExistant.getIdSecteurActivite()))
				.map(SecteurActivite::getLibelle)
				.findFirst()
				.orElse(null) ;
		String maitreOuvrageSelectionne = listeMaitresOuvrage.stream()
				.filter(maitreOuvrage -> maitreOuvrage.getId().equals(projetTicExistant.getIdMaitreOuvrage()) )
				.map(MaitreOuvrage::getLibelle)
				.findFirst()
				.orElse(null) ;
		
		log.info("regionSelectionnee="+regionSelectionnee+"categorieSelectionnee="+categorieSelectionnee+
				",secteurActiviteSelectionne="+secteurActiviteSelectionne+", maitreOuvrageSelectionne="+maitreOuvrageSelectionne) ;
		
		projetTicExistant.setLibelleRegion(regionSelectionnee == null ? null : regionSelectionnee) ;
		projetTicExistant.setLibelleCategorie(categorieSelectionnee == null ? null : categorieSelectionnee) ;
		projetTicExistant.setLibelleSecteurActivite(secteurActiviteSelectionne == null ? null : secteurActiviteSelectionne) ;
		projetTicExistant.setLibelleMaitreOuvrage(maitreOuvrageSelectionne == null ? null : maitreOuvrageSelectionne) ;
		
		ProjetTic projetTic = rest.postForObject(localUrl+"/ws/projettic/data/update", 
				 projetTicExistant, ProjetTic.class) ;
		
		model.addAttribute("projetTicExistant", projetTic) ;
	    return "projettic/edit" ; 
	}
	
}