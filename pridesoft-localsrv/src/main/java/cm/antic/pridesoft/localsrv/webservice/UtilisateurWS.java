package cm.antic.pridesoft.localsrv.webservice;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.antic.pridesoft.datamodel.local.Utilisateur;
import cm.antic.pridesoft.localsrv.service.UtilisateurService;
import lombok.extern.log4j.Log4j2;
@Log4j2
@RestController
@RequestMapping("/ws/utilisateur")
public class UtilisateurWS {
	private Logger logger = Logger.getLogger(UtilisateurWS.class.getName()) ;
	@Autowired
	private UtilisateurService utilisateurService ;
	
	@PostMapping("/ajout")
	public Utilisateur ajouter (@RequestBody Utilisateur utilisateur) {
		try {
			logger.log(Level.INFO, "Ajout du projet");
			Utilisateur utilisateurAjoute = utilisateurService.creer(utilisateur) ;
			return utilisateurAjoute ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null ;
		}
	}
	
	
	@PutMapping("/modification")
	public Utilisateur modifier (@RequestBody Utilisateur utilisateur) {
		try {
			logger.log(Level.INFO, "Modification du projet");
			Utilisateur utilisateurModifie = utilisateurService.modifier(utilisateur) ;
			return utilisateurModifie ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null ;
		}
	}
	
	
	@DeleteMapping("/suppression")
	public int supprimer (@RequestBody Utilisateur utilisateur) {
		try {
			logger.log(Level.INFO, "Suppression du projet");
			utilisateurService.supprimer(utilisateur) ;
			return 1 ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return 0 ;
		}
	}
	
	
	@GetMapping("/id/{id}")
	public Utilisateur rechercher(@PathVariable("id") String id) {
		try {
			logger.log(Level.INFO, "Recerche du projet d'id "+id);
			Utilisateur utilisateur = utilisateurService.rechercher(id) ;
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null ;
		}
	}
	
	
	@GetMapping("/login/{login}")
	public Utilisateur rechercherLogin(@PathVariable("login") String login) {
		try {
			logger.log(Level.INFO, "Recerche de l'utilisateur login "+login);
			Utilisateur utilisateur = utilisateurService.rechercherLogin(login) ;
			log.info("utilisateur="+utilisateur);
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null ;
		}
	}
	
	
	
	@GetMapping("/all")
	public List<Utilisateur> rechercherTout() {
		return utilisateurService.rechercher(new Utilisateur()) ;
	}
	
	
	@GetMapping("/login/{login}/profil/{profil}")
	public Utilisateur rechercher(@PathVariable("login") String login, @PathVariable("profil") String profil) {
		try {
			logger.log(Level.INFO, "Recerche de l'utilisateur login "+login);
			Utilisateur utilisateur = utilisateurService.rechercher(login) ;
			
			return utilisateur ;
		}catch(Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			return null ;
		}
	}
	
}
