package it.betplus.birs.primitive;

import java.io.Serializable;
import java.util.Date;


public class ProbMillionVlt implements Serializable{


	private static final long serialVersionUID = -7117267981298688324L;
	
	private String AAMS_VLT_ID;
	private String GS_VLT_ID;
	private String LOCATION_NAME;
	private Date DATA;
	private String AAMS_LOCATION_ID;
	private Double MAX_BET_VLT;
	private Double MEDIA_BET_VLT;
	private Double TOTAL_BET_SALA;
	private int NUM_VLT;
	private int PERC_MAX_BET;
	private Double MEDIA_7GG;
	private Double MEDIA_30GG;
	private String COLOR_PROB;
	
	
	
	public ProbMillionVlt() {
	}



	public String getAAMS_VLT_ID() {
		return AAMS_VLT_ID;
	}



	public void setAAMS_VLT_ID(String aAMS_VLT_ID) {
		AAMS_VLT_ID = aAMS_VLT_ID;
	}



	public String getGS_VLT_ID() {
		return GS_VLT_ID;
	}



	public void setGS_VLT_ID(String gS_VLT_ID) {
		GS_VLT_ID = gS_VLT_ID;
	}



	public String getLOCATION_NAME() {
		return LOCATION_NAME;
	}



	public void setLOCATION_NAME(String lOCATION_NAME) {
		LOCATION_NAME = lOCATION_NAME;
	}



	public Date getDATA() {
		return DATA;
	}



	public void setDATA(Date dATA) {
		DATA = dATA;
	}



	public String getAAMS_LOCATION_ID() {
		return AAMS_LOCATION_ID;
	}



	public void setAAMS_LOCATION_ID(String aAMS_LOCATION_ID) {
		AAMS_LOCATION_ID = aAMS_LOCATION_ID;
	}



	public Double getMAX_BET_VLT() {
		return MAX_BET_VLT;
	}



	public void setMAX_BET_VLT(Double mAX_BET_VLT) {
		MAX_BET_VLT = mAX_BET_VLT;
	}



	public Double getMEDIA_BET_VLT() {
		return MEDIA_BET_VLT;
	}



	public void setMEDIA_BET_VLT(Double mEDIA_BET_VLT) {
		MEDIA_BET_VLT = mEDIA_BET_VLT;
	}



	public Double getTOTAL_BET_SALA() {
		return TOTAL_BET_SALA;
	}



	public void setTOTAL_BET_SALA(Double tOTAL_BET_SALA) {
		TOTAL_BET_SALA = tOTAL_BET_SALA;
	}



	public int getNUM_VLT() {
		return NUM_VLT;
	}



	public void setNUM_VLT(int nUM_VLT) {
		NUM_VLT = nUM_VLT;
	}



	public int getPERC_MAX_BET() {
		return PERC_MAX_BET;
	}



	public void setPERC_MAX_BET(int pERC_MAX_BET) {
		PERC_MAX_BET = pERC_MAX_BET;
		
		if(pERC_MAX_BET>60)
			setCOLOR_PROB("MAGENTA");
		else if (pERC_MAX_BET>50)
			setCOLOR_PROB("RED");
		else if (pERC_MAX_BET>40)
			setCOLOR_PROB("ORANGE");
		else if (pERC_MAX_BET>30)
			setCOLOR_PROB("YELLOW");
		else 
			setCOLOR_PROB("GREEN");
	}



	public Double getMEDIA_7GG() {
		return MEDIA_7GG;
	}



	public void setMEDIA_7GG(Double mEDIA_7GG) {
		MEDIA_7GG = mEDIA_7GG;
	}



	public Double getMEDIA_30GG() {
		return MEDIA_30GG;
	}



	public void setMEDIA_30GG(Double mEDIA_30GG) {
		MEDIA_30GG = mEDIA_30GG;
	}



	public String getCOLOR_PROB() {
		return COLOR_PROB;
	}



	public void setCOLOR_PROB(String cOLOR_PROB) {
		COLOR_PROB = cOLOR_PROB;
	}
	

	
}
