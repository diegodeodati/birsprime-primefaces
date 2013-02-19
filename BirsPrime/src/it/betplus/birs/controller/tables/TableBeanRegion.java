package it.betplus.birs.controller.tables;

import it.betplus.birs.controller.sheets.InfoPageBean;
import it.betplus.birs.controller.sheets.ReportBean;
import it.betplus.birs.db.dao.MeterDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.model.MeterfactRegionDataModel;
import it.betplus.birs.primitive.MeterfactRegion;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.data.FilterEvent;

@ManagedBean(name = "tableBeanRegion")
@SessionScoped
public class TableBeanRegion extends BaseTableBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Double totBet = 0.0;
	private static Double totWin = 0.0;
	private static Long totGamesPlayed = 0l;
	private static Double totTotalIn = 0.0;
	private static Double totTotalOut = 0.0;

	private List<MeterfactRegion> tableList;
	private static List<MeterfactRegion> filteredTableList;
	private MeterfactRegionDataModel dataModel;
	private MeterfactRegion selectedObj;
	private MeterfactRegion[] selectedObjs;

	public TableBeanRegion() throws DataLayerException {
		tableList = new ArrayList<MeterfactRegion>();
	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		ReportBean reportBean = findBean("reportBean");

		try {

			List<MeterfactRegion> m = MeterDAO.getInstance()
					.retrieveMeter_regions(reportBean.getDataS(),
							reportBean.getDataE(),
							String.valueOf(params.get(0)));

			//setTableListAndCalculateTotals(m);
			filteredTableList=null;
			setDataModel(new MeterfactRegionDataModel(m));
			refreshTableList();

		} catch (DataAccessException e) {

			throw new DataLayerException(e.getMessage());

		}

	}

	public List<MeterfactRegion> getTableList() {
		return tableList;
	}

	public void setTableList(List<MeterfactRegion> tableList) {
		this.tableList = tableList;
	}

	public List<MeterfactRegion> getFilteredTableList() {
		return filteredTableList;
	}

	public void setFilteredTableList(List<MeterfactRegion> filteredTableList) {
		TableBeanRegion.filteredTableList = filteredTableList;
	}

	public MeterfactRegionDataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(MeterfactRegionDataModel dataModel) {
		this.dataModel = dataModel;
	}

	public MeterfactRegion getSelectedObj() {
		return selectedObj;
	}

	public void setSelectedObj(MeterfactRegion selectedObj) {
		this.selectedObj = selectedObj;
	}

	public MeterfactRegion[] getSelectedObjs() {
		return selectedObjs;
	}

	public void setSelectedObjs(MeterfactRegion[] selectedObjs) {
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

		for (MeterfactRegion mTot : tableList) {

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

		for (MeterfactRegion mTot : filteredTableList) {

			totBet += mTot.getBet();
			totWin += mTot.getWin();
			totGamesPlayed += mTot.getGamesPlayed();
			totTotalIn += mTot.getTotalIn();
			totTotalOut += mTot.getTotalOut();

		}

	}

	public void setTableListAndCalculateTotals(List<MeterfactRegion> dataList) {
		setTableList(dataList);
		setTotals();
	}

	public void refreshTableList() {

		ReportBean rBean = findBean("reportBean");
		ArrayList<MeterfactRegion> actTableList = new ArrayList<MeterfactRegion>();

		actTableList = (ArrayList<MeterfactRegion>) this.getDataModel()
				.getData();

		actTableList = (ArrayList<MeterfactRegion>) filterOnDay(actTableList,
				rBean);
		actTableList = (ArrayList<MeterfactRegion>) filterOnClose(actTableList,
				rBean);
		actTableList = (ArrayList<MeterfactRegion>) filterOnBet(actTableList,
				rBean);

		this.setTableList(actTableList);
		this.setTotals();

		rBean.setTooManyRecords(actTableList.size());
	}

	public List<MeterfactRegion> filterOnDay(ArrayList<MeterfactRegion> list,
			ReportBean rBean) {

		InfoPageBean infoPageBean = findBean("infoPageBean");
		infoPageBean.changeDettagliFiltriGiorni(rBean.isModSingTotDay());

		if (rBean.isModSingTotDay()) {
			log.info("MOSTRA TOTALE REGION");
			TableBeanRegion tabBeanRegion = findBean("tableBeanRegion");

			List<MeterfactRegion> mTot = tabBeanRegion.getTableList();

			HashMap<Integer, MeterfactRegion> hSum = new HashMap<Integer, MeterfactRegion>();

			for (MeterfactRegion mSum : mTot) {
				if (hSum.containsKey(mSum.getSpaziodim().getRegione()
						.getIdReg())) {

					MeterfactRegion mHash = hSum.get(mSum.getSpaziodim()
							.getRegione().getIdReg());

					mHash.sumValue(mSum);

				} else {
					MeterfactRegion mHash = new MeterfactRegion();

					mHash = mSum.clone();

					hSum.put(mSum.getSpaziodim().getRegione().getIdReg(), mHash);
				}

			}

			List<MeterfactRegion> summedList = new ArrayList<MeterfactRegion>(
					hSum.values());

			return summedList;

		} else {
			

			return list;

		}

	}

	public List<MeterfactRegion> filterOnClose(ArrayList<MeterfactRegion> list,
			ReportBean rBean) {

		InfoPageBean infoPageBean = findBean("infoPageBean");
		infoPageBean.changeDettagliFiltriOpen(rBean.isModAllOpenLoc());
		List<MeterfactRegion> tempList = new ArrayList<MeterfactRegion>();

		if (rBean.isModAllOpenLoc()) {

			for (MeterfactRegion m : list) {
				if (rBean.getAvaliableLocations().contains(
						m.getSistemagiocodim().getLoc()))
					tempList.add(m);
			}
			return tempList;
		} else {

			return list;
		}

	}

	public List<MeterfactRegion> filterOnBet(ArrayList<MeterfactRegion> list,
			ReportBean rBean) {

		List<MeterfactRegion> tempTableList = new ArrayList<MeterfactRegion>();

		for (MeterfactRegion m : list) {
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
	 * 
	 * public void refreshTableList() {
	 * 
	 * ReportBean reportBean = findBean("reportBean");
	 * ArrayList<MeterfactRegion> tempTableList = new
	 * ArrayList<MeterfactRegion>();
	 * 
	 * for (MeterfactRegion m : this.getDataModel().getData()) { switch
	 * (reportBean.getBetOpt()) { case 1: if (m.getBet() == 0)
	 * tempTableList.add(m); break; case 2: if (m.getBet() > 0 && m.getBet() <
	 * 1000) tempTableList.add(m); break; case 3: if (m.getBet() >= 1000)
	 * tempTableList.add(m); break; default: tempTableList =
	 * (ArrayList<MeterfactRegion>) tableList; break; } }
	 * 
	 * if (tempTableList.isEmpty() && reportBean.getBetOpt()==0) tempTableList =
	 * (ArrayList<MeterfactRegion>) this.getDataModel() .getData();
	 * 
	 * reportBean.setTooManyRecords(tempTableList.size());
	 * 
	 * if (filteredTableList != null) {
	 * this.setFilteredTableList(tempTableList);
	 * TableBeanRegion.setFilteredTotals(); } else {
	 * this.setTableList(tempTableList); this.setTotals(); } }
	 */
}
