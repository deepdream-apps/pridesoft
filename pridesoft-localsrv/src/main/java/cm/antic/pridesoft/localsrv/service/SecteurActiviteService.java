package cm.antic.pridesoft.localsrv.service;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.antic.pridesoft.datamodel.local.SecteurActivite;
import cm.antic.pridesoft.datamodel.remote.SecteurActiviteRemote;
import cm.antic.pridesoft.localsrv.repository.SecteurActiviteRepository;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Transactional
@Service
public class SecteurActiviteService {
	private SecteurActiviteRepository SecteurActiviteRepository ;
	
	
	public SecteurActiviteService(SecteurActiviteRepository SecteurActiviteRepository) {
		this.SecteurActiviteRepository = SecteurActiviteRepository;
	}
	
	
	public Optional<SecteurActivite> rechercher (Long id) {
		return SecteurActiviteRepository.findById(id) ;
	}
	
	
	public List<SecteurActivite> rechercherTout (){
		Iterable<SecteurActivite> SecteurActivites = SecteurActiviteRepository.findAll() ;
		List<SecteurActivite> listeSecteurActivites = new ArrayList<SecteurActivite>() ;
		SecteurActivites.forEach(listeSecteurActivites::add);
		return listeSecteurActivites ;
	}
	
	
	
	public List<SecteurActivite>  charger(List<SecteurActiviteRemote> listeSecteurActivitesR) {
		return listeSecteurActivitesR.stream()
				.peek(SecteurActivite -> log.info(String.format("Chargement de %s - %s", SecteurActivite.getSigle(), SecteurActivite.getLibelle())))
				.map(SecteurActiviteR -> {
					SecteurActivite SecteurActivite = new SecteurActivite();
					SecteurActivite.setId(SecteurActiviteR.getId());
					SecteurActivite.setLibelle(SecteurActiviteR.getLibelle());
					SecteurActivite.setSigle(SecteurActiviteR.getSigle());
					return SecteurActiviteRepository.save(SecteurActivite);
			  }).collect(Collectors.toList()) ;
	}
	
	
	
	public SecteurActivite modifier (SecteurActivite SecteurActivite) throws Exception {
		return SecteurActiviteRepository.save(SecteurActivite) ;
	}
	
	
	public void supprimer (SecteurActivite SecteurActivite) throws Exception {
		SecteurActiviteRepository.delete(SecteurActivite) ;
	}
}
