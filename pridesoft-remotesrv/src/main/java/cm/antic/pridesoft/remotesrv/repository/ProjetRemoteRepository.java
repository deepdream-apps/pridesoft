package cm.antic.pridesoft.remotesrv.repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.antic.pridesoft.datamodel.remote.ProjetRemote;
@Repository
public interface ProjetRemoteRepository extends CrudRepository<ProjetRemote, Long>{
	
	public Optional<ProjetRemote> findByCodeProjet(String codeProjet) ;

	public List<ProjetRemote> findByDateSignatureBetween(LocalDate dateDebut, LocalDate dateFin) ;
	
	public List<ProjetRemote> findByMaitreOuvrageAndDateSignatureBetween(String maitreOuvrage, 
			LocalDate dateDebut, LocalDate dateFin) ;

}
