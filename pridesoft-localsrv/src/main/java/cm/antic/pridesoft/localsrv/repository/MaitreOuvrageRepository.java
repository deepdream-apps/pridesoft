package cm.antic.pridesoft.localsrv.repository;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import cm.antic.pridesoft.datamodel.local.MaitreOuvrage;

@Repository
public interface MaitreOuvrageRepository extends MongoRepository<MaitreOuvrage, Long>{
	public Optional<MaitreOuvrage> findBySigle (String sigle) ;
}
