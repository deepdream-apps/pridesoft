package cm.antic.pridesoft.datamodel.local;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode (callSuper = false)
@Document
public class ProjetTic implements Serializable{
	@Id
	private String codeProjet ;
	
	private String libelle ;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateSignature ;
	
	private BigDecimal montant ;
	
	private String idCategorie ;
	
	private String libelleCategorie ;
	
	private String idRegion ;
	
	private String libelleRegion ;
	
	private String idMaitreOuvrage ;
	
	private String libelleMaitreOuvrage ;
	
	private String idSecteurActivite ;
	
	private String libelleSecteurActivite ;
	
	private String type ;

}
