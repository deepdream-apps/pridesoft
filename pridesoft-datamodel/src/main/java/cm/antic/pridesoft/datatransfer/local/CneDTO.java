package cm.antic.pridesoft.datatransfer.local;

import java.io.Serializable;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CneDTO implements Serializable {
	@Id
	private Long id ;

	private String numero;

	private BigDecimal montant;

	private String objetCommande;

	private LocalDate dateSignatureCommande ;

	private BigDecimal montantTtcCommande;

	private String codeRegion ;

	private String sigleSecteurActivite;

	private String sigleMaitreOuvrage;
	
	private Long idCategorie ;
	
	private String libelleCategorie ;

	private Integer valide ;
}
