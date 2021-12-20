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
@EqualsAndHashCode(callSuper = false)
@Table(name="param_pays")
public class PaysRemote implements Serializable{
	@Id
	@Column(name="pk_param_pays")
	private Long id;
	
	@Column(name="pays")
	private String pays;
}
