package it.betplus.birs.primitive;

// Generated 31-mar-2011 10.51.22 by Hibernate Tools 3.4.0.CR1

/**
 * Ruoli generated by hbm2java
 */
public class Ruolo implements java.io.Serializable {

	private static final long serialVersionUID = -4010012860980624667L;
	
	private String ruolo;	
	private String utente;

	public Ruolo() {
	}
	
	public Ruolo(String ruolo, String utente) {
		super();
		this.ruolo = ruolo;
		this.utente = utente;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}


	

}