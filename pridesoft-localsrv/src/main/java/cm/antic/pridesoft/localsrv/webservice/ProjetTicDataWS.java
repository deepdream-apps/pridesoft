package cm.antic.pridesoft.localsrv.webservice;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.antic.pridesoft.datamodel.local.ProjetTic;
import cm.antic.pridesoft.datatransfer.local.ProjetAnnuelDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetCategorieDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetMaitreOuvrageDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetRegionDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetSecteurActiviteDTO;
import cm.antic.pridesoft.localsrv.service.ProjetTicService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/projettic/data")
public class ProjetTicDataWS {
	private ProjetTicService projetTicService ;
	
	public ProjetTicDataWS(ProjetTicService projetTicService) {
		this.projetTicService = projetTicService ;
	}
	
	@PostMapping("/update")
	public ProjetTic editer (@RequestBody ProjetTic projetTic) {
		return projetTicService.modifier(projetTic) ;
	}


	@GetMapping("/groupageannuel/periode/{dateDebut}/{dateFin}")
	public List<ProjetAnnuelDTO> collecterDonneesProjetAnnuelles(@PathVariable("dateDebut") String dateDebut, 
			@PathVariable("dateFin") String dateFin) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		return projetTicService.collecterProjetAnnee(debut, fin) ;
	}
	

	@GetMapping("/groupageregional/periode/{dateDebut}/{dateFin}/{inclureSC}")
	public List<ProjetRegionDTO> collecterProjetRegion(@PathVariable("dateDebut") String dateDebut, 
			@PathVariable("dateFin") String dateFin, @PathVariable("inclureSC") Boolean inclureSC) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		return projetTicService.collecterProjetRegion(debut, fin, inclureSC) ;
	}
	
	
	
	@GetMapping("/groupagecategoriel/periode/{dateDebut}/{dateFin}")
	public List<ProjetCategorieDTO> collecterDonneesProjetCategoriels(@PathVariable("dateDebut") String dateDebut, 
			@PathVariable("dateFin") String dateFin) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		return projetTicService.collecterProjetCategorie(debut, fin) ;
	}
	
	
	@GetMapping("/groupagesectoriel/periode/{dateDebut}/{dateFin}")
	public List<ProjetSecteurActiviteDTO> collecterDonneesProjetSectoriels(@PathVariable("dateDebut") String dateDebut, 
			@PathVariable("dateFin") String dateFin) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		return projetTicService.collecterProjetSecteurActivite(debut, fin) ;
	}
	
	
	@GetMapping("/groupageparmo/periode/{dateDebut}/{dateFin}")
	public List<ProjetMaitreOuvrageDTO> collecterDonneesProjetParMaitreOuvrage(@PathVariable("dateDebut") String dateDebut, 
			@PathVariable("dateFin") String dateFin) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		return projetTicService.collecterProjetMaitreOuvrage(debut, fin) ;
	}
	
	
	@GetMapping("/periode/{dateDebut}/{dateFin}")
	public List<ProjetTic> rechercher(@PathVariable("dateDebut") String dateDebut, 
			@PathVariable("dateFin") String dateFin) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		return projetTicService.rechercher(debut, fin) ;
	}
	
	

	@GetMapping("/codeProjet/{codeProjet}")
	public ProjetTic rechercher(@PathVariable("codeProjet") String codeProjet) {
		return projetTicService.rechercher(codeProjet) ;
	}
	
	
}
