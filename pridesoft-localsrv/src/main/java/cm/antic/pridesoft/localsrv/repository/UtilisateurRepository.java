package cm.antic.pridesoft.localsrv.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;
import cm.antic.pridesoft.datamodel.local.Utilisateur;

@Repository
public interface UtilisateurRepository extends MongoRepository<Utilisateur, Long>{
	public Utilisateur findByLogin(String login) ;
}
