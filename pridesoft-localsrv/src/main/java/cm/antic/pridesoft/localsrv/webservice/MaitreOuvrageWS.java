package cm.antic.pridesoft.localsrv.webservice;

import java.io.IOException;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.client.RestTemplate;

import cm.antic.pridesoft.datamodel.exceptions.LocalEntityNotFoundException;
import cm.antic.pridesoft.datamodel.local.MaitreOuvrage;
import cm.antic.pridesoft.datamodel.remote.MaitreOuvrageRemote;
import cm.antic.pridesoft.localsrv.service.MaitreOuvrageService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/maitreouvrage")
public class MaitreOuvrageWS {
	private MaitreOuvrageService maitreOuvrageService ;
	private RestTemplate restTemplate ;
	private Environment environment ;
	
	
	public MaitreOuvrageWS(MaitreOuvrageService maitreOuvrageService, RestTemplate restTemplate, Environment environment) {
		this.maitreOuvrageService = maitreOuvrageService;
		this.restTemplate = restTemplate ;
		this.environment = environment ;
	}


	@PostMapping("/creation")
	public MaitreOuvrage creer (@RequestBody MaitreOuvrage maitreOuvrage) {
		return  maitreOuvrageService.creer(maitreOuvrage) ;
	}
	
	
	@PutMapping("/modification")
	public MaitreOuvrage modifier (@RequestBody MaitreOuvrage maitreOuvrage) {
		return  maitreOuvrageService.modifier(maitreOuvrage) ;
	}
	
	
	@GetMapping("/id/{id}")
	public MaitreOuvrage getMaitreOuvrage (@PathVariable("id") String id) {
		log.info(String.format("Recherche du maître d'ouvrage d'id %s ", id));
		return maitreOuvrageService.rechercher(id) 
				                   .map(Function.identity())
				                   .orElseThrow(LocalEntityNotFoundException::new);
	}
	
	
	@GetMapping("/liste")
	public List<MaitreOuvrage> getMaitreOuvrages() {
		log.info("Recherche de tous les maîtres d'ouvrage") ;
		return maitreOuvrageService.rechercherTout() ;
	}
	
	
	@DeleteMapping("/id/{id}")
	public void supprimer (@PathVariable("id") String id) {
		log.info(String.format("Suppression du maître d'ouvre d'id %s ", id)) ;
		MaitreOuvrage maitreOuvrage = MaitreOuvrage.builder()
				.id(id)
				.build() ;
		maitreOuvrageService.supprimer(maitreOuvrage) ;
	}
	
	
	@GetMapping("/telechargement/liste")
	public List<MaitreOuvrage> telechargerTout () throws IOException{	
		String remoteUrl = "http://"+environment.getProperty("app.remotesrv.host")+":"+environment.getProperty("app.remotesrv.port") ;
		ResponseEntity<MaitreOuvrageRemote[]> response = restTemplate.getForEntity(remoteUrl+"/ws/maitreouvrage/all", 
					MaitreOuvrageRemote[].class) ;
			
		List<MaitreOuvrageRemote> listeMaitresOuvrageRemote = Arrays.asList(response.getBody());
			
		return listeMaitresOuvrageRemote.parallelStream()
				.map(maitreOuvrageR -> {
					   MaitreOuvrage maitreOuvrage= new MaitreOuvrage();
					   maitreOuvrage.setId(Long.toString(maitreOuvrageR.getId()));
					   maitreOuvrage.setSigle(maitreOuvrageR.getSigle()) ;
					   maitreOuvrage.setLibelle(maitreOuvrageR.getLibelle()) ;
					   maitreOuvrage.setAdresse(maitreOuvrageR.getAdresse()) ;
					   maitreOuvrage.setBoitePostale(maitreOuvrageR.getBoitePostale()) ;
					   maitreOuvrage.setEmail(maitreOuvrageR.getEmail()) ;
					   maitreOuvrage.setTelephone(maitreOuvrageR.getTelephone()) ;
					   maitreOuvrage.setSiteWeb(maitreOuvrageR.getSiteWeb()) ;
					   return maitreOuvrageService.creerOuModifier(maitreOuvrage) ;
				 }).collect(Collectors.toList()) ;
	}
	

	
	
	@ExceptionHandler(LocalEntityNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public void handeEntityNotFoundException() {
		
	}
		
}
