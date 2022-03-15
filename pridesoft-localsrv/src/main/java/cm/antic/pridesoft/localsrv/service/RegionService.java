package cm.antic.pridesoft.localsrv.service;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
		region.setId(UUID.randomUUID().toString());
		return regionRepository.save(region) ;
	}
	

	public Region modifier (Region region) {
		return regionRepository.save(region) ;
	}
	
	
	public Region creerOuModifier (Region region) {
		return regionRepository.save(region) ;
	}
	
	
	public void supprimer (Region region) {
		regionRepository.delete(region) ;
	}
	
	
	public Optional<Region> rechercher (String id) {
		return regionRepository.findById(id) ;
	}
	
	
	public List<Region> rechercherTout (){
		Iterable<Region> regions = regionRepository.findAll() ;
		List<Region> listeRegions = new ArrayList<Region>() ;
		regions.forEach(listeRegions::add);
		return listeRegions ;
	}
	
}
