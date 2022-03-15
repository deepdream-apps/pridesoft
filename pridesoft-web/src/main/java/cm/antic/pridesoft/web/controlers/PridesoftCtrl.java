package cm.antic.pridesoft.web.controlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import cm.antic.pridesoft.datamodel.local.Utilisateur;

@Controller
@SessionAttributes({"utilisateurCourant", "localUrl"})
public class PridesoftCtrl {
	private Logger logger = Logger.getLogger(PridesoftCtrl.class.getName()) ;
	@Autowired
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	
	@GetMapping ("/")
	 public String pageLogin (Model model) {
		logger.info("Recuperation du fichier auth-login  /") ;
	    return "layout/auth-login" ; 
	 }
	 
	 @GetMapping ("/page-login")
	 public String pageLogin2 (Model model) {
		 logger.info("Recuperation du fichier auth-login  /page-login") ;
	      return "layout/auth-login" ;  
	 }
	 
	 @GetMapping ("/my-account")
	 public String monCompte (Model model) {
	       return "admin/mon-compte" ;
	 }
	 
	 
	 @GetMapping ("/login-echec")
	 public String pageLogin3 (Model model) {
		 model.addAttribute("messageEchec", model.getAttribute("messageEchec") == null ? "Echec de l'opération" : model.getAttribute("messageEchec")) ;
	     return "login" ;
	 }
	 
	 
	 @GetMapping ("/forgot-password")
	 public String forgotPwd (Model model) {
	       return "forgot-password" ;
	 }
	 
	 @GetMapping ("/change-password")
	 public String changePwd (Model model) {
	       return "change-password" ;
	 }
	 
	 @GetMapping ("/disconnected")
	 public String disconnected (Model model) {
	       return "auth-login" ;
	 }
	 
	 @GetMapping ("/error")
	 public String pageErreur (Model model) {
	       return "auth-404" ;
	 }
	 
	 @GetMapping ("/dashboard")
	 public String dashboard (Model model, HttpServletRequest request) throws Exception{
		 try {
			 Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
			 String login = authentication.getName() ;
			 
			 String localUrl = "http://"+env.getProperty("app.localsrv.host")+":"+env.getProperty("app.localsrv.port") ;
			 model.addAttribute("localUrl", localUrl) ;
			 
			 Utilisateur utilisateur = rest.getForObject(localUrl+"/ws/utilisateur/login/{login}", Utilisateur.class, login) ;
			 
			 if (utilisateur == null) {
				 throw new SecurityException ("Echec ! Login ou mot de passe incorrect") ;
			 }
			
			 /*if (Duration.between(LocalDateTime.now(), utilisateur.getDateExpiration()).isNegative()) {
				 throw new SecurityException ("Echec ! Mot de passe expiré") ;
			 }
			 if (Duration.between(LocalDateTime.now(), utilisateur.getDateExpirationMdp()).isNegative() ) {
				 throw new SecurityException ("Echec ! Votre compte a expiré") ;
			 }*/
			 
			 model.addAttribute("utilisateurCourant", utilisateur) ;			
		     return "dashboard" ; 
		 }catch(SecurityException sex) {
			 model.addAttribute("messageEchec", sex.getMessage()) ;
			 logger.log(Level.SEVERE, sex.getMessage(), sex) ;
			 return "layout/auth-login" ;  
		 }catch(Exception ex) {
			 model.addAttribute("messageEchec", "Echec de l'opération") ;
			 logger.log(Level.SEVERE, ex.getMessage(), ex) ;
			 return "layout/auth-login" ;  
		 }
	 }
}
