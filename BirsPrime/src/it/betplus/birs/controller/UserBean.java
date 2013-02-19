package it.betplus.birs.controller;


import it.betplus.birs.primitive.Utente;
import it.betplus.web.framework.managedbeans.GeneralBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "userBean")
@RequestScoped
public class UserBean extends GeneralBean implements Serializable{
	

	private static final long serialVersionUID = 4026676606179217327L;
	private String username;
	private String userPass;
	private String nome;
	private String cognome;	
	private String ruolo;
	private String matcherPreferences;
	private String email;
	private int emailPreferences;

	public UserBean(){}
	
	public UserBean(Utente user) {
		this.username = user.getUsername();
		this.userPass = user.getUserPass();
		this.nome = user.getNome();
		this.cognome = user.getCognome();
		this.ruolo = user.getRuolo();
		this.matcherPreferences = user.getMatcherPreferences();
		this.email = user.getEmail();
		this.emailPreferences = user.getEmailPreferences();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	
	
	public String getMatcherPreferences() {
		return matcherPreferences;
	}
	
	public String matcherPreferencesToString(){
		String pref = "";
		char c[] = matcherPreferences.toCharArray();
		
		for(int i=0;i<c.length;i++){
			pref=pref+c[i];
		}
		
		
		return pref;
	}
	
	
	public void setMatcherPreferences(List<String> matcherPreferences) {
		String pref = "";
		
		for(String m:matcherPreferences)
			pref=pref+m;
		
		this.matcherPreferences = pref;
	}
	
	public List<String> matcherPreferencesToListString() {
		List<String> prefList = new ArrayList<String>();
		
		char c[] = matcherPreferences.toCharArray();
		
		for(int i=0;i<c.length;i++){
			prefList.add(c[i]+"");
		}
		
		return prefList;
	}

	
	

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getEmailPreferences() {
		return emailPreferences;
	}

	/**
	 * @param emailPreferences the emailPreferences to set
	 */
	public void setEmailPreferences(int emailPreferences) {
		this.emailPreferences = emailPreferences;
	}

	public boolean isAdmin(){
		return ruolo.equals("admin");
	}
	
	public boolean isCoge(){
		return ruolo.equals("coge") || isAdmin();		
	}
	
	public boolean isPresidio(){
		return ruolo.equals("presidio") || isAdmin() || isCoge();		
	}
		
	
	public boolean isGuest(){
		return ruolo.equals("guest") || isCoge() || isAdmin() || isPresidio();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime
				* result
				+ ((matcherPreferences == null) ? 0 : matcherPreferences
						.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((ruolo == null) ? 0 : ruolo.hashCode());
		result = prime * result
				+ ((userPass == null) ? 0 : userPass.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserBean other = (UserBean) obj;
		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (matcherPreferences == null) {
			if (other.matcherPreferences != null)
				return false;
		} else if (!matcherPreferences.equals(other.matcherPreferences))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (ruolo == null) {
			if (other.ruolo != null)
				return false;
		} else if (!ruolo.equals(other.ruolo))
			return false;
		if (userPass == null) {
			if (other.userPass != null)
				return false;
		} else if (!userPass.equals(other.userPass))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserBean [username=" + username + ", userPass=" + userPass
				+ ", nome=" + nome + ", cognome=" + cognome + ", ruolo="
				+ ruolo + ", matcherPreferences=" + matcherPreferences
				+ ", email=" + email + "]";
	}


	

}
