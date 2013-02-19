package it.betplus.birs.primitive;

public class Contratto {

	
	
	//select id,manager_id,merchant_id,pct_esercente,pct_gestore from sc_location
	
	private String AAMS_LOCATION_ID;
	private String COD_GESTORE;
	private int COD_ESERCENTE;
	private double PCT_GESTORE;
	private double PCT_ESERCENTE;
	private double PCT_CONCESSIONARIO;
	
	
	public Contratto(String aAMS_LOCATION_ID, String cOD_GESTORE,
			int cOD_ESERCENTE, double pCT_GESTORE, double pCT_ESERCENTE,
			double pCT_CONCESSIONARIO) {
		super();
		AAMS_LOCATION_ID = aAMS_LOCATION_ID;
		COD_GESTORE = cOD_GESTORE;
		COD_ESERCENTE = cOD_ESERCENTE;
		PCT_GESTORE = pCT_GESTORE;
		PCT_ESERCENTE = pCT_ESERCENTE;
		PCT_CONCESSIONARIO = pCT_CONCESSIONARIO;
	}


	public String getAAMS_LOCATION_ID() {
		return AAMS_LOCATION_ID;
	}


	public void setAAMS_LOCATION_ID(String aAMS_LOCATION_ID) {
		AAMS_LOCATION_ID = aAMS_LOCATION_ID;
	}


	public String getCOD_GESTORE() {
		return COD_GESTORE;
	}


	public void setCOD_GESTORE(String cOD_GESTORE) {
		COD_GESTORE = cOD_GESTORE;
	}


	public int getCOD_ESERCENTE() {
		return COD_ESERCENTE;
	}


	public void setCOD_ESERCENTE(int cOD_ESERCENTE) {
		COD_ESERCENTE = cOD_ESERCENTE;
	}


	public double getPCT_GESTORE() {
		return PCT_GESTORE;
	}


	public void setPCT_GESTORE(double pCT_GESTORE) {
		PCT_GESTORE = pCT_GESTORE;
	}


	public double getPCT_ESERCENTE() {
		return PCT_ESERCENTE;
	}


	public void setPCT_ESERCENTE(double pCT_ESERCENTE) {
		PCT_ESERCENTE = pCT_ESERCENTE;
	}


	public double getPCT_CONCESSIONARIO() {
		return PCT_CONCESSIONARIO;
	}


	public void setPCT_CONCESSIONARIO(int pCT_CONCESSIONARIO) {
		PCT_CONCESSIONARIO = pCT_CONCESSIONARIO;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contratto other = (Contratto) obj;
		if (Double.doubleToLongBits(PCT_CONCESSIONARIO) != Double
				.doubleToLongBits(other.PCT_CONCESSIONARIO))
			return false;
		if (Double.doubleToLongBits(PCT_ESERCENTE) != Double
				.doubleToLongBits(other.PCT_ESERCENTE))
			return false;
		if (Double.doubleToLongBits(PCT_GESTORE) != Double
				.doubleToLongBits(other.PCT_GESTORE))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "contratto [AAMS_LOCATION_ID=" + AAMS_LOCATION_ID
				+ ", COD_GESTORE=" + COD_GESTORE + ", COD_ESERCENTE="
				+ COD_ESERCENTE + ", PCT_GESTORE=" + PCT_GESTORE
				+ ", PCT_ESERCENTE=" + PCT_ESERCENTE + ", PCT_CONCESSIONARIO="
				+ PCT_CONCESSIONARIO + "]";
	}
	
	
	
	
}
