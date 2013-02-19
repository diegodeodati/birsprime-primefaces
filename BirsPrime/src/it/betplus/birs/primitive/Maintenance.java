package it.betplus.birs.primitive;

import java.sql.Date;

public class Maintenance {
	
	long ID_MAINTENANCE;
	
	String AAMS_VLT_ID;
	Date MAINTENANCE_DATE_IN ;
	Date MAINTENANCE_DATE_OUT ;
	
	public long getID_MAINTENANCE() {
		return ID_MAINTENANCE;
	}
	public void setID_MAINTENANCE(long iD_MAINTENANCE) {
		ID_MAINTENANCE = iD_MAINTENANCE;
	}
	public String getAAMS_VLT_ID() {
		return AAMS_VLT_ID.toUpperCase();
	}
	public void setAAMS_VLT_ID(String aAMS_VLT_ID) {
		AAMS_VLT_ID = aAMS_VLT_ID;
	}
	public Date getMAINTENANCE_DATE_IN() {
		return MAINTENANCE_DATE_IN;
	}
	public void setMAINTENANCE_DATE_IN(Date mAINTENANCE_DATE_IN) {
		MAINTENANCE_DATE_IN = mAINTENANCE_DATE_IN;
	}
	public Date getMAINTENANCE_DATE_OUT() {
		return MAINTENANCE_DATE_OUT;
	}
	public void setMAINTENANCE_DATE_OUT(Date mAINTENANCE_DATE_OUT) {
		MAINTENANCE_DATE_OUT = mAINTENANCE_DATE_OUT;
	}

	
	@Override
	public String toString() {
		return "maintenance [ID_MAINTENANCE=" + ID_MAINTENANCE
				+ ", AAMS_VLT_ID=" + AAMS_VLT_ID + ", MAINTENANCE_DATE_IN="
				+ MAINTENANCE_DATE_IN + ", MAINTENANCE_DATE_OUT="
				+ MAINTENANCE_DATE_OUT + "]";
	}
}
