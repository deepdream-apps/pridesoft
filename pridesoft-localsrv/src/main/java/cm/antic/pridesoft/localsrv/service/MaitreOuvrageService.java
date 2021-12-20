package cm.antic.pridesoft.localsrv.service;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.antic.pridesoft.datamodel.local.MaitreOuvrage;
import cm.antic.pridesoft.datamodel.remote.MaitreOuvrageRemote;
import cm.antic.pridesoft.localsrv.repository.MaitreOuvrageRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional
@Service
public class MaitreOuvrageService {
	private MaitreOuvrageRepository maitreOuvrageRepository ;
	
	
	public MaitreOuvrageService(MaitreOuvrageRepository maitreOuvrageRepository) {
		this.maitreOuvrageRepository = maitreOuvrageRepository;
	}
	
	
	public MaitreOuvrage creer (MaitreOuvrage maitreOuvrage) {
		return maitreOuvrageRepository.save(maitreOuvrage) ;
	}
	
	
	public MaitreOuvrage modifier (MaitreOuvrage maitreOuvrage) {
		return maitreOuvrageRepository.save(maitreOuvrage) ;
	}
	

	public Optional<MaitreOuvrage> rechercher (Long id) {
		return maitreOuvrageRepository.findById(id)  ;
	}
	
	public List<MaitreOuvrage> rechercherTout (){
		Iterable<MaitreOuvrage> maitreOuvrages = maitreOuvrageRepository.findAll() ;
		List<MaitreOuvrage> listeMaitreOuvrages = new ArrayList<MaitreOuvrage>() ;
		maitreOuvrages.forEach(listeMaitreOuvrages::add);
		return listeMaitreOuvrages ;
	}
	
	
	public Optional<MaitreOuvrage> rechercherSigle (String sigle) {
		return maitreOuvrageRepository.findBySigle(sigle) ;
	}
	
	
	public List<MaitreOuvrage> charger(List<MaitreOuvrageRemote> listeMaitresOuvrageR) {
		return listeMaitresOuvrageR.stream()
								   .peek(maitreOuvrageR -> log.info(String.format("Chargement de %s - %s", maitreOuvrageR.getSigle(), maitreOuvrageR.getDesignation())))
								   .map(maitreOuvrageR ->{
									   MaitreOuvrage maitreOuvrage= new MaitreOuvrage();
									   maitreOuvrage.setId(maitreOuvrageR.getId());
									   maitreOuvrage.setSigle(maitreOuvrageR.getSigle()) ;
									   maitreOuvrage.setLibelle(maitreOuvrageR.getDesignation()) ;
									   return maitreOuvrageRepository.save(maitreOuvrage);
								 }).collect(Collectors.toList()) ;
		
	}
	
	
	
	public void supprimer (MaitreOuvrage maitreOuvrage)  {
		maitreOuvrageRepository.delete(maitreOuvrage) ;
	}
	
}
