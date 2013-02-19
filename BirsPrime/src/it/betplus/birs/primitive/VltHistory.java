package it.betplus.birs.primitive;

import java.sql.Timestamp;

public class VltHistory {

	
	long id;
	String AAMS_VLT_ID;
	String LOCATION_ID;
	Timestamp MOVE_DATE;
	
	public VltHistory(long id, String aAMS_VLT_ID, String lOCATION_ID,
			Timestamp mOVE_DATE) {
		super();
		this.id = id;
		AAMS_VLT_ID = aAMS_VLT_ID;
		LOCATION_ID = lOCATION_ID;
		MOVE_DATE = mOVE_DATE;
	}
	
	public VltHistory(){}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAAMS_VLT_ID() {
		return AAMS_VLT_ID;
	}
	public void setAAMS_VLT_ID(String aAMS_VLT_ID) {
		AAMS_VLT_ID = aAMS_VLT_ID;
	}
	public String getLOCATION_ID() {
		return LOCATION_ID;
	}
	public void setLOCATION_ID(String lOCATION_ID) {
		LOCATION_ID = lOCATION_ID;
	}
	public Timestamp getMOVE_DATE() {
		return MOVE_DATE;
	}
	public void setMOVE_DATE(Timestamp mOVE_DATE) {
		MOVE_DATE = mOVE_DATE;
	}
	@Override
	public String toString() {
		return "vltHistory [id=" + id + ", AAMS_VLT_ID=" + AAMS_VLT_ID
				+ ", LOCATION_ID=" + LOCATION_ID + ", MOVE_DATE=" + MOVE_DATE
				+ "]";
	}
	
}
