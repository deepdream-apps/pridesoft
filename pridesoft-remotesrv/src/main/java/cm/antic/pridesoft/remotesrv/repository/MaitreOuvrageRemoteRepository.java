package cm.antic.pridesoft.remotesrv.repository;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.antic.pridesoft.datamodel.remote.MaitreOuvrageRemote;
@Repository
public interface MaitreOuvrageRemoteRepository extends CrudRepository<MaitreOuvrageRemote, Long>{
	public Optional<MaitreOuvrageRemote> findByLibelle (String libelle) ;
	public Optional<MaitreOuvrageRemote> findBySigle (String sigle) ;
	
}
