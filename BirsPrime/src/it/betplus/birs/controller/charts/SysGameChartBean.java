package it.betplus.birs.controller.charts;

import it.betplus.birs.controller.sheets.ReportBean;
import it.betplus.birs.db.dao.MeterDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.MeterfactSysGame;
import it.betplus.birs.util.IConstants;
import it.betplus.web.framework.managedbeans.ChartsManagerBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.ChartSeries;



@ManagedBean(name="sysGiocoChartBean")
@SessionScoped
public class SysGameChartBean extends ChartsManagerBean{


	private static final long serialVersionUID = -9038906790050455829L;
	
	private boolean changeSysGioco = true;
	List<MeterfactSysGame> meters = new ArrayList<MeterfactSysGame>();
	
	private Double maxValue=0d;

	public SysGameChartBean() {
	}
	
	@Override
	public String refreshChartAction(String chartName) {
		loadMeterFactSysGame();
    	createChartModel();
    	return "chart-sysgioco";   
	}
	

	public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                        "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());
        
     //   MeterfactSysGioco mAct = meters.get(event.getItemIndex());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

    
   
    public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}
    
      
    private void loadMeterFactSysGame(){
    	ReportBean rBean = findBean("reportBean");
		meters = new ArrayList<MeterfactSysGame>();
		

		try {
			meters = MeterDAO.getInstance().retrieveMeter_sysGioco(	rBean.getDataS(), rBean.getDataE());
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

    }

	private void createChartModel() {	
		maxValue=0d;
		ChartSeries sysGioco= new ChartSeries();   
		ArrayList<ChartSeries> series = new ArrayList<ChartSeries>();

				
		for (MeterfactSysGame m : meters) {
			
			if(changeSysGioco)
				sysGioco = new ChartSeries();  
			
			if(IConstants.SYS_GIOCO_NOVOMATIC==m.getSistemagiocodim().getAAMS_GAMESYSTEM_ID()){
			sysGioco.setLabel("NOVOMATIC");
			//System.out.println("NOVOMATIC "+m.getTempodim().getDay()+" - "+m.getTempodim().getMonth()+"**"+m.getBet());
			}
			else{
			sysGioco.setLabel("INSPIRED");
			//System.out.println("INSPIRED "+m.getTempodim().getDay()+" - "+m.getTempodim().getMonth()+"**"+m.getBet());
			}
			
			sysGioco.set(m.getTempodim().getDay()+"/"+m.getTempodim().getMonth(),m.getBet());
			
			if(maxValue<=m.getBet())
			     maxValue=m.getBet();		
			
			if(meters.indexOf(m)<meters.size()-1){
				if(m.getSistemagiocodim().getAAMS_GAMESYSTEM_ID()!=meters.get(meters.indexOf(m)+1).getSistemagiocodim().getAAMS_GAMESYSTEM_ID()){
					series.add(sysGioco);
					changeSysGioco = true;
				}
				else
					changeSysGioco = false;
			}else{
				series.add(sysGioco);
				changeSysGioco = true;
			}
							
		}
		

		try {
				createOrUpdateChart("lineSysGameChart", series);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
