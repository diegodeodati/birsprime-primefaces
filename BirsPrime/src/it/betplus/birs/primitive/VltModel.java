package it.betplus.birs.primitive;

public class VltModel {
	
	long AAMS_VLT_MODEL_CODE=-1;
	String NAME="";
	long AAMS_GAME_SYSTEM_CODE=-1;
	String MANUFACTURER = "";
	String CABINET_TYPE = "";
	Double PCT_SUPPLIER = -1d;
	 
	public VltModel(){
				
	}
	
	public VltModel(long aAMS_VLT_MODEL_CODE, String nAME,
			long aAMS_GAME_SYSTEM_CODE, String mANUFACTURER,
			String cABINET_TYPE, Double pCT_SUPPLIER) {
		super();
		AAMS_VLT_MODEL_CODE = aAMS_VLT_MODEL_CODE;
		NAME = nAME;
		AAMS_GAME_SYSTEM_CODE = aAMS_GAME_SYSTEM_CODE;
		MANUFACTURER = mANUFACTURER;
		CABINET_TYPE = cABINET_TYPE;
		PCT_SUPPLIER = pCT_SUPPLIER;
	}


	public long getAAMS_VLT_MODEL_CODE() {
		return AAMS_VLT_MODEL_CODE;
	}


	public void setAAMS_VLT_MODEL_CODE(long aAMS_VLT_MODEL_CODE) {
		AAMS_VLT_MODEL_CODE = aAMS_VLT_MODEL_CODE;
	}


	public String getNAME() {
		return NAME;
	}


	public void setNAME(String nAME) {
		NAME = nAME;
	}


	public long getAAMS_GAME_SYSTEM_CODE() {
		return AAMS_GAME_SYSTEM_CODE;
	}


	public void setAAMS_GAME_SYSTEM_CODE(long aAMS_GAME_SYSTEM_CODE) {
		AAMS_GAME_SYSTEM_CODE = aAMS_GAME_SYSTEM_CODE;
	}


	public String getMANUFACTURER() {
		return MANUFACTURER;
	}


	public void setMANUFACTURER(String mANUFACTURER) {
		MANUFACTURER = mANUFACTURER;
	}


	public String getCABINET_TYPE() {
		return CABINET_TYPE;
	}


	public void setCABINET_TYPE(String cABINET_TYPE) {
		CABINET_TYPE = cABINET_TYPE;
	}


	public Double getPCT_SUPPLIER() {
		return PCT_SUPPLIER;
	}


	public void setPCT_SUPPLIER(Double pCT_SUPPLIER) {
		PCT_SUPPLIER = pCT_SUPPLIER;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ (int) (AAMS_GAME_SYSTEM_CODE ^ (AAMS_GAME_SYSTEM_CODE >>> 32));
		result = prime * result
				+ (int) (AAMS_VLT_MODEL_CODE ^ (AAMS_VLT_MODEL_CODE >>> 32));
		result = prime * result
				+ ((CABINET_TYPE == null) ? 0 : CABINET_TYPE.hashCode());
		result = prime * result
				+ ((MANUFACTURER == null) ? 0 : MANUFACTURER.hashCode());
		result = prime * result + ((NAME == null) ? 0 : NAME.hashCode());
		result = prime * result
				+ ((PCT_SUPPLIER == null) ? 0 : PCT_SUPPLIER.hashCode());
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
		VltModel other = (VltModel) obj;
		if (AAMS_GAME_SYSTEM_CODE != other.AAMS_GAME_SYSTEM_CODE)
			return false;
		if (AAMS_VLT_MODEL_CODE != other.AAMS_VLT_MODEL_CODE)
			return false;
		if (CABINET_TYPE == null) {
			if (other.CABINET_TYPE != null)
				return false;
		} else if (!CABINET_TYPE.equals(other.CABINET_TYPE))
			return false;
		if (MANUFACTURER == null) {
			if (other.MANUFACTURER != null)
				return false;
		} else if (!MANUFACTURER.equals(other.MANUFACTURER))
			return false;
		if (NAME == null) {
			if (other.NAME != null)
				return false;
		} else if (!NAME.equals(other.NAME))
			return false;
		if (PCT_SUPPLIER == null) {
			if (other.PCT_SUPPLIER != null)
				return false;
		} else if (!PCT_SUPPLIER.equals(other.PCT_SUPPLIER))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "VltModel [AAMS_VLT_MODEL_CODE=" + AAMS_VLT_MODEL_CODE
				+ ", NAME=" + NAME + ", AAMS_GAME_SYSTEM_CODE="
				+ AAMS_GAME_SYSTEM_CODE + ", MANUFACTURER=" + MANUFACTURER
				+ ", CABINET_TYPE=" + CABINET_TYPE + ", PCT_SUPPLIER="
				+ PCT_SUPPLIER + "]";
	}
	
	

}
