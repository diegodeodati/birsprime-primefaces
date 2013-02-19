package it.betplus.birs.controller.charts;

import it.betplus.birs.controller.sheets.ReportBean;
import it.betplus.birs.db.dao.MeterDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.MeterfactLocation;
import it.betplus.web.framework.managedbeans.ChartsManagerBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.chart.ChartSeries;



@ManagedBean(name="locationChartBean")
@SessionScoped
public class LocationChartBean extends ChartsManagerBean{


	private static final long serialVersionUID = 1L;
	
	List<MeterfactLocation> meters = new ArrayList<MeterfactLocation>();
	
	private Double maxValue=0d;	
	
	public LocationChartBean() {
		super();
	}


	@Override
	public String refreshChartAction(String chartName) {
		ReportBean rBean = findBean("reportBean");
		log.debug(rBean.getSelectedLocation());
		
		String code_location=rBean.getSelectedLocation().split("-")[0].trim();
    	if(code_location==null || code_location.equals("NO-LOC-ID")){    		 
            sendErrorMessageToUser("ERRORE LOCATION", "Nessuna Location selezionata");            
            createChartModel();
            
            String redirect = ((HttpServletRequest) FacesContext
    				.getCurrentInstance().getExternalContext().getRequest())
    				.getRequestURI();
    		
    		if (redirect
    				.contains("/BirsPrime/pages/chart/chart-location.xhtml"))
    			return "";
    		else
    			return "chart-location";
    	}
    	else{
    		
    	loadMeterFactLocation();
    	createChartModel();
    	return "chart-location";  
    	}
	}

    public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}



    /*
    public String refreshChartModel(){
    	ReportBean rBean = findBean("reportBean");
    	if(rBean.getSelectedLocation()==null && rBean.getSelectedLocation().equals("NO-LOC-ID")){
    		FacesContext facesContext = FacesContext.getCurrentInstance();  
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERRORE LOCATION","Nessuna Location selezionata"));  
      	    return "chart-location"; 
    	}
    	else{
    	createChartModel();
    	return "chart-location";  
    	}
    }*/

	private void loadMeterFactLocation(){
		ReportBean rBean = findBean("reportBean");
		meters = new ArrayList<MeterfactLocation>();
		
		try {
			String code_location=rBean.getSelectedLocation().split("-")[0].trim();
			meters = MeterDAO.getInstance().retrieveMeter_location(rBean.getDataS(), rBean.getDataE(),code_location);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
	
	private void createChartModel() {		
		maxValue=0d;
		ArrayList<ChartSeries> series = new ArrayList<ChartSeries>();
		ChartSeries locationBet= new ChartSeries();   
		ChartSeries locationWin= new ChartSeries(); 
		ChartSeries locationHouseWin= new ChartSeries(); 
		
	
		if(meters.size()>0){
			locationBet.setLabel(meters.get(0).getSistemagiocodim().getLoc().getCOMMERCIAL_NAME()+" - BET") ;
			locationWin.setLabel(meters.get(0).getSistemagiocodim().getLoc().getCOMMERCIAL_NAME()+" - WIN") ;
			locationHouseWin.setLabel(meters.get(0).getSistemagiocodim().getLoc().getCOMMERCIAL_NAME()+" - HOUSEWIN") ;
		}
		
		for (MeterfactLocation m : meters) {			
				
			locationBet.set(m.getTempodim().getDay()+"/"+m.getTempodim().getMonth(),m.getBet());
			locationWin.set(m.getTempodim().getDay()+"/"+m.getTempodim().getMonth(),m.getWin());
			locationHouseWin.set(m.getTempodim().getDay()+"/"+m.getTempodim().getMonth(),m.getHouseWin());
					
			if(maxValue<=m.getBet())
			     maxValue=m.getBet();			
		}
		
		
		series.add(locationBet);
		series.add(locationWin);
		series.add(locationHouseWin);
		
		try {
			createOrUpdateChart("lineLocationChart", series);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



	
}
