package cm.antic.pridesoft.datamodel.local;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Document
public class SecteurActivite implements Serializable{
	@Id
	private Long id ;

	private String sigle ;
	
	private String libelle ;

}
