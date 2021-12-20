package cm.antic.pridesoft.remotesrv.repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.antic.pridesoft.datamodel.remote.FicheSuiviRemote;
@Repository
public interface FicheSuiviRemoteRepository extends CrudRepository<FicheSuiviRemote, Long>{
	public Optional<FicheSuiviRemote> findByCodeProjet (String codeProjet) ;

	public List<FicheSuiviRemote> findByMaitreOuvrageAndDateSignatureBetween(String maitreOuvrage, LocalDate date1,
			LocalDate date2) ;
	
	public List<FicheSuiviRemote> findByDateSignatureBetween(LocalDate date1, LocalDate date2) ;
}
