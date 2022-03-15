package cm.antic.pridesoft.web.util;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import cm.antic.pridesoft.datamodel.local.Utilisateur;

@Service
public class AuthentificationService implements UserDetailsService{	
	private Logger logger = Logger.getLogger(AuthentificationService.class.getName()) ;
	@Autowired
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
 

    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    	String url = "http://"+env.getProperty("app.localsrv.host")+":"+env.getProperty("app.localsrv.port")+"/ws/utilisateur/login/{login}" ;
    	logger.info("Authentification à l'URL : "+url);
    	Utilisateur utilisateur = rest.getForObject(url, Utilisateur.class, login) ;
		UserDetails details = User.withUsername(utilisateur.getLogin()).password(utilisateur.getMotDePasse()).authorities(utilisateur.getProfil()).build();
		return details ;
    }
    
}
