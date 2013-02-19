package it.betplus.birs.primitive;

public class Clientedim implements java.io.Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Esercente esercente;
	private Gestore gestore;

	public Clientedim() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Esercente getEsercente() {
		return esercente;
	}

	public void setEsercente(Esercente esercente) {
		this.esercente = esercente;
	}

	public Gestore getGestore() {
		return gestore;
	}

	public void setGestore(Gestore gestore) {
		this.gestore = gestore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((esercente == null) ? 0 : esercente.hashCode());
		result = prime * result + ((gestore == null) ? 0 : gestore.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Clientedim other = (Clientedim) obj;
		if (esercente == null) {
			if (other.esercente != null)
				return false;
		} else if (!esercente.equals(other.esercente))
			return false;
		if (gestore == null) {
			if (other.gestore != null)
				return false;
		} else if (!gestore.equals(other.gestore))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Clientidim [id=" + id + ", esercente=" + esercente
				+ ", gestore=" + gestore + "]";
	}


	

}