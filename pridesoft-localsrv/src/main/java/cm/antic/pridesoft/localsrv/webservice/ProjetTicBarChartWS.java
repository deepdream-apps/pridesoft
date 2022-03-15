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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
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
@RequestMapping("/ws/projettic/barchart")
public class ProjetTicBarChartWS {
	private ProjetTicService projetTicService ;
	
	public ProjetTicBarChartWS(ProjetTicService projetTicService) {
		this.projetTicService = projetTicService ;
	}

	
	@GetMapping("/groupageannuel/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterDiagrammeProjetAnnuel(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") Integer largeur, @PathVariable("hauteur") Integer hauteur) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetAnnuelDTO> listeProjetsTic = projetTicService.collecterProjetAnnee(debut, fin) ;
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
		listeProjetsTic.forEach(groupe -> {
			dataset.addValue( groupe.getMontant(), String.valueOf(groupe.getAnnee()), "Année");  
		}) ;
		
		JFreeChart barChart = ChartFactory.createBarChart("Montant des projets TIC par année du "+dateDebut+ " au "+dateFin, "Année",  "Score",             
		         dataset, PlotOrientation.VERTICAL, true, true, true) ;
		
		File barChartFile = new File("bar_chart0.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(barChartFile, barChart, largeur == 0 ? 600 : largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(barChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	@GetMapping("/groupageannuel2/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterDiagrammeProjetAnnuel2(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") Integer largeur, @PathVariable("hauteur") Integer hauteur) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetAnnuelDTO> listeProjetsTic = projetTicService.collecterProjetAnnee(debut, fin) ;
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
		listeProjetsTic.forEach(groupe -> {
			dataset.addValue( groupe.getNombre(), String.valueOf(groupe.getAnnee()), "Année") ;
		}) ;
		
		JFreeChart barChart = ChartFactory.createBarChart("Nombre des projets TIC par année du "+dateDebut+ " au "+dateFin, "Année",  "Score",             
		         dataset, PlotOrientation.VERTICAL, true, true, true) ;
		
		File barChartFile = new File("bar_chart1.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(barChartFile, barChart, largeur == 0 ? 600 : largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(barChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	@GetMapping("/groupageregional/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}/{inclureSC}")
	public byte[] collecterDiagrammeProjetRegional(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") Integer largeur, @PathVariable("hauteur") Integer hauteur, @PathVariable("inclureSC") Boolean inclureSC) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetRegionDTO> listeProjetsTic = projetTicService.collecterProjetRegion(debut, fin, inclureSC) ;
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
		listeProjetsTic.forEach(groupe -> {
			dataset.addValue( groupe.getMontant(), String.valueOf(groupe.getRegion()), "Région");  
		}) ;
		
		JFreeChart barChart = ChartFactory.createBarChart("Montant des projets TIC par région du "+dateDebut+ " au "+dateFin, 
				"Région",  "Score",             
		         dataset, PlotOrientation.VERTICAL, true, true, true) ;
		
		File barChartFile = new File("bar_chart2.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(barChartFile, barChart, largeur == 0 ? 600 : largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(barChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	@GetMapping("/groupageregional2/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}/{inclureSC}")
	public byte[] collecterDiagrammeProjetRegional2(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur, @PathVariable("inclureSC") Boolean inclureSC) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetRegionDTO> listeProjetsTic = projetTicService.collecterProjetRegion(debut, fin, inclureSC) ;
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
		listeProjetsTic.forEach(groupe -> {
			dataset.addValue(groupe.getNombre(), String.valueOf(groupe.getRegion()), "Région");  
		}) ;
		
		JFreeChart barChart = ChartFactory.createBarChart("Nombre des projets TIC par région du "+dateDebut+ " au "+dateFin, 
				"Région",  "Score",             
		         dataset, PlotOrientation.VERTICAL, true, true, true) ;
		
		File barChartFile = new File("bar_chart3.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(barChartFile, barChart, largeur == 0 ? 600 : largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(barChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	@GetMapping("/groupagecategoriel/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterDiagrammeProjetCategoriel(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetCategorieDTO> listeProjetsTic = projetTicService.collecterProjetCategorie(debut, fin) ;
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
		listeProjetsTic.forEach(groupe -> {
			dataset.addValue( groupe.getMontant(), String.valueOf(groupe.getCategorie()), "Catégorie");  
		}) ;
		
		JFreeChart barChart = ChartFactory.createBarChart("Montant des projets TIC par catégorie du "+dateDebut+ " au "+dateFin, "Année",  "Score",             
		         dataset, PlotOrientation.VERTICAL, true, true, true) ;
		
		File barChartFile = new File("bar_chart4.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(barChartFile, barChart, largeur == 0 ? 600 : largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(barChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	@GetMapping("/groupagecategoriel2/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterDiagrammeProjetCategoriel2(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetCategorieDTO> listeProjetsTic = projetTicService.collecterProjetCategorie(debut, fin) ;
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
		listeProjetsTic.forEach(groupe -> {
			dataset.addValue( groupe.getNombre(), String.valueOf(groupe.getCategorie()), "Catégorie") ;
		}) ;
		
		JFreeChart barChart = ChartFactory.createBarChart("Nombre des projets TIC par catégorie du "+dateDebut+ " au "+dateFin, "Année",  "Score",             
		         dataset, PlotOrientation.VERTICAL, true, true, true) ;
		
		File barChartFile = new File("bar_chart5.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(barChartFile, barChart, largeur == 0 ? 600 : largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(barChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	
	@GetMapping("/groupagesectoriel/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterDiagrammeProjetSectoriel(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetSecteurActiviteDTO> listeProjetsTic = projetTicService.collecterProjetSecteurActivite(debut, fin) ;
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
		listeProjetsTic.forEach(groupe -> {
			dataset.addValue( groupe.getMontant(), String.valueOf(groupe.getSecteurActivite()), "Secteur d'activité");  
		}) ;
		
		JFreeChart barChart = ChartFactory.createBarChart("Montant des projets TIC par secteur d'activité du "+dateDebut+ " au "+dateFin, "Année",  "Score",             
		         dataset, PlotOrientation.VERTICAL, true, true, true) ;
		
		File barChartFile = new File("bar_chart6.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(barChartFile, barChart, largeur == 0 ? 600 : largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(barChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	@GetMapping("/groupagesectoriel2/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterDiagrammeProjetSectoriel2(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetSecteurActiviteDTO> listeProjetsTic = projetTicService.collecterProjetSecteurActivite(debut, fin) ;
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
		listeProjetsTic.forEach(groupe -> {
			dataset.addValue( groupe.getNombre(), String.valueOf(groupe.getSecteurActivite()), "Secteur d'activité") ;
		}) ;
		
		JFreeChart barChart = ChartFactory.createBarChart("Nombre des projets TIC par secteur d'activité du "+dateDebut+ " au "+dateFin, "Année",  "Score",             
		         dataset, PlotOrientation.VERTICAL, true, true, true) ;
		
		File barChartFile = new File("bar_chart7.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(barChartFile, barChart, largeur == 0 ? 600 : largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(barChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	@GetMapping("/groupageparmo/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterDiagrammeProjetParMO(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetMaitreOuvrageDTO> listeProjetsTic = projetTicService.collecterProjetMaitreOuvrage(debut, fin) ;
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
		listeProjetsTic.forEach(groupe -> {
			dataset.addValue( groupe.getMontant(), String.valueOf(groupe.getMaitreOuvrage()), "Maître d'ouvrage");  
		}) ;
		
		JFreeChart barChart = ChartFactory.createBarChart("Montant des projets TIC par maître d'ouvrage du "+dateDebut+ " au "+dateFin, "Année",  "Score",             
		         dataset, PlotOrientation.VERTICAL, true, true, true) ;
		
		File barChartFile = new File("bar_chart8.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(barChartFile, barChart, largeur == 0 ? 600 : largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(barChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
	
	
	@GetMapping("/groupageparmo2/periode/{dateDebut}/{dateFin}/{largeur}/{hauteur}")
	public byte[] collecterDiagrammeProjetParMO2(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("largeur") int largeur, @PathVariable("hauteur") int hauteur) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		List<ProjetMaitreOuvrageDTO> listeProjetsTic = projetTicService.collecterProjetMaitreOuvrage(debut, fin) ;
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
		listeProjetsTic.forEach(groupe -> {
			dataset.addValue( groupe.getNombre(), String.valueOf(groupe.getMaitreOuvrage()), "Maître d'ouvrage") ;
		}) ;
		
		JFreeChart barChart = ChartFactory.createBarChart("Nombre des projets TIC par maître d'ouvrage du "+dateDebut+ " au "+dateFin, "Année",  "Score",             
		         dataset, PlotOrientation.VERTICAL, true, true, true) ;
		
		File barChartFile = new File("bar_chart9.jpeg") ;
		
		try {
			ChartUtils.saveChartAsJPEG(barChartFile, barChart, largeur == 0 ? 600 : largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return IOUtils.toByteArray(new FileInputStream(barChartFile)) ;
		}catch(IOException  ex) {
			return null ;
		}
	}
	
}
