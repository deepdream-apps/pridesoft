package cm.antic.pridesoft.datamodel.local;
import java.io.Serializable;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document
public class SecteurActivite implements Serializable{
	@Id
	private String id ;

	private String libelle ;
	
	private String description ;

}
