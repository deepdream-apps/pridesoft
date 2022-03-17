package cm.antic.pridesoft.datamodel.local;
import java.io.Serializable;


import org.springframework.data.annotation.Id;
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
public class MaitreOuvrage implements Serializable{
	@Id
	private String id ;

	private String sigle ;

	private String libelle ;
	
	private String idSecteurActivite ;
	
	private String libelleSecteurActivite ;
	
	private String idRegion ;
	
	private String libelleRegion ;

	private String boitePostale ;

	private String telephone ;

	private String email ;

	private String adresse ;
	
	private String siteWeb ;

}
