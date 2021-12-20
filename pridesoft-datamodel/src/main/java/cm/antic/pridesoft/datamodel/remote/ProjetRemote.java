package cm.antic.pridesoft.datamodel.remote;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name ="r_projet")
@Data
@EqualsAndHashCode (callSuper = false)
public class ProjetRemote implements Serializable{
	@Id
	@Column(name = "code_projet")
	private String codeProjet ;
	
	@Column (name = "numero_ordre")
	private Integer numeroOrdre ;
	
	@ManyToOne
	@JoinColumn(name = "fk_maitre_ouvrage")
	private MaitreOuvrageRemote maitreOuvrage ;
	
	@Column(name = "designation")
	private String designation ;
	
	@Column(name = "montant_previsionnel")
	private BigDecimal montant ;
	
	@Column(name = "date_programmee_demarrage_travaux")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateDemarrageTravaux ;
	
	@Column(name = "date_programmee_reception_travaux")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateReceptionTravaux ;
	
	@Column(name = "date_programmee_lancement")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateLancement ;
	
	@Column(name = "date_programmee_attribution_marche")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateAttribution ;

	@Column(name = "date_programmee_signature_marche")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateSignature ; 

	@ManyToOne
	@JoinColumn(name = "fk_param_region")
	private RegionRemote region ;
	

}
