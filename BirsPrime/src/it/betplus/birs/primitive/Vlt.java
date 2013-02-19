package it.betplus.birs.primitive;

import java.sql.Timestamp;

public class Vlt implements java.io.Serializable{

	private static final long serialVersionUID = 1143084879611655888L;
	String AAMS_VLT_ID;
	String GS_VLT_ID;
	String CIV;
	int VLT_MODEL_ID;
	String LOCATION_ID;
	long GS_ID;
	int CONNECTION_TYPE;
	String MAC_ADDRESS;
	Timestamp CREATION_DATE;
	Timestamp CESSATION_DATE;
	Timestamp VARIATION_DATE;
	
	
	
	public Vlt(){
		AAMS_VLT_ID = "NO-VLT-ID";
		GS_VLT_ID = "";
		CIV = "";
		VLT_MODEL_ID = -1;
		LOCATION_ID = "";
		GS_ID = -1;
		MAC_ADDRESS = "";	
		CONNECTION_TYPE = 0; //custom lan
	}
	
	public String getAAMS_VLT_ID() {
		return AAMS_VLT_ID.toUpperCase();
	}


	public void setAAMS_VLT_ID(String aAMS_VLT_ID) {
		AAMS_VLT_ID = aAMS_VLT_ID;
	}


	public String getGS_VLT_ID() {
		return GS_VLT_ID.toUpperCase();
	}


	public void setGS_VLT_ID(String gS_VLT_CODE) {
		GS_VLT_ID = gS_VLT_CODE;
	}


	public String getCIV() {
		return CIV;
	}


	public void setCIV(String cIV) {
		CIV = cIV;
	}




	public int getVLT_MODEL_ID() {
		return VLT_MODEL_ID;
	}

	public void setVLT_MODEL_ID(int vLT_MODEL_ID) {
		VLT_MODEL_ID = vLT_MODEL_ID;
	}

	public String getLOCATION_ID() {
		return LOCATION_ID.toUpperCase();
	}

	public void setLOCATION_ID(String lOCATION_ID) {
		LOCATION_ID = lOCATION_ID;
	}

	public long getGS_ID() {
		return GS_ID;
	}

	public void setGS_ID(long gS_ID) {
		GS_ID = gS_ID;
	}

	public int getCONNECTION_TYPE() {
		return CONNECTION_TYPE;
	}

	public void setCONNECTION_TYPE(int cONNECTION_TYPE) {
		CONNECTION_TYPE = cONNECTION_TYPE;
	}

	public String getMAC_ADDRESS() {
		return MAC_ADDRESS.toUpperCase();
	}

	public void setMAC_ADDRESS(String mAC_ADDRESS) {
		MAC_ADDRESS = mAC_ADDRESS;
	}

	public Timestamp getCREATION_DATE() {
		return CREATION_DATE;
	}

	public void setCREATION_DATE(Timestamp cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}

	public Timestamp getCESSATION_DATE() {
		return CESSATION_DATE;
	}

	public void setCESSATION_DATE(Timestamp cESSATION_DATE) {
		CESSATION_DATE = cESSATION_DATE;
	}

	public Timestamp getVARIATION_DATE() {
		return VARIATION_DATE;
	}

	public void setVARIATION_DATE(Timestamp vARIATION_DATE) {
		VARIATION_DATE = vARIATION_DATE;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((AAMS_VLT_ID == null) ? 0 : AAMS_VLT_ID.hashCode());
		result = prime * result
				+ ((CESSATION_DATE == null) ? 0 : CESSATION_DATE.hashCode());
		result = prime * result + ((CIV == null) ? 0 : CIV.hashCode());
		result = prime * result + CONNECTION_TYPE;
		result = prime * result
				+ ((CREATION_DATE == null) ? 0 : CREATION_DATE.hashCode());
		result = prime * result + (int) (GS_ID ^ (GS_ID >>> 32));
		result = prime * result
				+ ((GS_VLT_ID == null) ? 0 : GS_VLT_ID.hashCode());
		result = prime * result + VLT_MODEL_ID;
		result = prime * result
				+ ((LOCATION_ID == null) ? 0 : LOCATION_ID.hashCode());
		result = prime * result
				+ ((MAC_ADDRESS == null) ? 0 : MAC_ADDRESS.hashCode());
		result = prime * result
				+ ((VARIATION_DATE == null) ? 0 : VARIATION_DATE.hashCode());
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
		Vlt other = (Vlt) obj;
		if (AAMS_VLT_ID == null) {
			if (other.AAMS_VLT_ID != null)
				return false;
		} else if (!AAMS_VLT_ID.equals(other.AAMS_VLT_ID))
			return false;
		if (CESSATION_DATE == null) {
			if (other.CESSATION_DATE != null)
				return false;
		} else if (!CESSATION_DATE.equals(other.CESSATION_DATE))
			return false;
		if (CIV == null) {
			if (other.CIV != null)
				return false;
		} else if (!CIV.equals(other.CIV))
			return false;
		if (CONNECTION_TYPE != other.CONNECTION_TYPE)
			return false;
		if (CREATION_DATE == null) {
			if (other.CREATION_DATE != null)
				return false;
		} else if (!CREATION_DATE.equals(other.CREATION_DATE))
			return false;
		if (GS_ID != other.GS_ID)
			return false;
		if (GS_VLT_ID == null) {
			if (other.GS_VLT_ID != null)
				return false;
		} else if (!GS_VLT_ID.equals(other.GS_VLT_ID))
			return false;
		if (VLT_MODEL_ID != other.VLT_MODEL_ID)
			return false;
		if (LOCATION_ID == null) {
			if (other.LOCATION_ID != null)
				return false;
		} else if (!LOCATION_ID.equals(other.LOCATION_ID))
			return false;
		if (MAC_ADDRESS == null) {
			if (other.MAC_ADDRESS != null)
				return false;
		} else if (!MAC_ADDRESS.equals(other.MAC_ADDRESS))
			return false;
		if (VARIATION_DATE == null) {
			if (other.VARIATION_DATE != null)
				return false;
		} else if (!VARIATION_DATE.equals(other.VARIATION_DATE))
			return false;
		return true;
	}
/*
	@Override
	public String toString() {
		return "vlt [AAMS_VLT_ID=" + AAMS_VLT_ID + ", GS_VLT_ID="
				+ GS_VLT_ID + ", CIV=" + CIV + ", ID_VLT_MODEL="
				+ ID_VLT_MODEL + ", LOCATION_ID=" + LOCATION_ID + ", GS_ID="
				+ GS_ID + ", CONNECTION_TYPE=" + CONNECTION_TYPE
				+ ", MAC_ADDRESS=" + MAC_ADDRESS + ", CREATION_DATE="
				+ CREATION_DATE + ", CESSATION_DATE=" + CESSATION_DATE
				+ ", VARIATION_DATE=" + VARIATION_DATE + "]";
	}*/

	public String toString(){
		return AAMS_VLT_ID;
	}
}
