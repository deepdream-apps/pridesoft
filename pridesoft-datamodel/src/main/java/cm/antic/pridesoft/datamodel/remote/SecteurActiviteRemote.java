package cm.antic.pridesoft.datamodel.remote;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "param_secteur_activite")
public class SecteurActiviteRemote implements Serializable{
	@Id
	@Column(name = "pk_param_secteur_activite")
	private Long id ;
	
	@Column(name = "sigle")
	private String sigle ;
	
	@Column (name = "designation")
	private String libelle ;

}
