package cm.antic.pridesoft.localsrv.service;
import java.time.LocalDate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.antic.pridesoft.datamodel.exceptions.LocalEntityNotFoundException;
import cm.antic.pridesoft.datamodel.local.Projet;
import cm.antic.pridesoft.localsrv.repository.ProjetRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional
@Service
public class ProjetService {
	private ProjetRepository projetRepository ;
	
	
	public Projet creer (Projet projet)  {
		return projetRepository.save(projet) ;
	}
	
	
	public Projet modifier (Projet projet) {
		return projetRepository.save(projet) ;
	}
	
	
	public Projet valider (Projet projet) {
		Optional<Projet> projectExistantOpt = projetRepository.findById(projet.getId()) ;
		
		return projectExistantOpt.map(this::handleValidation)
						  		 .orElseThrow(LocalEntityNotFoundException::new) ;	
	}
	
	
	private Projet handleValidation (Projet projet) {
		projet.setValide(1);
		return projetRepository.save(projet) ;
	}
	
	
	public Projet invalider (Projet projet) {
		Optional<Projet> projectExistantOpt = projetRepository.findById(projet.getId()) ;
		
		return projectExistantOpt.map(this::handleInvalidation)
						  		 .orElseThrow(LocalEntityNotFoundException::new) ;	
	}
	
	
	private Projet handleInvalidation (Projet projet) {
		projet.setValide(0);
		return projetRepository.save(projet) ;
	}
	
	
	public void supprimer (Projet projet) {
		projetRepository.delete(projet) ;
	}
	
	
	
	public Projet rechercher (Long id) {
		return  projetRepository.findById(id)
								.map(Function.identity())
								.orElseThrow(LocalEntityNotFoundException::new) ;
	}
	
	
	
	public List<Projet> validerMasse (LocalDate debut, LocalDate fin)  {
		List<Projet> listeProjets = projetRepository.findByValideAndDateSignatureBetween(0, debut, fin) ;
		
		final List<String> listMotsCles = Arrays.asList("informatique", "moteur de recherche", "logiciel", 
				"numérique", "digitalisation", " TIC ", "ordinateur", "laptop", "imprimante", 
				"baie de brassage", "salle serveur" ) ;

		return listeProjets.stream().filter(p -> {
			return listMotsCles.stream().anyMatch(motCle  ->  p.getLibelle().contains(motCle)) ;
		 				}).peek(projet -> log.info(String.format("Projet valide %s - %s", projet.getCodeProjet(), projet.getLibelle())))
						  .collect(Collectors.toList()) ;
	}
	
	
	public Projet rechercher (String codeProjet) {
		return projetRepository.findByCodeProjet(codeProjet)
							   .map(Function.identity())
							   .orElseThrow(LocalEntityNotFoundException::new);
	}
	
	
	public List<Projet> rechercher (LocalDate date1, LocalDate date2) {
		return projetRepository.findByDateSignatureBetween(date1, date2) ;
	}
	
}
