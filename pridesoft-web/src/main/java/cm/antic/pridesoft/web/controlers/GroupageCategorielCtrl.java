package cm.antic.pridesoft.web.controlers;
import java.io.Serializable;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.Resource;

import cm.antic.pridesoft.datatransfer.local.DateDebutFinDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetCategorieDTO;

@Controller
@SessionAttributes({"utilisateurCourant", "localUrl"})
@RequestMapping("/groupagecategoriel")
public class GroupageCategorielCtrl implements Serializable{
	@Autowired
	private RestTemplate rest ;
	@Autowired
	private Environment env ;
	@Autowired
	private CacheManager cacheManager ;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
	
	private LocalDate dateDebut = LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 1) ;
	private LocalDate dateFin = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 31) ;
	
	
	@GetMapping ("/liste")
	public String getDefaultGroupes (Model model, @SessionAttribute("localUrl") String localUrl) {
		ResponseEntity<ProjetCategorieDTO[]> response = rest.getForEntity(localUrl+"/ws/projettic/data/groupagecategoriel/periode/{dateDebut}/{dateFin}", 
				ProjetCategorieDTO[].class, dateDebut.format(formatter), dateFin.format(formatter)) ;
		List<ProjetCategorieDTO> listeGroupes = Arrays.asList(response.getBody());
		model.addAttribute("listeGroupes", listeGroupes) ;
		model.addAttribute("periode", new DateDebutFinDTO(dateDebut, dateFin)) ;
		cacheManager.getCache("dateCache").put("dateDebut", dateDebut) ;
		cacheManager.getCache("dateCache").put("dateFin", dateFin) ;
	    return "projettic/groupagecategoriel" ; 
	}
	
	

	@PostMapping ("/liste")
	public String getGroupes (Model model, DateDebutFinDTO periode, @SessionAttribute("localUrl") String localUrl) {
		LocalDate dateDebut = periode.getDateDebut() ;
		LocalDate dateFin = periode.getDateFin() ;
		
		ResponseEntity<ProjetCategorieDTO[]> response = rest.getForEntity(localUrl+"/ws/projettic/data/groupagecategoriel/periode/{dateDebut}/{dateFin}", 
				ProjetCategorieDTO[].class, dateDebut.format(formatter), dateFin.format(formatter)) ;
		List<ProjetCategorieDTO> listeGroupes = Arrays.asList(response.getBody());
		
		model.addAttribute("listeGroupes", listeGroupes) ;
		model.addAttribute("periode", periode) ;
		
		cacheManager.getCache("dateCache").put("dateDebut", dateDebut) ;
		cacheManager.getCache("dateCache").put("dateFin", dateFin) ;
		return "projettic/groupagecategoriel" ; 
	}
	
	
	@GetMapping ("/barchart")
	public @ResponseBody  ResponseEntity<Resource> getBarChart (@SessionAttribute("localUrl") String localUrl) {
		
		LocalDate dateDebut = (LocalDate) cacheManager.getCache("dateCache").get("dateDebut").get() ;
		LocalDate dateFin = (LocalDate) cacheManager.getCache("dateCache").get("dateFin").get() ;
		
		if(dateDebut == null || dateFin == null) {
			dateDebut = LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 1) ;
			dateFin = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 31) ;
		}

		ResponseEntity<byte[]> response = rest.getForEntity(localUrl+"/ws/projettic/barchart/groupagecategoriel/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}", 
				byte[].class, dateDebut.format(formatter), dateFin.format(formatter), 600, 400) ;
		
		byte[]  barchartBytes =  response.getBody();
		
		HttpHeaders header = new HttpHeaders();
	    header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=img1"+System.currentTimeMillis()+".jpg");
	    header.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    header.add("Pragma", "no-cache");
	    header.add("Expires", "0");

	    ByteArrayResource resource = new ByteArrayResource(barchartBytes);

	    return ResponseEntity.ok()
	                .headers(header)
	                .contentLength(barchartBytes.length)
	                .contentType(MediaType.parseMediaType("application/octet-stream"))
	                .body(resource);
	}
	
	
	
	@GetMapping ("/barchart2")
	public @ResponseBody  ResponseEntity<Resource> getBarChart2 (@SessionAttribute("localUrl") String localUrl) {
		
		LocalDate dateDebut = (LocalDate) cacheManager.getCache("dateCache").get("dateDebut").get() ;
		LocalDate dateFin = (LocalDate) cacheManager.getCache("dateCache").get("dateFin").get() ;

		if(dateDebut == null || dateFin == null) {
			dateDebut = LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 1) ;
			dateFin = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 31) ;
		}

		ResponseEntity<byte[]> response = rest.getForEntity(localUrl+"/ws/projettic/barchart/groupagecategoriel2/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}", 
				byte[].class, dateDebut.format(formatter), dateFin.format(formatter), 600, 400) ;
		
		byte[]  barchartBytes =  response.getBody();
		
		HttpHeaders header = new HttpHeaders();
	    header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=img2"+System.currentTimeMillis()+".jpg");
	    header.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    header.add("Pragma", "no-cache");
	    header.add("Expires", "0");

	    ByteArrayResource resource = new ByteArrayResource(barchartBytes);

	    return ResponseEntity.ok()
	                .headers(header)
	                .contentLength(barchartBytes.length)
	                .contentType(MediaType.parseMediaType("application/octet-stream"))
	                .body(resource);
	}
	
	
	
	@GetMapping ("/piechart")
	public @ResponseBody  ResponseEntity<Resource> getPieChart (@SessionAttribute("localUrl") String localUrl) {
		
		LocalDate dateDebut = (LocalDate) cacheManager.getCache("dateCache").get("dateDebut").get() ;
		LocalDate dateFin = (LocalDate) cacheManager.getCache("dateCache").get("dateFin").get() ;
		
		if(dateDebut == null || dateFin == null) {
			dateDebut = LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 1) ;
			dateFin = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 31) ;
		}

		ResponseEntity<byte[]> response = rest.getForEntity(localUrl+"/ws/projettic/piechart/groupagecategoriel/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}", 
				byte[].class, dateDebut.format(formatter), dateFin.format(formatter), 600, 400) ;
		
		byte[]  barchartBytes =  response.getBody();
		
		HttpHeaders header = new HttpHeaders();
	    header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=img3"+System.currentTimeMillis()+".jpg");
	    header.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    header.add("Pragma", "no-cache");
	    header.add("Expires", "0");

	    ByteArrayResource resource = new ByteArrayResource(barchartBytes);

	    return ResponseEntity.ok()
	                .headers(header)
	                .contentLength(barchartBytes.length)
	                .contentType(MediaType.parseMediaType("application/octet-stream"))
	                .body(resource);
	}
	
	
	
	@GetMapping ("/piechart2")
	public @ResponseBody  ResponseEntity<Resource> getPieChart2 (@SessionAttribute("localUrl") String localUrl) {
		
		LocalDate dateDebut = (LocalDate) cacheManager.getCache("dateCache").get("dateDebut").get() ;
		LocalDate dateFin = (LocalDate) cacheManager.getCache("dateCache").get("dateFin").get() ;
		
		if(dateDebut == null || dateFin == null ) {
			dateDebut = LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 1) ;
			dateFin = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 31) ;
		}

		ResponseEntity<byte[]> response = rest.getForEntity(localUrl+"/ws/projettic/piechart/groupagecategoriel2/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}", 
				byte[].class, dateDebut.format(formatter), dateFin.format(formatter), 600, 400) ;
		
		byte[]  barchartBytes =  response.getBody();
		
		HttpHeaders header = new HttpHeaders();
	    header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=img4"+System.currentTimeMillis()+".jpg");
	    header.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    header.add("Pragma", "no-cache");
	    header.add("Expires", "0");

	    ByteArrayResource resource = new ByteArrayResource(barchartBytes);

	    return ResponseEntity.ok()
	                .headers(header)
	                .contentLength(barchartBytes.length)
	                .contentType(MediaType.parseMediaType("application/octet-stream"))
	                .body(resource);
	}
	
	
	
}