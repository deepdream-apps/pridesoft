package cm.antic.pridesoft.localsrv.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cm.antic.pridesoft.datamodel.local.Utilisateur;
import cm.antic.pridesoft.localsrv.repository.UtilisateurRepository;
@Transactional
@Service
public class UtilisateurService {
	private Logger logger = Logger.getLogger(UtilisateurService.class.getName()) ;
	@Autowired
	private UtilisateurRepository utilisateurRepository ;
	
	public Utilisateur creer (Utilisateur utilisateur) throws Exception {
		try {
			utilisateur.setId(UUID.randomUUID().toString());
			Utilisateur utilisateurCree  = utilisateurRepository.save(utilisateur) ;
			return utilisateurCree ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw ex ;
		}
	}
	
	public Utilisateur modifier (Utilisateur utilisateur) throws Exception {
		try {
			Utilisateur utilisateurModifie  = utilisateurRepository.save(utilisateur) ;
			return utilisateurModifie ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw ex ;
		}
	}
	
	
	public void supprimer (Utilisateur utilisateur) throws Exception {
		try {
			utilisateurRepository.delete(utilisateur) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw ex ;
		}
	}
	
	
	public Utilisateur rechercher (String id) throws Exception {
		try {
			Optional<Utilisateur> utilisateurOpt  = utilisateurRepository.findById(id) ;
			return utilisateurOpt.orElseThrow(Exception::new) ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw ex ;
		}
	}
	
	public Utilisateur rechercherLogin (String login) throws Exception {
		try {
			Utilisateur utilisateur  = utilisateurRepository.findByLogin(login) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw ex ;
		}
	}
	
	
	public List<Utilisateur> rechercher (Utilisateur utilisateur)  {
		try {
			Iterable<Utilisateur> projets  = utilisateurRepository.findAll() ;
			List<Utilisateur> listeProjets = new ArrayList<Utilisateur>() ;
			projets.forEach(listeProjets::add) ;
			return listeProjets ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw ex ;
		}
	}
}
