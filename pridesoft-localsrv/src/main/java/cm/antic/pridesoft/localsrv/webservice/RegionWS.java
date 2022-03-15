package cm.antic.pridesoft.localsrv.webservice;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cm.antic.pridesoft.datamodel.exceptions.LocalEntityNotFoundException;
import cm.antic.pridesoft.datamodel.local.Region;
import cm.antic.pridesoft.datamodel.remote.RegionRemote;
import cm.antic.pridesoft.localsrv.service.RegionService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/region")
public class RegionWS {
	private RegionService regionService ;
	private RestTemplate restTemplate ;
	private Environment environment ;
	
	public RegionWS(RegionService regionService, RestTemplate restTemplate, Environment environment) {
		this.regionService = regionService ;
		this.restTemplate = restTemplate ;
		this.environment = environment ;
	}
	
	
	
	@PutMapping("/creation")
	public Region creer (@RequestBody Region region) {
		log.info("Modification de region");
		return regionService.creer(region) ;
	}
	
	
	@PutMapping("/modification")
	public Region modifier (@RequestBody Region region) {
		log.info("Modification de region");
		return regionService.modifier(region) ;
	}


	@GetMapping("/id/{id}")
	public Region getRegion (@PathVariable("id") String id) {
		log.info(String.format("Recherche de la region d'id %s", id));
		return regionService.rechercher(id)
							.map(Function.identity())
							.orElseThrow(LocalEntityNotFoundException::new) ;
	}

	
	
	@DeleteMapping("/id/{id}")
	public void supprimer (@PathVariable("id") Long id) {
		log.info(String.format("Suppression de region %s", id)) ;
		Region region = Region.builder()
				.id(Long.toString(id))
				.build() ;
		regionService.supprimer(region) ;
	}
	
	
	@GetMapping("/telechargement/liste")
	public List<Region> telechargerTout () {	
		log.info("Lancement du telechargement des regions") ;
		String remoteUrl = "http://"+environment.getProperty("app.remotesrv.host")+":"+environment.getProperty("app.remotesrv.port") ;
		ResponseEntity<RegionRemote[]> response = restTemplate.getForEntity(remoteUrl+"/ws/region/all", 
				RegionRemote[].class) ;
			
		List<RegionRemote> listeRegionsRemote = Arrays.asList(response.getBody());
			
		return listeRegionsRemote.parallelStream()
				.map(regionR -> {
					   Region region = new Region();
					   region.setId(Long.toString(regionR.getId())) ;
					   region.setCode(regionR.getCode()) ;
					   region.setLibelle(regionR.getLibelle()) ;
					   return regionService.creerOuModifier(region) ;
				 }).collect(Collectors.toList()) ;
	}
	
	
	
	@GetMapping("/liste")
	public List<Region> getRegions() {
		log.info("Recherche de toutes les regions");
		return regionService.rechercherTout() ;
	}
	
}
