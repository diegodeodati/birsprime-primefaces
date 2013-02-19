package it.betplus.birs.primitive;

import java.sql.Date;

public class Gestore implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String cod_gestore;
	private String denominazione="";
	private String piva="";
	private String telefono="";
	private String fax="";
	private String email="";
	private java.sql.Date data_stipula;
	private int toponimo = 0;
	private String indirizzo;
	private String numero_civico;
	private String cap;
	private String comune="";
	private String cf="";
	
	
	public Gestore(){}
	
	public Gestore(String cod_gestore, String denominazione, String piva,
			String telefono, String fax, String email, Date data_stipula,
			int toponimo, String indirizzo, String numero_civico, String cap,
			String comune, String cf) {
		super();
		this.cod_gestore = cod_gestore;
		this.denominazione = denominazione;
		this.piva = piva;
		this.telefono = telefono;
		this.fax = fax;
		this.email = email;
		this.data_stipula = data_stipula;
		this.toponimo = toponimo;
		this.indirizzo = indirizzo;
		this.numero_civico = numero_civico;
		this.cap = cap;
		this.comune = comune;
		this.cf = cf;
	}
	
	public String getCod_gestore() {
		return cod_gestore;
	}
	public void setCod_gestore(String cod_gestore) {
		this.cod_gestore = cod_gestore;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		if(denominazione!=null)
		this.denominazione = denominazione;
	}
	public String getPiva() {
		return piva;
	}
	public void setPiva(String piva) {
		if(piva!=null)
		this.piva = piva;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		if(fax!=null)
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		if(email!=null)
		this.email = email;
	}
	public java.sql.Date getData_stipula() {
		return data_stipula;
	}
	public void setData_stipula(java.sql.Date data_stipula) {
		this.data_stipula = data_stipula;
	}
	public int getToponimo() {
		return toponimo;
	}
	public void setToponimo(int toponimo) {
		this.toponimo = toponimo;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getNumero_civico() {
		return numero_civico;
	}
	public void setNumero_civico(String numero_civico) {
		this.numero_civico = numero_civico;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		if(cap!=null)
		this.cap = cap;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		if(comune!=null)
		this.comune = comune;
	}
	public String getCf() {
		return cf;
	}
	public void setCf(String cf) {
		if(cf!=null)
		this.cf = cf;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gestore other = (Gestore) obj;
		if (cap == null) {
			if (other.cap != null)
				return false;
		} else if (!cap.equals(other.cap))
			return false;
		if (cf == null) {
			if (other.cf != null)
				return false;
		} else if (!cf.equals(other.cf))
			return false;
		if (cod_gestore == null) {
			if (other.cod_gestore != null)
				return false;
		} else if (!cod_gestore.equals(other.cod_gestore))
			return false;
		if (comune == null) {
			if (other.comune != null)
				return false;
		} else if (!comune.equals(other.comune))
			return false;
		if (data_stipula == null) {
			if (other.data_stipula != null)
				return false;
		} else if (!data_stipula.equals(other.data_stipula))
			return false;
		if (denominazione == null) {
			if (other.denominazione != null)
				return false;
		} else if (!denominazione.equals(other.denominazione))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (indirizzo == null) {
			if (other.indirizzo != null)
				return false;
		} else if (!indirizzo.equals(other.indirizzo))
			return false;
		if (numero_civico == null) {
			if (other.numero_civico != null)
				return false;
		} else if (!numero_civico.equals(other.numero_civico))
			return false;
		if (piva == null) {
			if (other.piva != null)
				return false;
		} else if (!piva.equals(other.piva))
			return false;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		if (toponimo != other.toponimo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "gestore [cod_gestore=" + cod_gestore + ", denominazione="
				+ denominazione + ", piva=" + piva + ", telefono=" + telefono
				+ ", fax=" + fax + ", email=" + email + ", data_stipula="
				+ data_stipula + ", toponimo=" + toponimo + ", indirizzo="
				+ indirizzo + ", numero_civico=" + numero_civico + ", cap="
				+ cap + ", comune=" + comune + ", cf=" + cf + "]";
	}


}
