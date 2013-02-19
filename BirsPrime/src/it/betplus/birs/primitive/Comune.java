package it.betplus.birs.primitive;

public class Comune implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	
	
	private Long idComune;
	private Provincia provincia;
	private String nome;
	private String codIstat;
	private String idCom;
	private String cadastrialCode;


	public Comune() {
	}
	

	public Comune(Long idComune, Provincia provincia, String nome,
			String cadastrialCode) {
		this.idComune = idComune;
		this.provincia = provincia;
		this.nome = nome;
		this.cadastrialCode = cadastrialCode;
	}

	public Comune(Long idComune, Provincia provincia, String nome,
			String codIstat, String idCom, String cadastrialCode) {
		this.idComune = idComune;
		this.provincia = provincia;
		this.nome = nome;
		this.codIstat = codIstat;
		this.idCom = idCom;
		this.cadastrialCode = cadastrialCode;
	}

	public Long getIdComune() {
		return this.idComune;
	}

	public void setIdComune(Long idComune) {
		this.idComune = idComune;
	}

	public Provincia getProvincia() {
		return this.provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodIstat() {
		return this.codIstat;
	}

	public void setCodIstat(String codIstat) {
		this.codIstat = codIstat;
	}

	public String getIdCom() {
		return this.idCom;
	}

	public void setIdCom(String idCom) {
		this.idCom = idCom;
	}

	public String getCadastrialCode() {
		return this.cadastrialCode;
	}

	public void setCadastrialCode(String cadastrialCode) {
		this.cadastrialCode = cadastrialCode;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cadastrialCode == null) ? 0 : cadastrialCode.hashCode());
		result = prime * result
				+ ((codIstat == null) ? 0 : codIstat.hashCode());
		result = prime * result + ((idCom == null) ? 0 : idCom.hashCode());
		result = prime * result
				+ ((idComune == null) ? 0 : idComune.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((provincia == null) ? 0 : provincia.hashCode());
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
		Comune other = (Comune) obj;
		if (cadastrialCode == null) {
			if (other.cadastrialCode != null)
				return false;
		} else if (!cadastrialCode.equals(other.cadastrialCode))
			return false;
		if (codIstat == null) {
			if (other.codIstat != null)
				return false;
		} else if (!codIstat.equals(other.codIstat))
			return false;
		if (idCom == null) {
			if (other.idCom != null)
				return false;
		} else if (!idCom.equals(other.idCom))
			return false;
		if (idComune == null) {
			if (other.idComune != null)
				return false;
		} else if (!idComune.equals(other.idComune))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (provincia == null) {
			if (other.provincia != null)
				return false;
		} else if (!provincia.equals(other.provincia))
			return false;
		return true;
	}

}
