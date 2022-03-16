package cm.antic.pridesoft.datamodel.local;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document
public class Utilisateur implements Serializable{
	@Id
	private String id ;

	private String login ;

	private String motDePasse ;

	private String profil ;
	
	private LocalDateTime dateCreation ;

	private LocalDateTime dateExpiration ;

	private LocalDateTime dateExpirationMdp ;
	
}
