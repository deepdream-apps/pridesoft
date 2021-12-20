package cm.antic.pridesoft.datamodel.remote;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="param_region")
public class RegionRemote implements Serializable{
	@Id
	@Column(name="pk_param_region")
	private Long id;
	
	@Column(name="region")
	private String libelle ;
	
	@Column(name="code_region")
	private String code;
	
	@ManyToOne
	@JoinColumn(name="fk_param_pays")
	private PaysRemote pays ;

}
