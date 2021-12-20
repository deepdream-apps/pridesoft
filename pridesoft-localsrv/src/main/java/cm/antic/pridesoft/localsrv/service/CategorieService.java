package cm.antic.pridesoft.localsrv.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
	
	
	public Optional<Categorie> rechercher (Long id) {
		return categorieRepository.findById(id) ;
	}
	
	
	public List<Categorie> rechercherTout (){
		Iterable<Categorie> categories = categorieRepository.findAll() ;
		List<Categorie> listeCategories = new ArrayList<>() ;
		categories.forEach(listeCategories::add);
		return listeCategories ;
	}
	
	
	public Categorie creer (Categorie categorie) {
		return categorieRepository.save(categorie) ;
	}
	
	
	public Categorie modifier (Categorie categorie) {
		return categorieRepository.save(categorie) ;
	}
	
	
	public void supprimer (Categorie categorie) {
		categorieRepository.delete(categorie) ;
	}
	
}
