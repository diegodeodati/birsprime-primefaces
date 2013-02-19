package it.betplus.birs.primitive;

public class Esercente {
	
	
	
	private int cod_esercente;
	private String denominazione="";
	private String piva="";
	private String telefono="";
	private String fax="";
	private String email="";
	private int toponimo = 0;
	private String indirizzo="";
	private String numero_civico="";
	private String cap="";
	private String comune="";
	private String cadastral_code="";
	private String company_cf="";
	private String person_name="";
	private String person_surname="";
	private String person_cf="";
	
	public Esercente(){}
	
	
	public Esercente(int cod_esercente, String denominazione, String piva,
			String telefono, String fax, String email, int toponimo,
			String indirizzo, String numero_civico, String cap, String comune,
			String cadastral_code, String company_cf, String person_name,
			String person_surname, String person_cf) {
		super();
		this.cod_esercente = cod_esercente;
		this.denominazione = denominazione;
		this.piva = piva;
		this.telefono = telefono;
		this.fax = fax;
		this.email = email;
		this.toponimo = toponimo;
		this.indirizzo = indirizzo;
		this.numero_civico = numero_civico;
		this.cap = cap;
		this.comune = comune;
		this.cadastral_code = cadastral_code;
		this.company_cf = company_cf;
		this.person_name = person_name;
		this.person_surname = person_surname;
		this.person_cf = person_cf;
	}
	
	
	
	public int getCod_esercente() {
		return cod_esercente;
	}
	public void setCod_esercente(int cod_esercente) {
		this.cod_esercente = cod_esercente;
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
		if(telefono!=null)
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
		if(indirizzo!=null)
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
	public String getCadastral_code() {
		return cadastral_code;
	}
	public void setCadastral_code(String cadastral_code) {
		if(cadastral_code!=null)
		this.cadastral_code = cadastral_code;
	}
	public String getCompany_cf() {
		return company_cf;
	}
	public void setCompany_cf(String company_cf) {
		if(company_cf!=null)
		this.company_cf = company_cf;
	}
	public String getPerson_name() {
		return person_name;
	}
	public void setPerson_name(String person_name) {
		if(person_name!=null)
		this.person_name = person_name;
	}
	public String getPerson_surname() {
		return person_surname;
	}
	public void setPerson_surname(String person_surname) {
		if(person_surname!=null)
		this.person_surname = person_surname;
	}
	public String getPerson_cf() {
		return person_cf;
	}
	public void setPerson_cf(String person_cf) {
		if(person_cf!=null)
		this.person_cf = person_cf;
	}


	@Override
	public String toString() {
		return "esercente [cod_esercente=" + cod_esercente + ", denominazione="
				+ denominazione + ", piva=" + piva + ", telefono=" + telefono
				+ ", fax=" + fax + ", email=" + email + ", toponimo="
				+ toponimo + ", indirizzo=" + indirizzo + ", numero_civico="
				+ numero_civico + ", cap=" + cap + ", comune=" + comune
				+ ", cadastral_code=" + cadastral_code + ", company_cf="
				+ company_cf + ", person_name=" + person_name
				+ ", person_surname=" + person_surname + ", person_cf="
				+ person_cf + "]";
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Esercente other = (Esercente) obj;
		if (cadastral_code == null) {
			if (other.cadastral_code != null)
				return false;
		} else if (!cadastral_code.equals(other.cadastral_code))
			return false;
		if (cap == null) {
			if (other.cap != null)
				return false;
		} else if (!cap.equals(other.cap))
			return false;
		if (company_cf == null) {
			if (other.company_cf != null)
				return false;
		} else if (!company_cf.equals(other.company_cf))
			return false;
		if (comune == null) {
			if (other.comune != null)
				return false;
		} else if (!comune.equals(other.comune))
			return false;
		if (denominazione == null) {
			if (other.denominazione != null)
				return false;
		} else if (!denominazione.toUpperCase().equals(other.denominazione.toUpperCase()))
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
		if (person_cf == null) {
			if (other.person_cf != null)
				return false;
		} else if (!person_cf.equals(other.person_cf))
			return false;
		if (person_name == null) {
			if (other.person_name != null)
				return false;
		} else if (!person_name.equals(other.person_name))
			return false;
		if (person_surname == null) {
			if (other.person_surname != null)
				return false;
		} else if (!person_surname.equals(other.person_surname))
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



	


	
	
	
	

}
