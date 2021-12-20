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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode (callSuper = false)
@Table(name = "mo_ac")
public class MaitreOuvrageRemote implements Serializable{
	@Id
	@Column (name = "pk_mo_ac")
	private Long id ;
	
	@Column (name = "sigle")
	private String sigle ;
	
	@Column (name = "designation")
	private String designation ;
	
	@Column (name = "boite_postale")
	private String boitePostale ;
	
	@Column (name = "telephone")
	private String telephone ;
	
	@Column (name = "fax")
	private String fax ;
	
	@Column (name = "reference_acte_creation")
	private String referenceActeCreation ;
	
	@Column (name = "email")
	private String email ;
	
	@Column (name = "latitude")
	private String latitude ;
	
	@Column (name = "longitude")
	private String longitude ;
	
	@Column (name = "adresse")
	private String adresse ;
	
	@Column (name = "site_web")
	private String siteWeb ;
	
	@Column (name = "date_insertion")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateInsertion ;
	
	@Column (name = "date_ajout")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateAjout ;
	
	@Column (name = "ajout_par")
	private String ajoutPar ;
	
	@Column (name = "resume_attributions")
	private String resumeAttributions ;

	@Column(name="fk_param_type_mo_ac")
	private Long typeMoAc;
	
	/*fk_param_type_mo_ac	int4	32	0
	fichier_reference_acte_creation	varchar	255	0
	fk_param_pays	int4	32	0
	fk_param_region	int4	32	0
	fk_param_departement	int4	32	0
	fk_param_arrondissement	int4	32	0
	categorie_type_mo	int4	32	0
	 */

	

}
