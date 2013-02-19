package it.betplus.birs.controller.tables;

import it.betplus.birs.controller.sheets.InfoPageBean;
import it.betplus.birs.controller.sheets.ReportBean;
import it.betplus.birs.db.dao.MeterDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.model.MeterfactVltDataModel;
import it.betplus.birs.primitive.MeterfactVlt;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.data.FilterEvent;

@ManagedBean(name = "tableBeanVltDismissed")
@SessionScoped
public class TableBeanVltDismissed extends BaseTableBean {

	private static final long serialVersionUID = -3176805730244462965L;

	private static Double totBet = 0.0;
	private static Double totWin = 0.0;
	private static Long totGamesPlayed = 0l;
	private static Double totTotalIn = 0.0;
	private static Double totTotalOut = 0.0;

	private List<MeterfactVlt> tableList;
	private static List<MeterfactVlt> filteredTableList;
	private MeterfactVltDataModel dataModel;
	private MeterfactVlt selectedObj;
	private MeterfactVlt[] selectedObjs;

	private String actualSelected = "";
	private int trend = 0;

	public TableBeanVltDismissed() throws DataLayerException {
		tableList = new ArrayList<MeterfactVlt>();
	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		ReportBean reportBean = findBean("reportBean");

		try {

			List<MeterfactVlt> m = MeterDAO.getInstance().retrieveMeter_vlts_dismissed(
					reportBean.getDataS(), reportBean.getDataE(),
					String.valueOf(params.get(0)));
			//setTableListAndCalculateTotals(m);
			filteredTableList=null;
			setDataModel(new MeterfactVltDataModel(m));
			refreshTableList();

		} catch (DataAccessException e) {

			throw new DataLayerException(e.getMessage());

		}

	}

	public List<MeterfactVlt> getTableList() {
		return tableList;
	}

	public void setTableList(List<MeterfactVlt> tableList) {
		this.tableList = tableList;
	}

	public List<MeterfactVlt> getFilteredTableList() {
		return filteredTableList;
	}

	public void setFilteredTableList(List<MeterfactVlt> filteredTableList) {
		TableBeanVltDismissed.filteredTableList = filteredTableList;
	}

	public MeterfactVltDataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(MeterfactVltDataModel dataModel) {
		this.dataModel = dataModel;
	}

	public MeterfactVlt getSelectedObj() {
		return selectedObj;
	}

	public void setSelectedObj(MeterfactVlt selectedObj) {
		ReportBean reportBean = findBean("reportBean");

		if (selectedObj != null) {
			try {

				if (!actualSelected.equals(selectedObj.getSistemagiocodim()
						.getVlt().getAAMS_VLT_ID())) {
					actualSelected = selectedObj.getSistemagiocodim().getVlt()
							.getAAMS_VLT_ID();
					setTrend(MeterDAO.getInstance()
							.retrieveMeter_SimpleTrendVlt(
									reportBean.getDataS(),
									reportBean.getDataE(),
									selectedObj.getSistemagiocodim().getVlt()
											.getAAMS_VLT_ID()));

				}

			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
		this.selectedObj = selectedObj;
	}

	public MeterfactVlt[] getSelectedObjs() {
		return selectedObjs;
	}

	public void setSelectedObjs(MeterfactVlt[] selectedObjs) {
		this.selectedObjs = selectedObjs;
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

		for (MeterfactVlt mTot : tableList) {

			totBet += mTot.getBet();
			totWin += mTot.getWin();
			totGamesPlayed += mTot.getGamesPlayed();
			totTotalIn += mTot.getTotalIn();
			totTotalOut += mTot.getTotalOut();

		}
	}

	public static void setFilteredTotals() {
		totBet = 0d;
		totWin = 0d;
		totGamesPlayed = 0l;
		totTotalIn = 0d;
		totTotalOut = 0d;

		for (MeterfactVlt mTot : filteredTableList) {

			totBet += mTot.getBet();
			totWin += mTot.getWin();
			totGamesPlayed += mTot.getGamesPlayed();
			totTotalIn += mTot.getTotalIn();
			totTotalOut += mTot.getTotalOut();

		}

	}

	public void setTableListAndCalculateTotals(List<MeterfactVlt> dataList) {
		setTableList(dataList);
		setTotals();
	}

	public int getTrend() {
		return trend;
	}

	public void setTrend(int trend) {
		this.trend = trend;
	}

	public void refreshTableList() {

		ReportBean rBean = findBean("reportBean");
		ArrayList<MeterfactVlt> actTableList = new ArrayList<MeterfactVlt>();

		actTableList = (ArrayList<MeterfactVlt>) this.getDataModel()
				.getData();

		actTableList = (ArrayList<MeterfactVlt>) filterOnDay(actTableList,
				rBean);
		actTableList = (ArrayList<MeterfactVlt>) filterOnClose(
				actTableList, rBean);
		actTableList = (ArrayList<MeterfactVlt>) filterOnBet(actTableList,
				rBean);

		this.setTableList(actTableList);
		this.setTotals();

		rBean.setTooManyRecords(actTableList.size());
	}

	public List<MeterfactVlt> filterOnDay(
			ArrayList<MeterfactVlt> list, ReportBean rBean) {

		InfoPageBean infoPageBean = findBean("infoPageBean");
		infoPageBean.changeDettagliFiltriGiorni(rBean.isModSingTotDay());

		if (rBean.isModSingTotDay()) {
			

			HashMap<String, MeterfactVlt> hSum = new HashMap<String, MeterfactVlt>();

			for (MeterfactVlt mSum : list) {
				if (hSum.containsKey(mSum.getSistemagiocodim().getLoc()
						.getAAMS_LOCATION_ID())) {

					MeterfactVlt mHash = hSum.get(mSum
							.getSistemagiocodim().getLoc()
							.getAAMS_LOCATION_ID());

					mHash.sumValue(mSum);

				} else {
					MeterfactVlt mHash = new MeterfactVlt();

					mHash = mSum.clone();

					hSum.put(mHash.getSistemagiocodim().getLoc()
							.getAAMS_LOCATION_ID(), mHash);
				}

			}

			List<MeterfactVlt> summedList = new ArrayList<MeterfactVlt>(
					hSum.values());

			return summedList;

		} else {
			

			return list;

		}

	}

	public List<MeterfactVlt> filterOnClose(
			ArrayList<MeterfactVlt> list, ReportBean rBean) {

		InfoPageBean infoPageBean = findBean("infoPageBean");
		infoPageBean.changeDettagliFiltriOpen(rBean.isModAllOpenLoc());
		List<MeterfactVlt> tempList = new ArrayList<MeterfactVlt>();

		if (rBean.isModAllOpenLoc()) {

			for (MeterfactVlt m : list) {
				if (rBean.getAvaliableLocations().contains(
						m.getSistemagiocodim().getLoc()))
					tempList.add(m);
			}
			return tempList;
		} else {

			return list;
		}

	}

	public List<MeterfactVlt> filterOnBet(
			ArrayList<MeterfactVlt> list, ReportBean rBean) {

		List<MeterfactVlt> tempTableList = new ArrayList<MeterfactVlt>();

		for (MeterfactVlt m : list) {
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
	public void refreshTableList() {

		ReportBean reportBean = findBean("reportBean");
		ArrayList<MeterfactVlt> tempTableList = new ArrayList<MeterfactVlt>();

		for (MeterfactVlt m : this.getDataModel().getData()) {
			switch (reportBean.getBetOpt()) {
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
				break;
			}
		}

		if (tempTableList.isEmpty() && reportBean.getBetOpt()==0)
			tempTableList = (ArrayList<MeterfactVlt>) this.getDataModel()
					.getData();

		reportBean.setTooManyRecords(tempTableList.size());

		if (filteredTableList != null) {
			this.setFilteredTableList(tempTableList);
			TableBeanVltDismissed.setFilteredTotals();
		} else {
			this.setTableList(tempTableList);
			this.setTotals();
		}
	}*/

}
