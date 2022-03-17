package cm.antic.pridesoft.localsrv.webservice;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.antic.pridesoft.datatransfer.local.NombreMontantDTO;
import cm.antic.pridesoft.localsrv.service.ProjetTicService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/projettic")
public class ProjetTicGlobalDataWS {
	private ProjetTicService projetTicService ;
	
	public ProjetTicGlobalDataWS(ProjetTicService projetTicService) {
		this.projetTicService = projetTicService ;
	}
	
	
	@GetMapping("/global/data")
	public List<NombreMontantDTO> getGlobalData() {
		log.info(String.format("Recerche des donnees globales..."));
		return projetTicService.getGlobalStats() ;
	}
	
	
}
