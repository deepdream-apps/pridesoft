package cm.antic.pridesoft.localsrv.repository;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cm.antic.pridesoft.datamodel.local.ProjetTic;

@Repository
public interface ProjetTicRepository extends MongoRepository<ProjetTic, String> {
	public Optional<ProjetTic> findByCodeProjet (String codeProjet) ;
	public List<ProjetTic> findByDateSignatureBetween (LocalDate dateDebut, LocalDate dateFin) ;
}
