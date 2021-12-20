package cm.antic.pridesoft.remotesrv.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cm.antic.pridesoft.datamodel.remote.PaysRemote;
import cm.antic.pridesoft.datamodel.remote.RegionRemote;
@Repository
public interface RegionRemoteRepository extends CrudRepository<RegionRemote, Long>{
	
	public Optional<RegionRemote> findByCode (String code) ;
	public List<RegionRemote> findByPays (PaysRemote pays) ;
}
