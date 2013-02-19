package it.betplus.birs.controller.tables;

import it.betplus.birs.controller.sheets.InfoPageBean;
import it.betplus.birs.controller.sheets.ReportBean;
import it.betplus.birs.db.dao.MeterDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.model.MeterfactGameDataModel;
import it.betplus.birs.primitive.MeterfactGame;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.data.FilterEvent;

@ManagedBean(name = "tableBeanLocationGame")
@SessionScoped
public class TableBeanLocationGame extends BaseTableBean {

	private static final long serialVersionUID = 1295295261554761055L;
	private static Double totBet = 0.0;
	private static Double totWin = 0.0;
	private static Long totGamesPlayed = 0l;

	private List<MeterfactGame> tableList;
	private static List<MeterfactGame> filteredTableList;
	private MeterfactGameDataModel dataModel;
	private MeterfactGame selectedObj;
	private MeterfactGame[] selectedObjs;

	public TableBeanLocationGame() throws DataLayerException {
		tableList = new ArrayList<MeterfactGame>();
	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		ReportBean reportBean = findBean("reportBean");

		try {

			List<MeterfactGame> m = MeterDAO.getInstance()
					.retrieveMeter_location_games(reportBean.getDataS(),
							reportBean.getDataE(),
							String.valueOf(params.get(0)));

			//setTableListAndCalculateTotals(m);
			filteredTableList=null;
			setDataModel(new MeterfactGameDataModel(m));
			refreshTableList();
			
		} catch (DataAccessException e) {

			throw new DataLayerException(e.getMessage());

		}

	}

	public List<MeterfactGame> getTableList() {
		return tableList;
	}

	public void setTableList(List<MeterfactGame> tableList) {
		this.tableList = tableList;
	}

	public List<MeterfactGame> getFilteredTableList() {
		return filteredTableList;
	}

	public void setFilteredTableList(List<MeterfactGame> filteredTableList) {
		TableBeanLocationGame.filteredTableList = filteredTableList;
	}

	public MeterfactGameDataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(MeterfactGameDataModel dataModel) {
		this.dataModel = dataModel;
	}

	public MeterfactGame getSelectedObj() {
		return selectedObj;
	}

	public void setSelectedObj(MeterfactGame selectedObj) {
		this.selectedObj = selectedObj;
	}

	public MeterfactGame[] getSelectedObjs() {
		return selectedObjs;
	}

	public void setSelectedObjs(MeterfactGame[] selectedObjs) {
		this.selectedObjs = selectedObjs;
	}

	public Double getTotBet() {
		return totBet;
	}

	public Double getTotWin() {
		return totWin;
	}

	public String getTotGamesPlayed() {
		DecimalFormat dfmt = new DecimalFormat("");
		return dfmt.format(totGamesPlayed);
	}

	public void setTotals() {
		totBet = 0d;
		totWin = 0d;
		totGamesPlayed = 0l;

		for (MeterfactGame mTot : tableList) {

			totBet += mTot.getBet();
			totWin += mTot.getWin();
			totGamesPlayed += mTot.getGamesPlayed();

		}
	}

	public static void setFilteredTotals() {
		totBet = 0d;
		totWin = 0d;
		totGamesPlayed = 0l;

		for (MeterfactGame mTot : filteredTableList) {

			totBet += mTot.getBet();
			totWin += mTot.getWin();
			totGamesPlayed += mTot.getGamesPlayed();

		}

	}

	public void setTableListAndCalculateTotals(List<MeterfactGame> dataList) {
		setTableList(dataList);
		setTotals();
	}

	public void refreshTableList() {

		ReportBean rBean = findBean("reportBean");
		ArrayList<MeterfactGame> actTableList = new ArrayList<MeterfactGame>();

		actTableList = (ArrayList<MeterfactGame>) this.getDataModel().getData();

		actTableList = (ArrayList<MeterfactGame>) filterOnDay(actTableList,
				rBean);
		actTableList = (ArrayList<MeterfactGame>) filterOnClose(actTableList,
				rBean);
		actTableList = (ArrayList<MeterfactGame>) filterOnBet(actTableList,
				rBean);

		this.setTableList(actTableList);
		this.setTotals();

		rBean.setTooManyRecords(actTableList.size());
	}

	public List<MeterfactGame> filterOnDay(ArrayList<MeterfactGame> list,
			ReportBean rBean) {

		InfoPageBean infoPageBean = findBean("infoPageBean");
		infoPageBean.changeDettagliFiltriGiorni(rBean.isModSingTotDay());

		if (rBean.isModSingTotDay()) {
			

			HashMap<String, MeterfactGame> hSum = new HashMap<String, MeterfactGame>();

			for (MeterfactGame mSum : list) {
				if (hSum.containsKey(mSum.getSistemagiocodim().getLoc()
						.getAAMS_LOCATION_ID())) {

					MeterfactGame mHash = hSum.get(mSum.getSistemagiocodim()
							.getLoc().getAAMS_LOCATION_ID());

					mHash.sumValue(mSum);

				} else {
					MeterfactGame mHash = new MeterfactGame();

					mHash = mSum.clone();

					hSum.put(mHash.getSistemagiocodim().getLoc()
							.getAAMS_LOCATION_ID(), mHash);
				}

			}

			List<MeterfactGame> summedList = new ArrayList<MeterfactGame>(
					hSum.values());

			return summedList;

		} else {
			

			return list;

		}

	}

	public List<MeterfactGame> filterOnClose(ArrayList<MeterfactGame> list,
			ReportBean rBean) {

		InfoPageBean infoPageBean = findBean("infoPageBean");
		infoPageBean.changeDettagliFiltriOpen(rBean.isModAllOpenLoc());
		List<MeterfactGame> tempList = new ArrayList<MeterfactGame>();

		if (rBean.isModAllOpenLoc()) {

			for (MeterfactGame m : list) {
				if (rBean.getAvaliableLocations().contains(
						m.getSistemagiocodim().getLoc()))
					tempList.add(m);
			}
			return tempList;
		} else {

			return list;
		}

	}

	public List<MeterfactGame> filterOnBet(ArrayList<MeterfactGame> list,
			ReportBean rBean) {

		List<MeterfactGame> tempTableList = new ArrayList<MeterfactGame>();

		for (MeterfactGame m : list) {
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
	 * public void refreshTableList() {
	 * 
	 * ReportBean reportBean = findBean("reportBean"); ArrayList<MeterfactGame>
	 * tempTableList = new ArrayList<MeterfactGame>();
	 * 
	 * for (MeterfactGame m : this.getDataModel().getData()) { switch
	 * (reportBean.getBetOpt()) { case 1: if (m.getBet() == 0)
	 * tempTableList.add(m); break; case 2: if (m.getBet() > 0 && m.getBet() <
	 * 1000) tempTableList.add(m); break; case 3: if (m.getBet() >= 1000)
	 * tempTableList.add(m); break; default: break; } }
	 * 
	 * if (tempTableList.isEmpty() && reportBean.getBetOpt()==0) tempTableList =
	 * (ArrayList<MeterfactGame>) this.getDataModel() .getData();
	 * 
	 * reportBean.setTooManyRecords(tempTableList.size());
	 * 
	 * if (filteredTableList != null) {
	 * this.setFilteredTableList(tempTableList);
	 * TableBeanLocationGame.setFilteredTotals(); } else {
	 * this.setTableList(tempTableList); this.setTotals(); } }
	 */

}
