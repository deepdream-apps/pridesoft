package cm.antic.pridesoft.localsrv.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import cm.antic.pridesoft.datamodel.local.Region;

@Repository
public interface RegionRepository extends MongoRepository<Region, Long>{
	
	public Region findByCode(String code);
}
