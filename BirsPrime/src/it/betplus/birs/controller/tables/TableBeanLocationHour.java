package it.betplus.birs.controller.tables;

import it.betplus.birs.controller.sheets.InfoPageBean;
import it.betplus.birs.controller.sheets.ReportBean;
import it.betplus.birs.db.dao.MeterDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.model.MeterfactLocationDataModel;
import it.betplus.birs.primitive.MeterfactLocation;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.data.FilterEvent;

@ManagedBean(name = "tableBeanLocationHour")
@SessionScoped
public class TableBeanLocationHour extends BaseTableBean {

	private static final long serialVersionUID = -8048717366497032338L;

	private static Double totBet = 0.0;	
	private static Double totWin = 0.0;
	private static Long totGamesPlayed = 0l;
	private static Double totTotalIn = 0.0;
	private static Double totTotalOut = 0.0;

	private List<MeterfactLocation> tableList;
	private static List<MeterfactLocation> filteredTableList;
	private MeterfactLocationDataModel dataModel;
	private MeterfactLocation selectedObj;
	private MeterfactLocation[] selectedObjs;

	private String actualSelected = "";
	private int trend = 0;
	


	public TableBeanLocationHour() {

		super();

	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		ReportBean reportBean = findBean("reportBean");

		try {

			List<MeterfactLocation> m = MeterDAO.getInstance()
					.retrieveMeter_locations_hour(reportBean.getDataS(),
							reportBean.getDataE(),
							String.valueOf(params.get(0)));

			//setTableListAndCalculateTotals(m);
			filteredTableList=null;
			setDataModel(new MeterfactLocationDataModel(m));
			refreshTableList();

		} catch (DataAccessException e) {

			throw new DataLayerException(e.getMessage());

		}

	}

	
	public Double getTotBet() {
		return totBet;
	}

	public Double getTotWin() {
		return totWin;
	}

	public Long getTotGamesPlayed() {
		return totGamesPlayed;
	}

	public Double getTotTotalIn() {
		return totTotalIn;
	}

	public Double getTotTotalOut() {
		return totTotalOut;
	}

	

	public void setTotals() {
		totBet = 0d;
		totWin = 0d;
		totGamesPlayed = 0l;
		totTotalIn = 0d;
		totTotalOut = 0d;

		for (MeterfactLocation mTot : tableList) {

			totBet += mTot.getBet();
			totWin += mTot.getWin();
			totGamesPlayed += mTot.getGamesPlayed();
			totTotalIn += mTot.getTotalIn();
			totTotalOut += mTot.getTotalOut();

		}
	}
	
	public void setFilteredTotals(){
		totBet = 0d;
		totWin = 0d;
		totGamesPlayed = 0l;
		totTotalIn = 0d;
		totTotalOut = 0d;

		for (MeterfactLocation mTot : filteredTableList) {

			totBet += mTot.getBet();
			totWin += mTot.getWin();
			totGamesPlayed += mTot.getGamesPlayed();
			totTotalIn += mTot.getTotalIn();
			totTotalOut += mTot.getTotalOut();

		}
		
	}
	
	public void setTableListAndCalculateTotals(List<MeterfactLocation> dataList) {
		setTableList(dataList);
		setTotals();
	}

	public List<MeterfactLocation> getTableList() {
		return tableList;
	}

	public void setTableList(List<MeterfactLocation> tableList) {
		this.tableList = tableList;
	}

	public List<MeterfactLocation> getFilteredTableList() {
		return filteredTableList;
	}

	public void setFilteredTableList(List<MeterfactLocation> filteredTableList) {
		TableBeanLocationHour.filteredTableList = filteredTableList;
	}

	public MeterfactLocation getSelectedObj() {

		return selectedObj;
	}

	public void setSelectedObj(MeterfactLocation selectedObj) {
		ReportBean reportBean = findBean("reportBean");
		if (selectedObj != null) {
			try {
				if (!actualSelected
								.equals(selectedObj.getSistemagiocodim()
										.getLoc().getAAMS_LOCATION_ID())) {
					actualSelected = selectedObj.getSistemagiocodim().getLoc().getAAMS_LOCATION_ID();

					setTrendLocation(MeterDAO.getInstance()
							.retrieveMeter_SimpleTrendLocation(
									reportBean.getDataS(),
									reportBean.getDataE(),
									selectedObj.getSistemagiocodim().getLoc()
											.getAAMS_LOCATION_ID()));

				}
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
		this.selectedObj = selectedObj;
	}

	public MeterfactLocation[] getSelectedObjs() {
		return selectedObjs;
	}

	public void setSelectedObjs(MeterfactLocation[] selectedObjs) {
		this.selectedObjs = selectedObjs;
	}

	public MeterfactLocationDataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(MeterfactLocationDataModel dataModel) {
		this.dataModel = dataModel;
	}

	public int getTrend() {
		return trend;
	}

	public void setTrendLocation(int trend) {
		this.trend = trend;
	}
	
	public void refreshTableList() {

		ReportBean rBean = findBean("reportBean");
		ArrayList<MeterfactLocation> actTableList = new ArrayList<MeterfactLocation>();

		
		if(this.getFilteredTableList()!=null)
			actTableList=(ArrayList<MeterfactLocation>) this.getFilteredTableList();
		else		
		actTableList = (ArrayList<MeterfactLocation>) this.getDataModel()
				.getData();

		actTableList = (ArrayList<MeterfactLocation>) filterOnDay(actTableList,
				rBean);
		actTableList = (ArrayList<MeterfactLocation>) filterOnClose(
				actTableList, rBean);
		actTableList = (ArrayList<MeterfactLocation>) filterOnBet(actTableList,
				rBean);

		if(this.getFilteredTableList()!=null){
			this.setFilteredTableList(actTableList);
		    this.setFilteredTotals();
		}
		else{		
			this.setTableList(actTableList);		
			this.setTotals();
		}
		rBean.setTooManyRecords(actTableList.size());
	}

	public List<MeterfactLocation> filterOnDay(
			ArrayList<MeterfactLocation> list, ReportBean rBean) {

		InfoPageBean infoPageBean = findBean("infoPageBean");
		infoPageBean.changeDettagliFiltriGiorni(rBean.isModSingTotDay());

		if (rBean.isModSingTotDay()) {
			

			HashMap<String, MeterfactLocation> hSum = new HashMap<String, MeterfactLocation>();

			for (MeterfactLocation mSum : list) {
				if (hSum.containsKey(mSum.getSistemagiocodim().getLoc()
						.getAAMS_LOCATION_ID())) {

					MeterfactLocation mHash = hSum.get(mSum
							.getSistemagiocodim().getLoc()
							.getAAMS_LOCATION_ID());

					mHash.sumValue(mSum);

				} else {
					MeterfactLocation mHash = new MeterfactLocation();

					mHash = mSum.clone();

					hSum.put(mHash.getSistemagiocodim().getLoc()
							.getAAMS_LOCATION_ID(), mHash);
				}

			}

			List<MeterfactLocation> summedList = new ArrayList<MeterfactLocation>(
					hSum.values());

			return summedList;

		} else {
			

			return list;

		}

	}

	public List<MeterfactLocation> filterOnClose(
			ArrayList<MeterfactLocation> list, ReportBean rBean) {

		InfoPageBean infoPageBean = findBean("infoPageBean");
		infoPageBean.changeDettagliFiltriOpen(rBean.isModAllOpenLoc());
		List<MeterfactLocation> tempList = new ArrayList<MeterfactLocation>();

		if (rBean.isModAllOpenLoc()) {

			for (MeterfactLocation m : list) {
				if (rBean.getAvaliableLocations().contains(
						m.getSistemagiocodim().getLoc()))
					tempList.add(m);
			}
			return tempList;
		} else {

			return list;
		}

	}

	public List<MeterfactLocation> filterOnBet(
			ArrayList<MeterfactLocation> list, ReportBean rBean) {

		List<MeterfactLocation> tempTableList = new ArrayList<MeterfactLocation>();

		for (MeterfactLocation m : list) {
			switch (rBean.getBetOpt()) {
			case 1:
				if (m.getBet() == 0)
					tempTableList.add(m);
				break;
			case 2:
				if (m.getBet() > 0 && m.getBet() < 1000)
					tempTableList.add(m);
				break;
			case 3:
				if (m.getBet() >= 1000)
					tempTableList.add(m);
				break;
			default:
				tempTableList = list;
				break;
			}
		}

		return tempTableList;
	}

	public void handleFilter(FilterEvent f) {
		ReportBean reportBean = findBean("reportBean");
		reportBean.setTooManyRecords(filteredTableList.size());
		setFilteredTotals();
	}

	
	/*
	public void refreshTableList(){
		
		ReportBean reportBean = findBean("reportBean");
		ArrayList<MeterfactLocation> tempTableList = new ArrayList<MeterfactLocation>();
		
		for(MeterfactLocation m:this.getDataModel().getData()){
			switch (reportBean.getBetOpt()) {
			case 1:
				if(m.getBet()==0)
					tempTableList.add(m);
				break;
			case 2:
				if(m.getBet()>0 && m.getBet()<1000)
					tempTableList.add(m);
				break;
			case 3:
				if(m.getBet()>=1000)
					tempTableList.add(m);
				break;
			default:
				break;
			}
		}
		
		if (tempTableList.isEmpty() && reportBean.getBetOpt()==0)
			tempTableList = (ArrayList<MeterfactLocation>) this.getDataModel().getData();
		
		reportBean.setTooManyRecords(tempTableList.size());
		
		if(filteredTableList!=null){
			this.setFilteredTableList(tempTableList);
			TableBeanLocationHour.setFilteredTotals();
		}
		else{
		this.setTableList(tempTableList);
		this.setTotals();
		}
	}	*/
	

}
