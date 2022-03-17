package cm.antic.pridesoft.localsrv.repository;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import cm.antic.pridesoft.datamodel.local.Cne;

@Repository
public interface CneRepository extends MongoRepository<Cne, String>{
	
	public Optional<Cne> findByCodeProjet (String codeProjet) ;
	
	public List<Cne> findByDateSignatureBetween (LocalDate date1, LocalDate date2)  ;
	
	public List<Cne> findByValideAndDateSignatureBetween (int valide, LocalDate dateDebut, LocalDate dateFin) ;
	
	public Long countByValide(int valide);
}
