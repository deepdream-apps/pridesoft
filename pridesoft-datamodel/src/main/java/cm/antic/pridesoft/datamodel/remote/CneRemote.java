package cm.antic.pridesoft.datamodel.remote;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name="vue_cne")
public class CneRemote implements Serializable {
	
	@Id
	@Column(name = "pk_cne")
	private Long id ;
	
	@Column(name="numero_cne")
	private String numero;
	
	@Column(name="montant_cne")
	private Long montant;
	
	@Column(name="ville_delivrance_cne")
	private String villeDelivrance;
	
	@Column(name="objet_commande")
	private String objetCommande;
	
	@Column(name="date_signature_cne")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateSignature ;
	
	@Column(name="reference_commande_public")
	private String referenceCommandePublic;
	
	@Column(name="date_signature_commande")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateSignatureCommande ;
	
	@Column(name="nom_ordonnateur_commande")
	private String nomOrdonnateurCommande;
	
	@Column(name="montant_ttc_commande")
	private BigDecimal montantTtcCommande;
	
	@Column(name="agence_etablissement_bancaire")
	private String agenceEtablissementBancaire;
	
	@Column(name="numero_recu_versement")
	private String numeroRecuVersement;
	
	@Column(name="numero_quittance_payement")
	private String numeroQuittancePayement;
	
	//@Column(name="region_execution")
	//private Long regionExecution;
	
	@Column(name="observation")
	private String observation;
	
	@Column(name="date_quittance_paiement")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateQuittancePaiement ;
	
	@Column(name="nom_signataire_quittance")
	private String nomSignataireQuittance;
	
	@Column(name="nom_signataire_cne")
	private String nomSignataireCne;
	
	@Column(name="date_versement")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateVersement;
	
	@Column(name="date_fin_validite")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateFinValidite;
	
	@Column(name="numero_demande")
	private String numeroDemande;
	
	@Column(name="imputation_commande")
	private Long imputationCommande;
	
	@Column(name="numero_operateur")
	private String numeroOperateur;
	
	@Column(name="mode_delivrance")
	private String modeDelivrance;
	
	@Column(name="decharge")
	private String decharge;
	
	@Column(name="anteriorite")
	private String anteriorite;
	
	@Column(name="date_decharge")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateDecharge;

	@Column(name="raison_sociale")
	private String raisonSociale;
	
	@Column(name="boite_postale")
	private String boitePostale;
	
	@Column(name="numero_contribuable")
	private String numeroContribuable;
	
	@Column(name="registre_commerce")
	private String registreCommerce;
	
	
//	@ManyToOne()
//    @JoinColumn(name="fk_ac")
//	private MaitreOuvrage ac;
	
    @Column(name="fk_param_secteur_activite")
	private Long secteurActivite;

	
    @Column(name="fk_param_sous_secteur_activite")
	private Long sousSecteurActivite;
	
    @Column(name="fk_entrepriseid")
	private Long entreprise;
	
    @Column(name="fk_mocne")
	private Long maitreOuvrage;
	
    @Column(name="fk_param_type_procedure")
	private Long typeProcedure;
	
    @Column(name="fk_param_region")
	private Long region;
}
