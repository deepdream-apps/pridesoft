package cm.antic.pridesoft.localsrv.repository;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cm.antic.pridesoft.datamodel.local.Projet;

@Repository
public interface ProjetRepository extends MongoRepository<Projet, Long>{
	public Optional<Projet> findByCodeProjet (String codeProjet) ;
	public List<Projet> findByDateSignatureBetween (LocalDate date1, LocalDate date2) ;
	public List<Projet> findByValideAndDateSignatureBetween(Integer valide, LocalDate date1, LocalDate date2) ;

}
