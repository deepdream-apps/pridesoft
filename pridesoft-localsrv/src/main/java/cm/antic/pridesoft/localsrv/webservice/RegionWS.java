package cm.antic.pridesoft.localsrv.webservice;
import java.util.List;
import java.util.function.Function;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.antic.pridesoft.datamodel.exceptions.LocalEntityNotFoundException;
import cm.antic.pridesoft.datamodel.local.Region;
import cm.antic.pridesoft.localsrv.service.RegionService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/region")
public class RegionWS {
	private RegionService regionService ;
	
	
	public RegionWS(RegionService regionService) {
		this.regionService = regionService;
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
	public Region getRegion (@PathVariable("id") Long id) {
		log.info(String.format("Recherche de la region d'id %s", id));
		return regionService.rechercher(id)
							.map(Function.identity())
							.orElseThrow(LocalEntityNotFoundException::new) ;
	}

	
	
	@DeleteMapping("/id/{id}")
	public void supprimer (@PathVariable("id") Long id) {
		log.info(String.format("Suppression de region %s", id)) ;
		Region region = Region.builder()
				.id(id)
				.build() ;
		regionService.supprimer(region) ;
	}
	
	
	@GetMapping("/all")
	public List<Region> getRegions() {
		log.info("Recherche de toutes les regions");
		return regionService.rechercherTout() ;
	}
	
}
