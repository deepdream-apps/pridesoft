package cm.antic.pridesoft.localsrv.service;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.antic.pridesoft.datamodel.local.Region;
import cm.antic.pridesoft.datamodel.remote.RegionRemote;
import cm.antic.pridesoft.localsrv.repository.RegionRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional
@Service
public class RegionService {
	private RegionRepository regionRepository ;
	
	
	public RegionService(RegionRepository regionRepository) {
		this.regionRepository = regionRepository;
	}
	
	
	public Region creer (Region region) {
		return regionRepository.save(region) ;
	}
	

	public Region modifier (Region region) {
		return regionRepository.save(region) ;
	}
	
	
	public void supprimer (Region region) {
		regionRepository.delete(region) ;
	}
	
	
	public Optional<Region> rechercher (Long id) {
		return regionRepository.findById(id) ;
	}
	
	
	public List<Region> rechercherTout (){
		Iterable<Region> regions = regionRepository.findAll() ;
		List<Region> listeRegions = new ArrayList<Region>() ;
		regions.forEach(listeRegions::add);
		return listeRegions ;
	}
	
	
	
	public List<Region>  charger(List<RegionRemote> listeRegionsR) {
		return listeRegionsR.stream()
				.peek(region -> log.info(String.format("Chargement de %s - %s", region.getCode(), region.getLibelle())))
				.map(regionR -> {
					Region region = new Region();
					region.setId(regionR.getId());
					region.setLibelle(regionR.getLibelle());
					region.setCode(regionR.getCode());
					return regionRepository.save(region);
			  }).collect(Collectors.toList()) ;
	}
	
	
	
}
