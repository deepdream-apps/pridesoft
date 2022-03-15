package cm.antic.pridesoft.datatransfer.local;

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
public class ProjetRegionDTO {
	private String region ;
	private Integer nombre ;
	private Long montant ;
	private Float pourcentageNombre ;
	private Float pourcentageMontant ;

}
