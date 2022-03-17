package cm.antic.pridesoft.localsrv.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import cm.antic.pridesoft.datatransfer.local.ProjetRegionDTO;
import cm.antic.pridesoft.localsrv.service.ProjetTicService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ws/projettic")
public class ProjetTicRapportRegionWS {
	private ProjetTicService projetTicService ;
	
	public ProjetTicRapportRegionWS(ProjetTicService projetTicService) {
		this.projetTicService = projetTicService ;
	}
	
	
	@GetMapping("/rapportregional/periode/{dateDebut}/{dateFin}/{inclureSC}")
	public byte[] collecterDiagrammeProjetRegional(@PathVariable("dateDebut") String dateDebut, @PathVariable("dateFin") String dateFin, 
			@PathVariable("inclureSC") Boolean inclureSC) {
		log.info(String.format("Recerche des projets de %s à %s", dateDebut, dateFin));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;
		LocalDate debut = LocalDate.parse(dateDebut, formatter) ;
		LocalDate fin = LocalDate.parse(dateFin, formatter) ;
		
		List<ProjetRegionDTO> listeProjetsTic = projetTicService.collecterProjetRegion(debut, fin, inclureSC) ;
		
		String fileName = "diagramme_regional_"+dateDebut+"_"+dateFin+".pdf" ;
		
		try {
			Document document = new Document(PageSize.A4.rotate());
        
        	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName)) ;
        
        	document.open() ;
        	
        	PdfPTable taleauDonnees = genererTableauProjetsRegion(listeProjetsTic) ;
        	
        	Image diagrammeMontants = genererDiagrammeMontantProjetsTic(listeProjetsTic, 400, 300) ;
        	
        	Image diagrammeNombres = genererDiagrammeNombreProjetsTic(listeProjetsTic, 400, 300) ;
        	
        	document.add(taleauDonnees) ;
        	
        	document.add(diagrammeMontants) ;
        	
        	document.add(diagrammeNombres) ;
        	
        	
        	document.close() ;
		
			return IOUtils.toByteArray(new FileInputStream(fileName)) ;
		}catch(IOException | DocumentException  ex) {
			log.info(ex.getMessage(), ex) ;
			return null ;
		}
	}
	
	
	private PdfPTable genererTableauProjetsRegion(List<ProjetRegionDTO> listeProjetsRegion) throws DocumentException {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy") ;

		PdfPTable bodyTable = new PdfPTable(6) ; 
		bodyTable.setSpacingBefore (10.0f) ;
		bodyTable.setHorizontalAlignment(Element.ALIGN_CENTER) ;
		bodyTable.setWidthPercentage(75.f) ;
		bodyTable.setWidths(new int[]{10, 30, 20, 20, 15, 15});
		
		String[] labels = new String[] {"N°", "Région", "Montant", "Nombre", "Pourcentage (Montant)", 
				"Pourcentage (Nombre)"} ;
		
		for (String label : labels) {
			PdfPCell cell = new PdfPCell(new Phrase(label, new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.BOLD)));
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY) ;
			cell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
			bodyTable.addCell(cell);
		}
		
		for(int index = 0 ; index < listeProjetsRegion.size() ; index ++){
			ProjetRegionDTO regionDTO = listeProjetsRegion.get(index) ;
			
			PdfPCell _numCell = new PdfPCell(new Phrase(Integer.toString(index+1), new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
			_numCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
			bodyTable.addCell(_numCell);
			
			PdfPCell _regionCell = new PdfPCell(new Phrase(regionDTO.getRegion(), new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
			_regionCell.setHorizontalAlignment(Element.ALIGN_LEFT) ;
			bodyTable.addCell(_regionCell);
			
			PdfPCell _montantCell = new PdfPCell(new Phrase(String.valueOf(regionDTO.getMontant()), new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
			_montantCell.setHorizontalAlignment(Element.ALIGN_RIGHT) ;
			bodyTable.addCell(_montantCell);
			
			PdfPCell _nombreCell = new PdfPCell(new Phrase(String.valueOf(regionDTO.getNombre()), new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
			_nombreCell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
			bodyTable.addCell(_nombreCell);
			
			PdfPCell _perc1Cell = new PdfPCell(new Phrase(String.valueOf(regionDTO.getPourcentageMontant()), new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
			_perc1Cell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
			bodyTable.addCell(_perc1Cell);
			
			PdfPCell _perc2Cell = new PdfPCell(new Phrase(String.valueOf(regionDTO.getPourcentageNombre()), new Font(FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL)));
			_perc2Cell.setHorizontalAlignment(Element.ALIGN_CENTER) ;
			bodyTable.addCell(_perc2Cell);
		}
		
		return bodyTable ;
	}
	
	
	private Image genererDiagrammeMontantProjetsTic(List<ProjetRegionDTO> listeProjetsRegion, 
			Integer largeur, Integer hauteur) {
		String fileName = "diagramme_montant_projets_tic.png" ;
		
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
		listeProjetsRegion.forEach(groupe -> {
			dataset.addValue( groupe.getMontant(), String.valueOf(groupe.getRegion()), "Région");  
		}) ;
		
		JFreeChart barChart = ChartFactory.createBarChart("", 
				"Région",  "Montant",             
		         dataset, PlotOrientation.VERTICAL, true, true, true) ;
		
		try {
			ChartUtils.saveChartAsJPEG(new File(fileName), barChart, largeur == 0 ? 600 : largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return Image.getInstance(fileName) ;
		}catch(IOException | BadElementException  ex) {
			log.error(ex.getMessage(), ex);
			return null ;
		}
	}
	
	
	private Image genererDiagrammeNombreProjetsTic(List<ProjetRegionDTO> listeProjetsRegion, 
			Integer largeur, Integer hauteur) {

		String fileName = "diagramme_nombre_projets_tic.png" ;

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
		
		listeProjetsRegion.forEach(groupe -> {
			dataset.addValue(groupe.getNombre(), String.valueOf(groupe.getRegion()), "Région");  
		}) ;
		
		JFreeChart barChart = ChartFactory.createBarChart("", "Région",  "Nombre",             
		         dataset, PlotOrientation.VERTICAL, true, true, true) ;

		try {
			ChartUtils.saveChartAsJPEG(new File(fileName), barChart, largeur == 0 ? 600 : largeur, 
					hauteur == 0 ? 400:hauteur) ;
			return Image.getInstance(fileName) ;
		}catch(IOException | BadElementException  ex) {
			log.error(ex.getMessage(), ex);
			return null ;
		}
	}
	
	
	

}
