package it.betplus.birs.primitive;

import java.util.List;


public class Location implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	

	private String AAMS_LOCATION_ID = "";	
	private String CADASTRAL_CODE = "";
	private String COMMERCIAL_NAME = "";
	private String ADDRESS = "";
	private String STREET_NUMBER = "";
	private String POSTAL_CODE = "";
	private java.sql.Timestamp CESSATION_DATE;
	private java.sql.Timestamp PRELIMINARE_DATE;
	private java.sql.Timestamp CREATION_DATE;
	private java.sql.Date OPENING_DATE;
	private double FLOOR_SURFACE = 0;
	private int ID_LOCATION_TYPE;
	private String TOPONIMO = "";
	private String CAP = "";
	private double FLOOR_VLT_SURFACE = 0;
	private String TELEPHONE = "xxx";

	public Location(String aAMS_LOCATION_ID, String cADASTRAL_CODE,
			String cOMMERCIAL_NAME, String aDDRESS, String sTREET_NUMBER,
			String pOSTAL_CODE, java.sql.Timestamp cESSATION_DATE,
			java.sql.Timestamp pRELIMINARE_DATE,
			java.sql.Timestamp cREATION_DATE, double fLOOR_SURFACE,
			int iD_LOCATION_TYPE, String tOPONIMO, String cAP,
			double fLOOR_VLT_SURFACE, String tELEPHONE,
			java.sql.Date oPENING_DATE) {
		super();
		AAMS_LOCATION_ID = aAMS_LOCATION_ID;
		CADASTRAL_CODE = cADASTRAL_CODE;
		COMMERCIAL_NAME = cOMMERCIAL_NAME;
		ADDRESS = aDDRESS;
		STREET_NUMBER = sTREET_NUMBER;
		POSTAL_CODE = pOSTAL_CODE;
		CESSATION_DATE = cESSATION_DATE;
		PRELIMINARE_DATE = pRELIMINARE_DATE;
		CREATION_DATE = cREATION_DATE;
		FLOOR_SURFACE = fLOOR_SURFACE;
		ID_LOCATION_TYPE = iD_LOCATION_TYPE;
		TOPONIMO = tOPONIMO;
		CAP = cAP;
		FLOOR_VLT_SURFACE = fLOOR_VLT_SURFACE;
		TELEPHONE = tELEPHONE;
		OPENING_DATE = oPENING_DATE;
	}

	public Location() {
		AAMS_LOCATION_ID="NO-LOC-ID";
	}

	public String getAAMS_LOCATION_ID() {
		return AAMS_LOCATION_ID.toUpperCase();
	}

	public void setAAMS_LOCATION_ID(String aAMS_LOCATION_ID) {
		AAMS_LOCATION_ID = aAMS_LOCATION_ID;
	}

	public String getCADASTRAL_CODE() {
		return CADASTRAL_CODE.toUpperCase();
	}

	public void setCADASTRAL_CODE(String cADASTRAL_CODE) {
		CADASTRAL_CODE = cADASTRAL_CODE;
	}

	public String getCOMMERCIAL_NAME() {
		return COMMERCIAL_NAME.toUpperCase();
	}

	public void setCOMMERCIAL_NAME(String cOMMERCIAL_NAME) {
		COMMERCIAL_NAME = cOMMERCIAL_NAME.toUpperCase().replaceAll("'","");
	}

	public String getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public String getSTREET_NUMBER() {
		return STREET_NUMBER;
	}

	public void setSTREET_NUMBER(String sTREET_NUMBER) {
		STREET_NUMBER = sTREET_NUMBER;
	}

	public String getPOSTAL_CODE() {
		return POSTAL_CODE;
	}

	public void setPOSTAL_CODE(String pOSTAL_CODE) {
		POSTAL_CODE = pOSTAL_CODE;
	}

	public java.sql.Timestamp getCESSATION_DATE() {
		return CESSATION_DATE;
	}

	public void setCESSATION_DATE(java.sql.Timestamp cESSATION_DATE) {
		CESSATION_DATE = cESSATION_DATE;
	}

	public java.sql.Timestamp getPRELIMINARE_DATE() {
		return PRELIMINARE_DATE;
	}

	public void setPRELIMINARE_DATE(java.sql.Timestamp pRELIMINARE_DATE) {
		PRELIMINARE_DATE = pRELIMINARE_DATE;
	}

	public java.sql.Timestamp getCREATION_DATE() {
		return CREATION_DATE;
	}

	public void setCREATION_DATE(java.sql.Timestamp cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}

	public double getFLOOR_SURFACE() {
		return FLOOR_SURFACE;
	}

	public void setFLOOR_SURFACE(double fLOOR_SURFACE) {
		FLOOR_SURFACE = fLOOR_SURFACE;
	}

	public int getID_LOCATION_TYPE() {
		return ID_LOCATION_TYPE;
	}

	public void setID_LOCATION_TYPE(int iD_LOCATION_TYPE) {
		ID_LOCATION_TYPE = iD_LOCATION_TYPE;
	}

	public String getTOPONIMO() {
		return TOPONIMO;
	}

	public void setTOPONIMO(String tOPONIMO) {
		TOPONIMO = tOPONIMO;
	}

	public String getCAP() {
		return CAP;
	}

	public void setCAP(String cAP) {
		CAP = cAP;
	}

	public double getFLOOR_VLT_SURFACE() {
		return FLOOR_VLT_SURFACE;
	}

	public void setFLOOR_VLT_SURFACE(double fLOOR_VLT_SURFACE) {
		FLOOR_VLT_SURFACE = fLOOR_VLT_SURFACE;
	}

	public String getTELEPHONE() {
		return TELEPHONE;
	}

	public void setTELEPHONE(String tELEPHONE) {
		TELEPHONE = tELEPHONE;
	}

	public java.sql.Date getOPENING_DATE() {
		return OPENING_DATE;
	}

	public void setOPENING_DATE(java.sql.Date oPENING_DATE) {
		OPENING_DATE = oPENING_DATE;
	}

	
	@Override
	public boolean equals(Object obj) {
		Location other = (Location) obj;
		if (AAMS_LOCATION_ID == null) {
			if (other.AAMS_LOCATION_ID != null)
				return false;
		} else if (!AAMS_LOCATION_ID.equals(other.AAMS_LOCATION_ID))
			return false;
		
		return true;
	}
/*
	@Override
	public String toString() {
		return "Location [AAMS_LOCATION_ID=" + AAMS_LOCATION_ID
				+ ", CADASTRAL_CODE=" + CADASTRAL_CODE + ", COMMERCIAL_NAME="
				+ COMMERCIAL_NAME + ", ADDRESS=" + ADDRESS + ", STREET_NUMBER="
				+ STREET_NUMBER + ", POSTAL_CODE=" + POSTAL_CODE
				+ ", CESSATION_DATE=" + CESSATION_DATE + ", PRELIMINARE_DATE="
				+ PRELIMINARE_DATE + ", CREATION_DATE=" + CREATION_DATE
				+ ", OPENING_DATE=" + OPENING_DATE + ", FLOOR_SURFACE="
				+ FLOOR_SURFACE + ", ID_LOCATION_TYPE=" + ID_LOCATION_TYPE
				+ ", TOPONIMO=" + TOPONIMO + ", CAP=" + CAP
				+ ", FLOOR_VLT_SURFACE=" + FLOOR_VLT_SURFACE + ", TELEPHONE="
				+ TELEPHONE + "]";
	}*/
	
	public String toString(){
		return AAMS_LOCATION_ID+" - "+COMMERCIAL_NAME;
	}

	public static String toParamenterLocationID(List<Location> avaliableLoc){
		String stringsLocationId = "(";	
		int i = 0;
		for (Location l : avaliableLoc){
			stringsLocationId+= "'"+l.getAAMS_LOCATION_ID()+"'";
			
			if(i<avaliableLoc.size()-1){
				stringsLocationId+=",";	
			}
				
			i++;
		}
		
		return stringsLocationId+")";
	}


}
