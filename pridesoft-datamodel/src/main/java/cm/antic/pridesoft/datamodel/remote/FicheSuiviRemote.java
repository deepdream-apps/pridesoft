package cm.antic.pridesoft.datamodel.remote;
import java.io.Serializable;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "fiche_de_suivi_sir_pridesoft")
public class FicheSuiviRemote implements Serializable{
	@Id
	@Column(name = "code_projet")
	private String codeProjet ;
	
	@Column(name = "maitre_ouvrage")
	private String maitreOuvrage ;
	
	@Column(name = "designation")
	private String designation ;
	
	@Column(name = "nature_prestation")
	private String naturePrestation ;

	@Column(name = "montant_previsionnel_marche")
	private Double montantPrevisionnel ;
	
	@Column(name = "date_programmee_lancement_appel_offre")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateProgrammee ;
	
	@Column(name = "date_signature_marche")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateSignature ;
	
	
	@Column(name = "date_programmee_attribution_marche")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateAttribution ;
	
	@Column(name = "date_programmee_demarrage_travaux")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateDemarrageTravaux ;
	
	@Column(name = "date_programmee_reception_travaux")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateReceptionTravaux ;
	
	@Column(name = "region")
	private String region ;
	
	@Column(name = "departement")
	private String departement ;
	
	@Column(name = "arrondissement_commune")
	private String arrondissement ;
	
	@Column(name = "autorite_contractante")
	private String autoriteContractante ;
	
	@Column(name = "reference_marche")
	private String referenceMarche ;
	
	@Column(name = "attributaire")
	private String attributaire ;
	
	@Column(name = "ingenieur_marche")
	private String ingenieurMarche ;
	
	@Column(name = "chef_service_marche")
	private String chefServiceMarche ;
	
}
