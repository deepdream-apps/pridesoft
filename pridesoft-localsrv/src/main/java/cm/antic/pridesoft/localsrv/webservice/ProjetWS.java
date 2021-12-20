package cm.antic.pridesoft.localsrv.webservice;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cm.antic.pridesoft.datamodel.local.Projet;
import cm.antic.pridesoft.datamodel.remote.ProjetRemote;
import cm.antic.pridesoft.localsrv.service.ProjetService;
import lombok.extern.log4j.Log4j2;
@Log4j2
@RestController
@RequestMapping("/ws/projet")
public class ProjetWS {
	@Autowired
	private ProjetService projetService ;
	@Autowired
	private RestTemplate rest ;
	@Autowired
	private Environment env ;

	
	@PostMapping("/ajout")
	public Projet ajouter (@RequestBody Projet projet) {
		log.info("Ajout du projet");
		return projetService.creer(projet) ;
	}
	
	
	@PutMapping("/modification")
	public Projet modifier (@RequestBody Projet projet) {
		log.info("Modification du projet");
		return projetService.modifier(projet) ;
	}
	
	
	@DeleteMapping("/id/{id}")
	public void supprimer (@PathVariable("id") Long id) {
		log.info("Suppression du projet");
		Projet projet = Projet.builder()
				.id(id)
				.build() ;
		projetService.supprimer(projet) ;
	}
	
	
	@GetMapping("/id/{id}")
	public Projet rechercherId(@PathVariable("id") Long id) {
		log.info("Recherche du projet d'id %s", id);
		return projetService.rechercher(id) ;
	}
	
	
	@GetMapping("/codeprojet/{codeProjet}")
	public Projet rechercher(@PathVariable("codeProjet") String codeProjet) {
		log.info(String.format("Recherche du projet de code %s", codeProjet));
		return projetService.rechercher(codeProjet) ;
	}
	
	
	@GetMapping("/validation-masse/{dateDebut}/{dateFin}")
	public List<Projet> validerMasse(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin) {
		log.info("Validation des projets en masse de %s a %s ", dateDebut, dateFin);
		LocalDate debut = LocalDate.parse(dateDebut, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
		LocalDate fin = LocalDate.parse(dateFin, DateTimeFormatter.ofPattern("dd-MM-yyyy")) ;
		return projetService.validerMasse(debut, fin) ;
	}
	
	
	@PutMapping("/validation")
	public Projet valider(@RequestBody Projet projet) {
		log.info(String.format("Validation du projet de code %s", projet.getCodeProjet()));
		return projetService.valider(projet) ;
	}
	
	
	@PutMapping("/invalidation")
	public Projet invalider(@RequestBody Projet projet) {
		log.info(String.format("Invalidation du projet de code %s", projet.getCodeProjet()));
		return projetService.invalider(projet) ;
	}
	
	
	@GetMapping("/year/{year}")
	public List<Projet> rechercher(@PathVariable("year") Long year) {
		log.info(String.format("Recerche des projets de %s", year));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(year+"-01-01", formatter) ;
		LocalDate fin = LocalDate.parse(year+"-12-31", formatter) ;
		return projetService.rechercher(debut, fin) ;
	}
	
	
	@GetMapping("/{dateDebut}/{dateFin}")
	public List<Projet> rechercher(@PathVariable("dateDebut") String dateDebut, 
			@PathVariable("dateFin") String dateFin) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		return projetService.rechercher(debut, fin) ;
	}
	
	
	@GetMapping("/telechargement/{dateDebut}/{dateFin}")
	public List<Projet> telecharger (@PathVariable("dateDebut") String dateDebut, 
			@PathVariable("dateFin") String dateFin) throws IOException{
		try (Stream<String> streamMotsCle = Files.lines(Paths.get(new ClassPathResource(
	    	      "data/keywords").getFile().getAbsolutePath()))){
			log.info(String.format("Recherche de la liste des projets de %s  a %s", dateDebut, dateFin)) ;
					
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
			LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
			LocalDate fin = LocalDate.parse(dateFin, formatter) ;
			
			ResponseEntity<ProjetRemote[]> response = rest.getForEntity("http://"+env.getProperty("app.remotesrv")+"/projet/{date1}/{date2}", 
					ProjetRemote[].class, debut, fin) ;
			
			List<ProjetRemote> listeProjets = Arrays.asList(response.getBody());
			
			log.info(String.format("Nombre de projets recuperees : %s", listeProjets.size())) ;
			
			List<ProjetRemote> listeProjetsFiltres = listeProjets.stream()
					.filter(projet -> {
						return streamMotsCle.anyMatch(motCle -> projet.getDesignation().contains(motCle)) ;
					}).collect(Collectors.toList()) ;
			
			return listeProjetsFiltres.stream()
							   .map(projetR  -> {
								    Projet projet = Projet.builder()
								    		.libelle(projetR.getDesignation())
								    		.codeProjet(projetR.getCodeProjet())
								    		.dateSignature(projetR.getDateSignature())
								    		.montant(projetR.getMontant())
								    		.idRegion(projetR.getRegion() == null ? null : projetR.getRegion().getId())
								    		.libelleRegion(projetR.getRegion() == null ? null : projetR.getRegion().getLibelle())
								    		.idMaitreOuvrage(projetR.getMaitreOuvrage() == null ? null :  projetR.getMaitreOuvrage().getId())
								    		.sigleMaitreOuvrage(projetR.getMaitreOuvrage() == null ? null : projetR.getMaitreOuvrage().getDesignation())
								    		.build() ;
								    return projetService.creer(projet) ;
							   }).collect(Collectors.toList()) ;
			
		}catch(IOException ex) {
			log.error(ex.getMessage(), ex) ;
			throw ex ;
		}
	}
	

}
