package cm.antic.pridesoft.remotesrv.service;
import java.io.Serializable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cm.antic.pridesoft.datamodel.remote.FicheSuiviRemote;
import cm.antic.pridesoft.remotesrv.repository.FicheSuiviRemoteRepository;

@Service
public class FicheSuiviRemoteService implements Serializable{
	private FicheSuiviRemoteRepository ficheSuiviRemoteRepository ;
	
	public FicheSuiviRemoteService(FicheSuiviRemoteRepository ficheSuiviRemoteRepository) {
		this.ficheSuiviRemoteRepository = ficheSuiviRemoteRepository ;
	}
	
	
	public Optional<FicheSuiviRemote> rechercher(String codeProjet) {
		return ficheSuiviRemoteRepository.findByCodeProjet(codeProjet) ;
	}
	
	
	public List<FicheSuiviRemote> rechercher (String maitreOuvrage, LocalDate date1, LocalDate date2) {
		return ficheSuiviRemoteRepository.findByMaitreOuvrageAndDateSignatureBetween(maitreOuvrage, date1, date2) ;
	}
	
	
	public List<FicheSuiviRemote> rechercher (LocalDate date1, LocalDate date2) {
		return ficheSuiviRemoteRepository.findByDateSignatureBetween(date1, date2) ;
	}
	
}
