package cm.antic.pridesoft.localsrv.service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	
	public Cne modifier (Cne cne) {
		return cneRepository.save(cne) ;
	}
	
	
	public Optional<Cne> rechercher (Long id) {
		return cneRepository.findById(id) ;
	}
	
	
	public Cne valider (Cne cne)  {
		Optional<Cne> cneExistantOpt = cneRepository.findById(cne.getId()) ;
		return cneExistantOpt.map(this::handleValidation)
				             .orElseThrow(LocalEntityNotFoundException::new) ;
	}
	
	
	private Cne handleValidation(Cne cne) {
		ProjetTic projetTic = new ProjetTic() ;
		projetTic.setCodeProjet(cne.getNumero());
		projetTic.setLibelle(cne.getLibelle());
		projetTicRepository.save(projetTic) ;
		
		cne.setValide(1);
		return cneRepository.save(cne) ;
	}
	
	
	public Cne invalider (Cne cne) {
		Optional<Cne> cneExistantOpt = cneRepository.findById(cne.getId()) ;
		return cneExistantOpt.map(this::handleUnValidation)
	             .orElseThrow(LocalEntityNotFoundException::new) ;
	}
	
	
	private Cne handleUnValidation(Cne cne) {
		cne.setValide(0);
		return cneRepository.save(cne) ;
	}
		
	
	
	public List<Cne> validerEnMasse (LocalDate debut, LocalDate fin) {
		List<Cne> listeCNEs = cneRepository.findByValideAndDateSignatureBetween(0, debut, fin) ;
		
		final List<String> listMotsCles = Arrays.asList("informatique", "moteur de recherche", "logiciel", 
				"numérique", "digitalisation", " TIC ", "ordinateur", "laptop", "imprimante", 
				"baie de brassage", "salle serveur" ) ;

		return listeCNEs.stream().filter(p -> {
			return listMotsCles.stream().anyMatch(motCle  ->  p.getLibelle().contains(motCle)) ;
		 }).collect(Collectors.toList()) ;
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
