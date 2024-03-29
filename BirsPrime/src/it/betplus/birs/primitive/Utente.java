package it.betplus.birs.primitive;


// Generated 31-mar-2011 10.51.22 by Hibernate Tools 3.4.0.CR1


/**
 * Utenti generated by hbm2java
 */
public class Utente implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	private String username;
	private String userPass;
	private String nome;
	private String cognome;	
	private String ruolo;
	private String matcherPreferences;
	private String email;
	private int emailPreferences;

	public Utente() {
	}

	public Utente(String username) {
		this.username = username;
	}

	public Utente(String username, String userPass, String nome,
			String cognome, String ruolo,String matcherPreferences,String email,int emailPreferences) {
		this.username = username;
		this.userPass = userPass;
		this.nome = nome;
		this.cognome = cognome;
		this.ruolo = ruolo;
		this.matcherPreferences =matcherPreferences;
		this.email = email;
		this.setEmailPreferences(emailPreferences);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserPass() {
		return this.userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getRuolo() {
		return this.ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	
	public String getMatcherPreferences(){
		return matcherPreferences;
	}
	

	public void setMatcherPreferences(String matcherPreferences) {
		this.matcherPreferences = matcherPreferences;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		if(email==null || email.isEmpty())
			return "";
		else
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the emailPreferences
	 */
	public int getEmailPreferences() {
		return emailPreferences;
	}

	/**
	 * @param emailPreferences the emailPreferences to set
	 */
	public void setEmailPreferences(int emailPreferences) {
		this.emailPreferences = emailPreferences;
	}

	@Override
	public String toString() {
		return "Utente [username=" + username + ", userPass=" + userPass
				+ ", nome=" + nome + ", cognome=" + cognome + ", ruolo="
				+ ruolo + ", matcherPreferences=" + matcherPreferences
				+ ", email=" + email + "]";
	}




}
