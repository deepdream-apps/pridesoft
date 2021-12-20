package cm.antic.pridesoft.remotesrv.repository;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cm.antic.pridesoft.datamodel.remote.SecteurActiviteRemote;
@Repository
public interface SecteurActiviteRemoteRepository extends CrudRepository<SecteurActiviteRemote, Long>{
	public Optional<SecteurActiviteRemote> findBySigle(String sigle) ;
}
