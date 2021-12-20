package cm.antic.pridesoft.localsrv.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cm.antic.pridesoft.datamodel.local.ProjetTic;

@Repository
public interface ProjetTicRepository extends MongoRepository<ProjetTic, Long> {

}
