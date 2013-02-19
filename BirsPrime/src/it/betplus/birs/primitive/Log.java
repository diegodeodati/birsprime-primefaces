package it.betplus.birs.primitive;


import it.betplus.web.framework.utils.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Log implements Serializable{
	
	
	private static final long serialVersionUID = 7888553701319787957L;

	
	Date dataStart = null;
	Date dataEnd = null;
	Date operation_dateStart = null;
	Date operation_dateEnd = null;
	int exit_code = 0;	
	String aams_game_system_code = "";
	
	
	public Log(){				
	}



	public Date getDataEnd() {
		return dataEnd;
	}



	public void setDataEnd(Date dataEnd) {
		this.dataEnd = dataEnd;
	}



	public Date getDataStart() {
		return dataStart;
	}



	public void setDataStart(Date dataStart) {
		this.dataStart = dataStart;
	}



	public Date getOperation_dateStart() {
		return operation_dateStart;
	}



	public void setOperation_dateStart(Date operation_dateStart) {
		this.operation_dateStart = operation_dateStart;
	}



	public Date getOperation_dateEnd() {
		return operation_dateEnd;
	}



	public void setOperation_dateEnd(Date operation_dateEnd) {
		this.operation_dateEnd = operation_dateEnd;
	}



	public int getExit_code() {
		return exit_code;
	}



	public void setExit_code(int exit_code) {
		this.exit_code = exit_code;
	}



	public String getAams_game_system_code() {
		return aams_game_system_code;
	}



	public void setAams_game_system_code(String aams_game_syste_code) {
		this.aams_game_system_code = aams_game_syste_code;
	}



	public void autoSet_aams_game_system_code(String c){
		if(c.equals("1711000045"))
			aams_game_system_code = "Novomatic";
		else if (c.equals("1711000065"))
			aams_game_system_code = "Inspired";
		else 
			aams_game_system_code = "Sconosciuto";
	}
	
	public boolean isNovomatic(){
		return this.getAams_game_system_code().equals("Novomatic");
	}

	public boolean isTimeForReimport(){
		int sDay = DateUtils.day(this.getDataStart());
    	int sHour = DateUtils.hour(this.getDataStart());
    	int sMinute = DateUtils.minute(this.getDataStart());
    	int sSecond = DateUtils.second(this.getDataStart());
   	
    	return sHour==00 && sMinute==00 && sSecond==1;
    }


	@Override
	public String toString() {
		return "Log [dataEnd=" + dataEnd + ", dataStart=" + dataStart
				+ ", operation_dateStart=" + operation_dateStart
				+ ", operation_dateEnd=" + operation_dateEnd + ", exit_code="
				+ exit_code + ", aams_game_system_code="
				+ aams_game_system_code + "]";
	}


	public static ArrayList<String> headerToArrayList() {
		ArrayList<String> hList = new ArrayList<String>();

		hList.add("Data Inizio");
		hList.add("Data Fine");
		hList.add("Data Inizio Operazione");
		hList.add("Data Fine Operazione");
		hList.add("Exit Code");
		hList.add("Sistema Gioco");
		return hList;

	}

	

}
