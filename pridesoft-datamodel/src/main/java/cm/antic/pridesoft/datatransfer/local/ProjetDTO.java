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
public class ProjetDTO implements Serializable{
	@Id
	private Long id ;

	private String codeProjet ;

	private String libelle ;

	private LocalDate dateSignature ;

	private LocalDate dateLancement ;
	
	private LocalDate dateAttribution ;

	private BigDecimal montantPrevisionnel ;

	private String codeRegion ;
	
	private String sigleSecteurActivite ;

	private String sigleMaitreOuvrage ;
	
	private Long idCategorie ;
	
	private String libelleCategorie ;

	private Integer valide ;
	
}
