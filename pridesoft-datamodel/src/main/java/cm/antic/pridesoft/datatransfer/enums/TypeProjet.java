package cm.antic.pridesoft.datatransfer.enums;

public enum TypeProjet {
	BONDECOMMANDE ("BON DE COMMANDE"), MARCHE ("MARCHE");
	
	private String libelle ;
	
	private TypeProjet(String libelle) {
		this.libelle = libelle ;
	}

	public String getLibelle() {
		return libelle;
	}
	
}
