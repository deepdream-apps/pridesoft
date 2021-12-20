package cm.antic.pridesoft.remotesrv.repository;
import java.time.LocalDate;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.antic.pridesoft.datamodel.remote.CneRemote;

@Repository
public interface CneRemoteRepository extends CrudRepository<CneRemote, Long>{

	public List<CneRemote> findByDateSignatureBetween(LocalDate date1, LocalDate date2) ;
	
}
