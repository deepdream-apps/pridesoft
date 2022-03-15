package cm.antic.pridesoft.datamodel.remote;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
	private String libelle ;
	
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
		
}
