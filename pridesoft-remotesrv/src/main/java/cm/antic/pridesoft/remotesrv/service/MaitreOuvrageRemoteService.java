package cm.antic.pridesoft.remotesrv.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cm.antic.pridesoft.datamodel.remote.MaitreOuvrageRemote;
import cm.antic.pridesoft.remotesrv.repository.MaitreOuvrageRemoteRepository;

@Service
public class MaitreOuvrageRemoteService {

	private MaitreOuvrageRemoteRepository maitreOuvrageRemoteRepository ;
	
	public MaitreOuvrageRemoteService(MaitreOuvrageRemoteRepository maitreOuvrageRemoteRepository) {
		this.maitreOuvrageRemoteRepository = maitreOuvrageRemoteRepository ;
	}
	
	public Optional<MaitreOuvrageRemote> rechercher (Long id) {
		return maitreOuvrageRemoteRepository.findById(id) ;
	}
	
	
	public List<MaitreOuvrageRemote> rechercherTout (){
		Iterable<MaitreOuvrageRemote> maitreOuvrages = maitreOuvrageRemoteRepository.findAll() ;
		List<MaitreOuvrageRemote> listeMaitreOuvrages = new ArrayList<MaitreOuvrageRemote>() ;
		maitreOuvrages.forEach(listeMaitreOuvrages::add);
		return listeMaitreOuvrages ;
	}
	
	
	public Optional<MaitreOuvrageRemote> rechercherSigle (String sigle) {
		return maitreOuvrageRemoteRepository.findBySigle(sigle);
	}
	
	
	public Optional<MaitreOuvrageRemote> rechercherDesignation (String designation) {
		return maitreOuvrageRemoteRepository.findByDesignation(designation);
	}
	
}
