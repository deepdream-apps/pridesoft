package cm.antic.pridesoft.localsrv.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import cm.antic.pridesoft.datamodel.local.Categorie;
@Repository
public interface CategorieRepository extends MongoRepository<Categorie, String>{
	
	
}
