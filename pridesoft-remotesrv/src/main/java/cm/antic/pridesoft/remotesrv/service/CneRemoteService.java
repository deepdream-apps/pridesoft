package cm.antic.pridesoft.remotesrv.service;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import cm.antic.pridesoft.datamodel.remote.CneRemote;
import cm.antic.pridesoft.remotesrv.repository.CneRemoteRepository;

@Service
public class CneRemoteService {
	
	private CneRemoteRepository cneRemoteRepository ;
	
	public CneRemoteService(CneRemoteRepository cneRemoteRepository) {
		this.cneRemoteRepository = cneRemoteRepository ;
	}
	
	
	@Cacheable("cneByIDCache")
	public Optional<CneRemote> rechercher (Long id) {
		return cneRemoteRepository.findById(id) ;
	}
	
	@Cacheable("cneByDatesCache")
	public List<CneRemote> rechercher (LocalDate date1, LocalDate date2){
		return cneRemoteRepository.findByDateSignatureBetween(date1, date2) ;
	}
	
}
