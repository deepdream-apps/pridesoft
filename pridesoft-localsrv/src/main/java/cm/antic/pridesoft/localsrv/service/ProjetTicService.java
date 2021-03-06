package cm.antic.pridesoft.localsrv.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.antic.pridesoft.datamodel.local.MaitreOuvrage;
import cm.antic.pridesoft.datamodel.local.ProjetTic;
import cm.antic.pridesoft.datatransfer.enums.TypeProjet;
import cm.antic.pridesoft.datatransfer.local.NombreMontantDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetAnnuelDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetCategorieDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetMaitreOuvrageDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetRegionDTO;
import cm.antic.pridesoft.datatransfer.local.ProjetSecteurActiviteDTO;
import cm.antic.pridesoft.localsrv.repository.MaitreOuvrageRepository;
import cm.antic.pridesoft.localsrv.repository.ProjetTicRepository;

@Transactional
@Service
public class ProjetTicService {
	private ProjetTicRepository projetTicRepository ;
	
	@Value("${app.pridesoft.region.services_centraux.id}")
	private String idSC ;
	
	public ProjetTicService(ProjetTicRepository projetTicRepository) {
		this.projetTicRepository = projetTicRepository ;
	}
	
	
	public ProjetTic creer (ProjetTic projet)  {
		return projetTicRepository.save(projet) ;
	}
	
	
	public ProjetTic modifier (ProjetTic projet) {
		ProjetTic projetTic = projetTicRepository.findByCodeProjet(projet.getCodeProjet())
				   .orElseThrow(() -> new RuntimeException(String.format("Code projet inexistant %s", projet.getCodeProjet()))) ;
		
		projetTic.setIdRegion(projet.getIdRegion());
		projetTic.setLibelleRegion(projet.getLibelleRegion());
		projetTic.setIdSecteurActivite(projet.getIdSecteurActivite());
		projetTic.setLibelleSecteurActivite(projet.getLibelleSecteurActivite());
		projetTic.setIdCategorie(projet.getIdCategorie());
		projetTic.setLibelleCategorie(projet.getLibelleCategorie());
		projetTic.setIdSousCategorie(projet.getIdSousCategorie());
		projetTic.setLibelleSousCategorie(projet.getLibelleSousCategorie());
		
		return projetTicRepository.save(projetTic) ;
	}
	
	
	public ProjetTic rechercher (String codeProjet)  {
		return projetTicRepository.findByCodeProjet(codeProjet)
								  .map(Function.identity())
								  .orElseThrow() ;
	}
	
	@Cacheable(cacheNames = "groupeProjetsTicAnnee", key = "#dateDebut_#dateFin")
	public List<ProjetAnnuelDTO> collecterProjetAnnee (LocalDate dateDebut, LocalDate dateFin){
		List<ProjetTic> listeProjetsTic = projetTicRepository.findByDateSignatureBetween(dateDebut, dateFin) ;
		
		Double montantTotal = listeProjetsTic.stream().mapToDouble(p -> p.getMontant().doubleValue()).sum() ;
		
		return listeProjetsTic.stream()
		                .collect(groupingBy(p -> p.getDateSignature().getYear(), toList()))
		                .entrySet()
		                .stream()
		                .map(tuple -> {
		                	Integer annee = tuple.getKey() ;
		                	List<ProjetTic> projetsAnnuels = tuple.getValue() ;
		                	Integer nombre = projetsAnnuels.size() ;
		                	Long montant = projetsAnnuels.stream().mapToLong(p -> p.getMontant().longValue()).sum() ;
		                	return new ProjetAnnuelDTO(annee, nombre, montant, nombre*100.0f/listeProjetsTic.size(), Double.valueOf(montant*100/montantTotal).floatValue()) ;
		              }).collect(toList()) ;
							   
	}
	
	
	@Cacheable(cacheNames = "groupeProjetsTicRegion", key = "#dateDebut_#dateFin_#inclureSC")
	public List<ProjetRegionDTO> collecterProjetRegion (LocalDate dateDebut, LocalDate dateFin, Boolean inclureSC){
		List<ProjetTic> listeProjetsTic = projetTicRepository.findByDateSignatureBetween(dateDebut, dateFin) ;
		
		Double montantTotal = listeProjetsTic.stream().mapToDouble(p -> p.getMontant().doubleValue()).sum() ;
		
		return listeProjetsTic.stream()
					    .filter(projetTic -> (inclureSC || ! idSC.equals(projetTic.getIdRegion()) ) && projetTic.getLibelleRegion() != null)
		                .collect(groupingBy(p -> p.getLibelleRegion(), toList()))
		                .entrySet()
		                .stream()
		                .map(tuple -> {
		                	String region = tuple.getKey() ;
		                	List<ProjetTic> projetsAnnuels = tuple.getValue() ;
		                	Integer nombre = projetsAnnuels.size() ;
		                	Long montant = projetsAnnuels.stream().mapToLong(p -> p.getMontant().longValue()).sum() ;
		                	return new ProjetRegionDTO(region, 
		                			nombre,
		                			montant,
		                			nombre*100.0f/listeProjetsTic.size(),
		                			Double.valueOf(montant*100/montantTotal).floatValue()) ;
		              }).sorted((g1, g2) ->{
		            	  return g2.getMontant().compareTo(g1.getMontant()) ;
		              }).collect(toList()) ;
							   
	}
	
	
	@Cacheable(cacheNames = "groupeProjetsTicCategorie", key = "#dateDebut_#dateFin")
	public List<ProjetCategorieDTO> collecterProjetCategorie (LocalDate dateDebut, LocalDate dateFin){
		List<ProjetTic> listeProjetsTic = projetTicRepository.findByDateSignatureBetween(dateDebut, dateFin) ;
		
		Double montantTotal = listeProjetsTic.stream().mapToDouble(p -> p.getMontant().doubleValue()).sum() ;
		
		return listeProjetsTic.stream()
		                .collect(groupingBy(p -> p.getLibelleCategorie() == null ? "Aucune Cat??gorie" : p.getLibelleCategorie(), toList()))
		                .entrySet()
		                .stream()
		                .map(tuple -> {
		                	String categorie = tuple.getKey() ;
		                	List<ProjetTic> projetsCategoriels = tuple.getValue() ;
		                	Integer nombre = projetsCategoriels.size() ;
		                	Long montant = projetsCategoriels.stream().mapToLong(p -> p.getMontant().longValue()).sum() ;
		                	return new ProjetCategorieDTO(categorie, 
		                			nombre,
		                			montant,
		                			nombre*100.0f/listeProjetsTic.size(),
		                			Double.valueOf(montant*100/montantTotal).floatValue()) ;
		              }).sorted((g1, g2) ->{
		            	  return g2.getMontant().compareTo(g1.getMontant()) ;
		              }).collect(toList()) ;
							   
	}
	
	
	@Cacheable(cacheNames = "groupeProjetsTicSecteurActivite", key = "#dateDebut_#dateFin")
	public List<ProjetSecteurActiviteDTO> collecterProjetSecteurActivite (LocalDate dateDebut, LocalDate dateFin){
		List<ProjetTic> listeProjetsTic = projetTicRepository.findByDateSignatureBetween(dateDebut, dateFin) ;
		
		Double montantTotal = listeProjetsTic.stream().mapToDouble(p -> p.getMontant().doubleValue()).sum() ;
		
		return listeProjetsTic.stream()
		                .collect(groupingBy(p -> p.getLibelleSecteurActivite() == null ? "Aucun secteur d'activit??" : p.getLibelleSecteurActivite(), toList()))
		                .entrySet()
		                .stream()
		                .map(tuple -> {
		                	String secteurActivite = tuple.getKey() ;
		                	List<ProjetTic> projetsSecteursActivite = tuple.getValue() ;
		                	Integer nombre = projetsSecteursActivite.size() ;
		                	Long montant = projetsSecteursActivite.stream().mapToLong(p -> p.getMontant().longValue()).sum() ;
		                	return new ProjetSecteurActiviteDTO(secteurActivite, 
		                			nombre,
		                			montant,
		                			nombre*100.0f/listeProjetsTic.size(),
		                			Double.valueOf(montant*100/montantTotal).floatValue()) ;
		              }).sorted((g1, g2) ->{
		            	  return g2.getMontant().compareTo(g1.getMontant()) ;
		              }).collect(toList()) ;
							   
	}
	
	
	@Cacheable(cacheNames = "groupeProjetsTicMaitreOuvrage", key = "#dateDebut_#dateFin")
	public List<ProjetMaitreOuvrageDTO> collecterProjetMaitreOuvrage (LocalDate dateDebut, LocalDate dateFin){
		List<ProjetTic> listeProjetsTic = projetTicRepository.findByDateSignatureBetween(dateDebut, dateFin) ;
		
		Double montantTotal = listeProjetsTic.stream().mapToDouble(p -> p.getMontant().doubleValue()).sum() ;
		
		return listeProjetsTic.stream()
		                .collect(groupingBy(p -> p.getLibelleMaitreOuvrage() == null ? "Aucun ma??tre d'ouvrage" : p.getLibelleMaitreOuvrage(), toList()))
		                .entrySet()
		                .stream()
		                .map(tuple -> {
		                	String maitreOuvrage = tuple.getKey() ;
		                	List<ProjetTic> projetsSecteursActivite = tuple.getValue() ;
		                	Integer nombre = projetsSecteursActivite.size() ;
		                	Long montant = projetsSecteursActivite.stream().mapToLong(p -> p.getMontant().longValue()).sum() ;
		                	return new ProjetMaitreOuvrageDTO(maitreOuvrage, 
		                			nombre,
		                			montant,
		                			nombre*100.0f/listeProjetsTic.size(),
		                			Double.valueOf(montant*100/montantTotal).floatValue()) ;
		              }).sorted((g1, g2) ->{
		            	  return g2.getMontant().compareTo(g1.getMontant()) ;
		              }).limit(5).collect(toList()) ;
							   
	}
	
	
	@Cacheable(cacheNames = "projetsTic", key = "#dateDebut_#dateFin")
	public List<ProjetTic> rechercher (LocalDate dateDebut, LocalDate dateFin) {
		return projetTicRepository.findByDateSignatureBetween(dateDebut, dateFin) ;
	}
	
	
	public List<NombreMontantDTO> getGlobalStats() {
		List<NombreMontantDTO> liste = new ArrayList<>() ;
		
		List<ProjetTic> listeProjetsTic = projetTicRepository.findAll() ;
		Long montantProjetsTic = listeProjetsTic.stream()
				.map(ProjetTic::getMontant)
				.mapToLong(BigDecimal::longValue)
				.sum() ;
		
		List<ProjetTic> listeProjetsTicSC = projetTicRepository.findByIdRegion(idSC) ;
		Long montantProjetsTicSC = listeProjetsTicSC.stream()
				.map(ProjetTic::getMontant)
				.mapToLong(BigDecimal::longValue)
				.sum() ;
		
		
		List<ProjetTic> listeMarches = listeProjetsTic.stream()
				.filter(projetTic -> TypeProjet.MARCHE.getLibelle().equals(projetTic.getType()))
				.collect(Collectors.toList());
		
		Long montantTotalMaches = listeMarches.stream()
					.map(ProjetTic::getMontant)
					.mapToLong(BigDecimal::longValue)
					.sum() ;
		
		List<ProjetTic> listeCNEs = listeProjetsTic.stream()
				.filter(projetTic -> TypeProjet.BONDECOMMANDE.getLibelle().equals(projetTic.getType()))
				.collect(Collectors.toList()) ;
		
		Long montantTotalCNEs = listeCNEs.stream()
					.map(ProjetTic::getMontant)
					.mapToLong(BigDecimal::longValue)
					.sum() ;
		
		Integer nombreTotal = listeProjetsTic.size() == 0 ? 1 : listeProjetsTic.size() ;
		Long montantTotal = montantProjetsTic == 0L ? 1L : montantProjetsTic ;
		
		liste.add( new NombreMontantDTO("Nombre et Montant des tous les projets TIC au niveau national", 
				listeProjetsTic.size(), montantProjetsTic, 
				Double.valueOf(listeProjetsTic.size()*1.0/nombreTotal).floatValue(), 
				Double.valueOf(montantProjetsTic*1.0/montantTotal).floatValue()))  ;
		
		liste.add( new NombreMontantDTO("Nombre et Montant des projets TIC dans l'Administration centrale", 
				listeProjetsTicSC.size(), montantProjetsTicSC, 
				Double.valueOf(listeProjetsTicSC.size()*1.0/nombreTotal).floatValue(), 
				Double.valueOf(montantProjetsTicSC*1.0/montantTotal).floatValue()))  ;
		
		liste.add( new NombreMontantDTO("Nombre et Montant des march??s TIC au niveau national", 
				listeMarches.size(), montantTotalMaches, 
				Double.valueOf(listeMarches.size()*1.0/nombreTotal).floatValue(), 
				Double.valueOf(montantTotalMaches*1.0/montantTotal).floatValue()))  ;
		
		liste.add( new NombreMontantDTO("Nombre et Montant des bons de commande TIC au niveau national", 
				listeCNEs.size(), montantTotalCNEs, 
				Double.valueOf(listeCNEs.size()*1.0/nombreTotal).floatValue(), 
				Double.valueOf(montantTotalCNEs*1.0/montantTotal).floatValue()))  ;
		
		return liste ;
	}
	
	
}
