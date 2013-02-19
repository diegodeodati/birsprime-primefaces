package it.betplus.birs.controller.sheets;

import it.betplus.birs.db.dao.LogDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "infoPageBean")
@ViewScoped
public class InfoPageBean implements Serializable{

	private static final long serialVersionUID = 3699742125162411737L;
	ArrayList<String> dettagli_filtri = new ArrayList<String>();
	String page_info = "";

	Log logI;
	Log logN;

	public InfoPageBean(ArrayList<String> dettagli_filtri, String page_info) {
		super();
		this.dettagli_filtri = dettagli_filtri;
		this.page_info = page_info;
	}

	public InfoPageBean() {
		try {
			List<Log> listLog = new ArrayList<Log>();
			listLog = LogDAO.getInstance().retrieve_log();

			for (Log l : listLog) {
				if (l.isNovomatic())
					logN = l;
				else if (!l.isNovomatic())
					logI = l;
			}
			
			this.dettagli_filtri.add("SINGOLI GIORNI");
			this.dettagli_filtri.add("TUTTE LOCATION");
			
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	public String getDettagli_filtri() {
		String aux = "";

		for (String s : dettagli_filtri) {
			aux = aux + s + " - ";
		}

		return aux.substring(0, aux.length() - 2);
	}

	public void changeDettagliFiltri(boolean isModSingTotDay,
			boolean isModAllOpenLoc) {

		dettagli_filtri.clear();

		if (!isModSingTotDay) {
			dettagli_filtri.add("SINGOLI GIORNI ");
		} else {
			dettagli_filtri.add("TOTALI GIORNI ");
		}

		if (!isModAllOpenLoc) {
			dettagli_filtri.add("TUTTE LOCATION ");
		} else {
			dettagli_filtri.add("SOLO LOCATION APERTE ");
		}
	}

	public void changeDettagliFiltriGiorni(boolean isModSingTotDay) {
		if(dettagli_filtri.size()!=0 && dettagli_filtri!=null){

		String aux = dettagli_filtri.get(1);
		dettagli_filtri.clear();

		if (!isModSingTotDay) {
			dettagli_filtri.add("SINGOLI GIORNI ");
		} else {
			dettagli_filtri.add("TOTALI GIORNI ");
		}

		dettagli_filtri.add(aux);
		}
	}

	public void changeDettagliFiltriOpen(boolean isModAllOpenLoc) {
		if(dettagli_filtri.size()!=0 && dettagli_filtri!=null){

		String aux = dettagli_filtri.get(0);
		dettagli_filtri.clear();

		dettagli_filtri.add(aux);

		if (!isModAllOpenLoc) {
			dettagli_filtri.add("TUTTE LOCATION ");
		} else {
			dettagli_filtri.add("SOLO LOCATION APERTE ");
		}

		}
	}

	public void addDettagliFiltri(String dett) {
		dettagli_filtri.add(dett);
	}

	public void removeDettagliFiltri(String dett) {
		if(dettagli_filtri.size()>2)
		dettagli_filtri.remove(2);
	}

	public String getPage_info() {
		return " (dati filtrati per: " + getDettagli_filtri() + ")";
	}

	public Log getLogI() {
		return logI;
	}

	public void setLogI(Log logI) {
		this.logI = logI;
	}

	public Log getLogN() {
		return logN;
	}

	public void setLogN(Log logN) {
		this.logN = logN;
	}
	
	public String getDepthPage(){
		
		String depth = FacesContext
				.getCurrentInstance().getExternalContext().getRequestServletPath();
		
		int rep = depth.replaceAll("[^/]", "").length();
		String relativepath = "";
		
		for(int i=0;i<rep-1;i++){
			relativepath=relativepath+"../";
		}		
		
		return relativepath;
	}

}
