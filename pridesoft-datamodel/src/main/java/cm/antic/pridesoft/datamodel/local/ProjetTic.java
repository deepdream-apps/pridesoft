package cm.antic.pridesoft.datamodel.local;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode (callSuper = false)
@Document
public class ProjetTic implements Serializable{
	@Id
	private Long id ;
	
	private String codeProjet ;
	
	private String libelle ;
	
	private LocalDate dateSignature ;
	
	private BigDecimal montant ;
	
	private Long idCategorie ;
	
	private Long idRegion ;
	
	private Long idMaitreOuvrage ;
	
	private Long idSecteurActivite ;
	
	private String type ;

}
