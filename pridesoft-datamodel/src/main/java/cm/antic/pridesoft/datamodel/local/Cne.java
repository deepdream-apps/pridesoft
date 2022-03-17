package cm.antic.pridesoft.datamodel.local;

import java.io.Serializable;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public class Cne implements Serializable {
	@Id
	private String codeProjet ;

	private String libelle ;

	private LocalDate dateSignature ;

	private BigDecimal montant;
	
	private String idRegion ;

	private String libelleRegion ;
	
	private String idSecteurActivite;

	private String libelleSecteurActivite;
	
	private String idMaitreOuvrage;

	private String libelleMaitreOuvrage;
	
	private String idCategorie ;
	
	private String libelleCategorie ;

	private Integer valide ;
}
