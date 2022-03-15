package cm.antic.pridesoft.localsrv.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cm.antic.pridesoft.datamodel.local.Categorie;
import cm.antic.pridesoft.localsrv.repository.CategorieRepository;

@Transactional
@Service
public class CategorieService {
	private CategorieRepository categorieRepository ;
	
	public CategorieService(CategorieRepository categorieRepository) {
		this.categorieRepository = categorieRepository ;
	}
	
	
	public Optional<Categorie> rechercher (String id) {
		return categorieRepository.findById(id) ;
	}
	
	
	public List<Categorie> rechercherTout (){
		Iterable<Categorie> categories = categorieRepository.findAll() ;
		List<Categorie> listeCategories = new ArrayList<>() ;
		categories.forEach(listeCategories::add);
		return listeCategories ;
	}
	
	
	public Categorie creer (Categorie categorie) {
		categorie.setId(UUID.randomUUID().toString());
		return categorieRepository.save(categorie) ;
	}
	
	
	public Categorie modifier (Categorie categorieExistante) {
		Categorie categorie = categorieRepository.findById(categorieExistante.getId())
												 .orElseThrow(IllegalArgumentException::new) ;
		categorie.setDescription(categorieExistante.getDescription());
		categorie.setLibelle(categorieExistante.getLibelle()) ;
		return categorieRepository.save(categorie) ;
	}
	
	
	public void supprimer (Categorie categorie) {
		categorieRepository.delete(categorie) ;
	}
	
}
