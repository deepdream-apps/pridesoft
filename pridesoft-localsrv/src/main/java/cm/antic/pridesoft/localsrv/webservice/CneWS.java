package cm.antic.pridesoft.localsrv.webservice;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import cm.antic.pridesoft.datamodel.exceptions.LocalEntityNotFoundException;
import cm.antic.pridesoft.datamodel.local.Categorie;
import cm.antic.pridesoft.datamodel.local.Cne;
import cm.antic.pridesoft.datamodel.local.MaitreOuvrage;
import cm.antic.pridesoft.datamodel.local.Region;
import cm.antic.pridesoft.datamodel.local.SecteurActivite;
import cm.antic.pridesoft.datamodel.remote.CneRemote;
import cm.antic.pridesoft.localsrv.service.CneService;
import cm.antic.pridesoft.localsrv.service.MaitreOuvrageService;
import cm.antic.pridesoft.localsrv.service.RegionService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/cne")
@SessionAttributes({"utilisateurCourant", "listeCNEs"})
public class CneWS {
	private CneService cneService ;
	private MaitreOuvrageService maitreOuvrageService ;
	private RegionService regionService ;
	private RestTemplate restTemplate ;
	private Environment env ;
	
	public CneWS(CneService cneService, MaitreOuvrageService maitreOuvrageService, 
			RegionService regionService, RestTemplate restTemplate, Environment env) {
		this.cneService = cneService;
		this.maitreOuvrageService = maitreOuvrageService ;
		this.regionService = regionService ;
		this.restTemplate = restTemplate ;
		this.env = env ;
	}
	
	
	@GetMapping("/id/{id}")
	public Cne getCne (@PathVariable("id") String id) {
		log.info(String.format("Recherche du cne d'id %s", id));
		return cneService.rechercher(id)
					     .map(Function.identity())
					     .orElseThrow(LocalEntityNotFoundException::new);
	}
	
	
	@GetMapping("/all")
	public List<Cne> getCnes() {
		log.info("Recherche de toutes les CNE ") ;
		return cneService.rechercherTout() ;
	}
	
	
	@GetMapping("/periode/{dateDebut}/{dateFin}")
	public List<Cne> rechercherPlusieurs(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		return cneService.rechercher(debut, fin) ;
	}
	
	
	@PostMapping("/validation-masse")
	public Integer validerMasse(@RequestBody List<Cne> listeCNEs) {
		log.info(String.format("Validation des %s CNE ", listeCNEs.size()));
		return cneService.validerEnMasse(listeCNEs).size() ;
	}
	
	
	@PutMapping("/validation")
	public Cne valider(@RequestBody Cne cne) {
		return cneService.valider(cne) ;
	}
	
	
	@PutMapping("/invalidation")
	public Cne invalider(@RequestBody Cne cne) {
		return cneService.invalider(cne) ;
	}
	
	
	@GetMapping("/telechargement/periode/{dateDebut}/{dateFin}")
	public List<Cne> telecharger (@PathVariable("dateDebut") String dateDebut, 
			@PathVariable("dateFin") String dateFin) throws IOException{
		try (Stream<String> streamMotsCle = Files.lines(Paths.get(env.getProperty("app.localsrv.path.data.keyworks")))){
			log.info(String.format("Recherche de la liste des projets de %s  a %s", dateDebut, dateFin)) ;
					
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
			
			final List<Region> listeRegions = regionService.rechercherTout() ;
			final List<MaitreOuvrage> listeMaitresOuvrage = maitreOuvrageService.rechercherTout() ;
			
			List<String> listeMotsCles = streamMotsCle.collect(Collectors.toList()) ;
			
			String remoteUrl = "http://"+env.getProperty("app.remotesrv.host")+":"+env.getProperty("app.remotesrv.port") ;
			
			ResponseEntity<CneRemote[]> response = restTemplate.getForEntity(remoteUrl+"/ws/cne/periode/{date1}/{date2}", CneRemote[].class, dateDebut, dateFin) ;
			
			List<CneRemote> listeCNEs = Arrays.asList(response.getBody());
			
			log.info(String.format("Nombre de CNE recuperees : %s", listeCNEs.size())) ;
			
			List<CneRemote> listeCNEsFiltres = listeCNEs.stream()
					.filter(cneR -> {
						return listeMotsCles.stream().anyMatch(motCle ->cneR.getObjetCommande().toLowerCase().contains(motCle)) 
							   && cneR.getMontantTtcCommande() != null && cneR.getMontantTtcCommande().longValue() > 0L  
							   && cneR.getMontantTtcCommande().longValue() < 5000000L;
					}).collect(Collectors.toList()) ;
			
			return listeCNEsFiltres.stream()
							       .map(cneR  -> {
							    	   String regionSelectionnee = listeRegions.stream()
												.peek(System.out::println)
												.filter(region -> region.getId().equals(String.valueOf(cneR.getRegion())))
												.map(Region::getLibelle)
												.findFirst()
												.orElse(null) ;

										String maitreOuvrageSelectionne = listeMaitresOuvrage.stream()
												.filter(maitreOuvrage -> maitreOuvrage.getId().equals(String.valueOf(cneR.getMaitreOuvrage())))
												.map(MaitreOuvrage::getLibelle)
												.findFirst()
												.orElse(null) ;
								    Cne cne = Cne.builder()
								    		.codeProjet(cneR.getNumero())
								    		.libelle(cneR.getObjetCommande())
								    		.dateSignature(cneR.getDateSignature())
								    		.montant(cneR.getMontantTtcCommande())
								    		.idRegion(cneR.getRegion() == null ? null : Long.toString(cneR.getRegion()))
								    		.libelleRegion(regionSelectionnee)
								    		.idMaitreOuvrage(Long.toString(cneR.getMaitreOuvrage()))
								    		.libelleMaitreOuvrage(maitreOuvrageSelectionne)
								    		.build() ;
								    return cneService.creerEtValider(cne) ;
							   }).collect(Collectors.toList()) ;
			
		}catch(IOException ex) {
			log.error(ex.getMessage(), ex) ;
			throw ex ;
		}
	}

}
