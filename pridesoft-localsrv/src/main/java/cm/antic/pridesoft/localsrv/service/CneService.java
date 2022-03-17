package cm.antic.pridesoft.localsrv.service;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import cm.antic.pridesoft.datamodel.exceptions.LocalEntityNotFoundException;
import cm.antic.pridesoft.datamodel.local.Cne;
import cm.antic.pridesoft.datamodel.local.ProjetTic;
import cm.antic.pridesoft.localsrv.repository.CneRepository;
import cm.antic.pridesoft.localsrv.repository.ProjetTicRepository;

@Transactional
@Service
public class CneService {
	private CneRepository cneRepository ;
	private ProjetTicRepository projetTicRepository ;
	
	
	public CneService(CneRepository cneRepository, ProjetTicRepository projetTicRepository) {
		this.cneRepository = cneRepository;
		this.projetTicRepository = projetTicRepository;
	}


	public Cne creer (Cne cne) {
		return cneRepository.save(cne) ;
	}
	
	
	public Cne creerEtValider (Cne cne) {
		Cne cneCree = cneRepository.save(cne) ;
		ProjetTic projetTic = ProjetTic.builder()
				.codeProjet(cne.getCodeProjet())
			    .libelle(cne.getLibelle())
			    .dateSignature(cne.getDateSignature())
			    .montant(cne.getMontant())
			    .idRegion(cne.getIdRegion())
			    .libelleRegion(cne.getLibelleRegion())
			    .idMaitreOuvrage(cne.getIdMaitreOuvrage())
			    .libelleMaitreOuvrage(cne.getLibelleMaitreOuvrage())
			    .idCategorie(cne.getIdCategorie())
			    .libelleCategorie(cne.getLibelleCategorie())
			    .idSecteurActivite(cne.getIdSecteurActivite())
			    .libelleSecteurActivite(cne.getLibelleSecteurActivite())
			    .build() ;
		projetTicRepository.save(projetTic) ;
		
		return cneCree ;
	}
	
	
	public Cne modifier (Cne cne) {
		return cneRepository.save(cne) ;
	}
	
	
	public Optional<Cne> rechercher (String id) {
		return cneRepository.findById(id) ;
	}
	
	
	public Cne valider (Cne cne)  {
		Optional<Cne> cneExistantOpt = cneRepository.findByCodeProjet(cne.getCodeProjet()) ;
		return cneExistantOpt.map(this::handleValidation)
				             .orElseThrow(LocalEntityNotFoundException::new) ;
	}
	
	
	private Cne handleValidation(Cne cne) {
		ProjetTic projetTic = new ProjetTic() ;
		projetTic.setCodeProjet(cne.getCodeProjet());
		projetTic.setLibelle(cne.getLibelle());
		projetTicRepository.save(projetTic) ;
		
		cne.setValide(1);
		return cneRepository.save(cne) ;
	}
	
	
	public Cne invalider (Cne cne) {
		Optional<Cne> cneExistantOpt = cneRepository.findByCodeProjet(cne.getCodeProjet()) ;
		return cneExistantOpt.map(this::handleUnValidation)
	             .orElseThrow(LocalEntityNotFoundException::new) ;
	}
	
	
	private Cne handleUnValidation(Cne cne) {
		cne.setValide(0);
		return cneRepository.save(cne) ;
	}
		
	
	
	public List<Cne> validerEnMasse (List<Cne> listeCNEs) {
		return listeCNEs.stream()
						.map(this::handleValidation)
						.collect(Collectors.toList()) ;
	}
	

	
	public List<Cne> rechercherTout (){
		Iterable<Cne> cnes = cneRepository.findAll() ;
		List<Cne> listeCnes = new ArrayList<Cne>() ;
		cnes.forEach(listeCnes::add);
		return listeCnes ;
	}
	
	
	public List<Cne> rechercher (LocalDate date1, LocalDate date2){
		return cneRepository.findByDateSignatureBetween(date1, date2) ;
	}
	 

}
