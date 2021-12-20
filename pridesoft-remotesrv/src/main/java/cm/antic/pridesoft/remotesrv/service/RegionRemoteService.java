package cm.antic.pridesoft.remotesrv.service;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cm.antic.pridesoft.datamodel.remote.PaysRemote;
import cm.antic.pridesoft.datamodel.remote.RegionRemote;
import cm.antic.pridesoft.remotesrv.repository.RegionRemoteRepository;

@Service
public class RegionRemoteService {
	private RegionRemoteRepository regionRemoteRepository ;
	
	public RegionRemoteService(RegionRemoteRepository regionRemoteRepository) {
		this.regionRemoteRepository = regionRemoteRepository ;
	}
	
	public Optional<RegionRemote> rechercher (Long id) {
		return  regionRemoteRepository.findById(id) ;
	}
	
	public Optional<RegionRemote> rechercher (String code) {
		return  regionRemoteRepository.findByCode(code) ;
	}
	
	public List<RegionRemote> rechercher (PaysRemote pays){
		Iterable<RegionRemote> regions = regionRemoteRepository.findByPays(pays) ;
		List<RegionRemote> listeRegions = new ArrayList<RegionRemote>() ;
		regions.forEach(listeRegions::add);
		return listeRegions ;
	}

}
