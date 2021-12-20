package cm.antic.pridesoft.remotesrv.service;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import cm.antic.pridesoft.datamodel.remote.ProjetRemote;
import cm.antic.pridesoft.remotesrv.repository.ProjetRemoteRepository;

@Service
public class ProjetRemoteService {
	
	private ProjetRemoteRepository projetRemoteRepository ;
	
	public ProjetRemoteService(ProjetRemoteRepository projetRemoteRepository) {
		this.projetRemoteRepository = projetRemoteRepository ;
	}
	
	
	@Cacheable("projectsByCodeCache")
	public Optional<ProjetRemote> rechercher (String codeProjet){
		return projetRemoteRepository.findByCodeProjet(codeProjet) ;
	}
	
	@Cacheable("projectsByDatesCache")
	public List<ProjetRemote> rechercher (LocalDate dateDebut, LocalDate dateFin){
		return projetRemoteRepository.findByDateSignatureBetween(dateDebut, dateFin) ;
	}
	
	@Cacheable("projectsByMOAndDatesCache")
	public List<ProjetRemote> rechercher (String maitreOuvrage, LocalDate dateDebut, LocalDate dateFin){
		return projetRemoteRepository.findByMaitreOuvrageAndDateSignatureBetween(maitreOuvrage, dateDebut, dateFin) ;
	}
}
