package cm.antic.pridesoft.datatransfer.local;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateDebutFinSCDTO {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateDebut ;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateFin ;
	
	private Boolean inclureSC ;
}
