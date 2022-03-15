package cm.antic.pridesoft.localsrv.webservice;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.antic.pridesoft.datatransfer.local.ProjetAnnuelDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetCategorieDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetMaitreOuvrageDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetRegionDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetSecteurActiviteDTO;
import cm.antic.pridesoft.localsrv.service.ProjetTicService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/projettic/piechart")
public class ProjetTicPieChartWS {
	private ProjetTicService projetTicService ;
	
	public ProjetTicPieChartWS(ProjetTicService projetTicService) {
		this.projetTicService = projetTicService ;
	}

	
	@GetMapping("/groupageannuel/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterCamemberProjetsAnnuels(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		
		log.info(String.format("Recherche des projets de %s à %s", dateDebut, dateFin));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetAnnuelDTO> listeProjetsTic = projetTicService.collecterProjetAnnee(debut, fin) ;
		
		final DefaultPieDataset dataset = new DefaultPieDataset() ; 
		
		listeProjetsTic.forEach(groupe -> {
			dataset.setValue(String.valueOf(groupe.getAnnee()), groupe.getMontant());  
		}) ;
		
		JFreeChart pieChart = ChartFactory.createPieChart("Répartition des montants des projets TIC par année du "+dateDebut+ " au "+dateFin,             
		         dataset, true, true, false) ;
		
		File pieChartFile = new File("pie_chart0.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(pieChartFile, pieChart, largeur == 0 ? 600:largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(pieChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	@GetMapping("/groupageannuel2/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterCamemberProjetsAnnuels2(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		
		log.info(String.format("Recherche des projets de %s à %s", dateDebut, dateFin));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetAnnuelDTO> listeProjetsTic = projetTicService.collecterProjetAnnee(debut, fin) ;
		
		final DefaultPieDataset dataset = new DefaultPieDataset() ; 
		
		listeProjetsTic.forEach(groupe -> {
			dataset.setValue(String.valueOf(groupe.getAnnee()), groupe.getNombre());  
		}) ;
		
		JFreeChart pieChart = ChartFactory.createPieChart("Répartition des nombres de projets TIC par année du "+dateDebut+ " au "+dateFin,             
		         dataset, true, true, false) ;
		
		File pieChartFile = new File("pie_chart1.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(pieChartFile, pieChart, largeur == 0 ? 600:largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(pieChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	@GetMapping("/groupageregional/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}/{inclureSC}")
	public byte[] collecterCamemberProjetsRegionauxs(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur, @PathVariable("inclureSC") Boolean inclureSC) {
		
		log.info(String.format("Recherche des projets de %s à %s", dateDebut, dateFin));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetRegionDTO> listeProjetsTic = projetTicService.collecterProjetRegion(debut, fin, inclureSC) ;
		
		final DefaultPieDataset dataset = new DefaultPieDataset() ; 
		
		listeProjetsTic.forEach(groupe -> {
			dataset.setValue(String.valueOf(groupe.getRegion()), groupe.getMontant());  
		}) ;
		
		JFreeChart pieChart = ChartFactory.createPieChart("Répartition des montants des projets TIC par région du "+dateDebut+ " au "+dateFin,             
		         dataset, true, true, false) ;
		
		File pieChartFile = new File("pie_chart2.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(pieChartFile, pieChart, largeur == 0 ? 600:largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(pieChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	@GetMapping("/groupageregional2/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}/{inclureSC}")
	public byte[] collecterCamemberProjetsRegionauxs2(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur, @PathVariable("inclureSC") Boolean inclureSC) {
		
		log.info(String.format("Recherche des projets de %s à %s", dateDebut, dateFin));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetRegionDTO> listeProjetsTic = projetTicService.collecterProjetRegion(debut, fin, inclureSC) ;
		
		final DefaultPieDataset dataset = new DefaultPieDataset() ; 
		
		listeProjetsTic.forEach(groupe -> {
			dataset.setValue(String.valueOf(groupe.getRegion()), groupe.getNombre());  
		}) ;
		
		JFreeChart pieChart = ChartFactory.createPieChart("Répartition des nombres de projets TIC par région du "+dateDebut+ " au "+dateFin,             
		         dataset, true, true, false) ;
		
		File pieChartFile = new File("pie_chart3.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(pieChartFile, pieChart, largeur == 0 ? 600:largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(pieChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	@GetMapping("/groupagecategoriel/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterCamemberProjetsCategoriels(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		
		log.info(String.format("Recherche des projets de %s à %s", dateDebut, dateFin));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetCategorieDTO> listeProjetsTic = projetTicService.collecterProjetCategorie(debut, fin) ;
		
		final DefaultPieDataset dataset = new DefaultPieDataset() ; 
		
		listeProjetsTic.forEach(groupe -> {
			dataset.setValue(String.valueOf(groupe.getCategorie()), groupe.getMontant());  
		}) ;
		
		JFreeChart pieChart = ChartFactory.createPieChart("Répartition des montants des projets TIC par catégorie du "+dateDebut+ " au "+dateFin,             
		         dataset, true, true, false) ;
		
		File pieChartFile = new File("pie_chart12.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(pieChartFile, pieChart, largeur == 0 ? 600:largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(pieChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	@GetMapping("/groupagecategoriel2/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterCamemberProjetsCategoriel2(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		
		log.info(String.format("Recherche des projets de %s à %s", dateDebut, dateFin));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetCategorieDTO> listeProjetsTic = projetTicService.collecterProjetCategorie(debut, fin) ;
		
		final DefaultPieDataset dataset = new DefaultPieDataset() ; 
		
		listeProjetsTic.forEach(groupe -> {
			dataset.setValue(String.valueOf(groupe.getCategorie()), groupe.getNombre());  
		}) ;
		
		JFreeChart pieChart = ChartFactory.createPieChart("Répartition des nombres de projets TIC par année du "+dateDebut+ " au "+dateFin,             
		         dataset, true, true, false) ;
		
		File pieChartFile = new File("pie_chart13.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(pieChartFile, pieChart, largeur == 0 ? 600:largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(pieChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	@GetMapping("/groupagesectoriel/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterCamemberProjetsSectoriels(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		
		log.info(String.format("Recherche des projets de %s à %s", dateDebut, dateFin));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetSecteurActiviteDTO> listeProjetsTic = projetTicService.collecterProjetSecteurActivite(debut, fin) ;
		
		final DefaultPieDataset dataset = new DefaultPieDataset() ; 
		
		listeProjetsTic.forEach(groupe -> {
			dataset.setValue(String.valueOf(groupe.getSecteurActivite()), groupe.getMontant());  
		}) ;
		
		JFreeChart pieChart = ChartFactory.createPieChart("Répartition des montants des projets TIC par secteur d'activité du "+dateDebut+ " au "+dateFin,             
		         dataset, true, true, false) ;
		
		File pieChartFile = new File("pie_chart14.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(pieChartFile, pieChart, largeur == 0 ? 600:largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(pieChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	@GetMapping("/groupagesectoriel2/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterCamemberProjetsSectoriel2(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		
		log.info(String.format("Recherche des projets de %s à %s", dateDebut, dateFin));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetSecteurActiviteDTO> listeProjetsTic = projetTicService.collecterProjetSecteurActivite(debut, fin) ;
		
		final DefaultPieDataset dataset = new DefaultPieDataset() ; 
		
		listeProjetsTic.forEach(groupe -> {
			dataset.setValue(String.valueOf(groupe.getSecteurActivite()), groupe.getNombre());  
		}) ;
		
		JFreeChart pieChart = ChartFactory.createPieChart("Répartition des nombres de projets TIC par secteur d'activité du "+dateDebut+ " au "+dateFin,             
		         dataset, true, true, false) ;
		
		File pieChartFile = new File("pie_chart150.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(pieChartFile, pieChart, largeur == 0 ? 600:largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(pieChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	@GetMapping("/groupageparmo/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterCamemberProjetsParMO(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		
		log.info(String.format("Recherche des projets de %s à %s", dateDebut, dateFin));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetMaitreOuvrageDTO> listeProjetsTic = projetTicService.collecterProjetMaitreOuvrage(debut, fin) ;
		
		final DefaultPieDataset dataset = new DefaultPieDataset() ; 
		
		listeProjetsTic.forEach(groupe -> {
			dataset.setValue(String.valueOf(groupe.getMaitreOuvrage()), groupe.getMontant());  
		}) ;
		
		JFreeChart pieChart = ChartFactory.createPieChart("Répartition des montants des projets TIC par maître d'ouvrage du "+dateDebut+ " au "+dateFin,             
		         dataset, true, true, false) ;
		
		File pieChartFile = new File("pie_chart16.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(pieChartFile, pieChart, largeur == 0 ? 600:largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(pieChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	@GetMapping("/groupageparmo2/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterCamemberProjetsParMO2(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		
		log.info(String.format("Recherche des projets de %s à %s", dateDebut, dateFin));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetMaitreOuvrageDTO> listeProjetsTic = projetTicService.collecterProjetMaitreOuvrage(debut, fin) ;
		
		final DefaultPieDataset dataset = new DefaultPieDataset() ; 
		
		listeProjetsTic.forEach(groupe -> {
			dataset.setValue(String.valueOf(groupe.getMaitreOuvrage()), groupe.getNombre());  
		}) ;
		
		JFreeChart pieChart = ChartFactory.createPieChart("Répartition des nombres de projets TIC par maître d'ouvrage du "+dateDebut+ " au "+dateFin,             
		         dataset, true, true, false) ;
		
		File pieChartFile = new File("pie_chart17.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(pieChartFile, pieChart, largeur == 0 ? 600:largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(pieChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
}
