package cm.antic.pridesoft.remotesrv.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import cm.antic.pridesoft.datamodel.remote.SecteurActiviteRemote;
import cm.antic.pridesoft.remotesrv.repository.SecteurActiviteRemoteRepository;

@Service
public class SecteurActiviteRemoteService {
	private SecteurActiviteRemoteRepository secteurActiviteRemoteRepository ;
	
	public SecteurActiviteRemoteService(SecteurActiviteRemoteRepository secteurActiviteRemoteRepository) {
		this.secteurActiviteRemoteRepository = secteurActiviteRemoteRepository ;
	}
	
	public Optional<SecteurActiviteRemote> rechercher (Long id) {
		return secteurActiviteRemoteRepository.findById(id)  ;
	}
	
	
	public Optional<SecteurActiviteRemote> rechercher (String sigle) {
		return secteurActiviteRemoteRepository.findBySigle(sigle)  ;
	}
	
	public List<SecteurActiviteRemote> rechercherTout (){
		Iterable<SecteurActiviteRemote> secteursActivites = secteurActiviteRemoteRepository.findAll() ;
		List<SecteurActiviteRemote> listeSecteursActivites = new ArrayList<SecteurActiviteRemote>() ;
		secteursActivites.forEach(listeSecteursActivites::add);
		return listeSecteursActivites ;
	}
	
}
