package it.betplus.birs.controller.tables;

import it.betplus.birs.controller.LoginBeanBirs;
import it.betplus.birs.controller.sheets.InfoPageBean;
import it.betplus.birs.controller.sheets.ReportBean;
import it.betplus.birs.db.dao.MeterDAO;
import it.betplus.birs.db.dao.SeicentoDAO;
import it.betplus.birs.db.dao.UtentiDAO;
import it.betplus.birs.db.dao.VltMilionarieDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.model.MatcherVltDataModel;
import it.betplus.birs.primitive.Matcher;
import it.betplus.birs.primitive.MeterfactVlt;
import it.betplus.birs.primitive.RealMillionvlt;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.data.FilterEvent;

@ManagedBean(name = "tableBeanMatcherVlt")
@SessionScoped
public class TableBeanMatcherVlt extends BaseTableBean {

	private static final long serialVersionUID = 189788887360867976L;

	private static Double totBet = 0.0;
	private static Double totWin = 0.0;
	private static Long totGamesPlayed = 0l;
	private static Double totTotalIn = 0.0;
	private static Double totTotalOut = 0.0;

	private List<Matcher> tableList;
	private static List<Matcher> filteredTableList;
	private MatcherVltDataModel dataModel;
	private Matcher selectedObj;
	private Matcher[] selectedObjs;

	public TableBeanMatcherVlt() {

		super();

	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		ReportBean reportBean = findBean("reportBean");

		try {

			List<Matcher> m = generateMatchingVlt(reportBean.getDataS(),
					reportBean.getDataE());
			// setTableListAndCalculateTotals(m);
			filteredTableList = null;
			setDataModel(new MatcherVltDataModel(m));
			refreshTableList();

		} catch (DataAccessException e) {

			throw new DataLayerException(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<Matcher> generateMatchingVlt(Date dataS, Date dataE)
			throws Exception {
		ArrayList<Matcher> matchingVltList = new ArrayList<Matcher>();

		ArrayList<MeterfactVlt> meterfactVltSogei = (ArrayList<MeterfactVlt>) MeterDAO
				.getInstance().retrieveMeter_vlts(dataS, dataE, "%");

		HashMap<String, MeterfactVlt> mapMeterfactVlt600 = SeicentoDAO
				.getInstance().retrieveMeterMap600_vlts(dataS, dataE);

		HashMap<String, RealMillionvlt> hashMillionVlt = VltMilionarieDAO
				.getInstance().getRangeVltMilionarieByVlt(dataS, dataE);

		for (MeterfactVlt mSogeiVlt : meterfactVltSogei) {

			String key = mSogeiVlt.getSistemagiocodim().getVlt()
					.getAAMS_VLT_ID()
					+ "-" + mSogeiVlt.getTempodim().simpleTempodimToString();

			if (mapMeterfactVlt600.containsKey(key)) {
				MeterfactVlt m600Vlt = mapMeterfactVlt600.get(key);

				if (mSogeiVlt.getBet().doubleValue() != m600Vlt.getBet()
						.doubleValue()
						|| mSogeiVlt.getWin().doubleValue() != m600Vlt.getWin()
								.doubleValue()
						|| mSogeiVlt.getTotalIn().doubleValue() != m600Vlt
								.getTotalIn().doubleValue()
						|| mSogeiVlt.getTotalOut().doubleValue() != m600Vlt
								.getTotalOut().doubleValue()) {

					if (hashMillionVlt.containsKey(mSogeiVlt.getTempodim()
							.simpleTempodimToString()
							+ "-"
							+ mSogeiVlt.getSistemagiocodim().getVlt()
									.getAAMS_VLT_ID())) {
						mSogeiVlt.setMilionarie(true);
						mSogeiVlt.setRealMillionVlt(hashMillionVlt
								.get(mSogeiVlt.getTempodim()
										.simpleTempodimToString()
										+ "-"
										+ mSogeiVlt.getSistemagiocodim()
												.getVlt().getAAMS_VLT_ID()));
					}

					Matcher m = new Matcher();
					m.setMSogei(mSogeiVlt);
					m.setM600(m600Vlt);

					matchingVltList.add(m);

					mapMeterfactVlt600.remove(key);
				} else {
					mapMeterfactVlt600.remove(key);
				}
			} else {

				if (!mSogeiVlt.isInconsiderable()) {
					Matcher m = new Matcher();
					m.setMSogei(mSogeiVlt);
					m.setM600(new MeterfactVlt());

					matchingVltList.add(m);
				}
			}

		}

		/*
		 * ArrayList<MeterfactVlt> meterfactVlt600 = new
		 * ArrayList<MeterfactVlt>( mapMeterfactVlt600.values());
		 * 
		 * for (MeterfactVlt m600Vlt : meterfactVlt600) {
		 * 
		 * if(!m600Vlt.isInconsiderable()){ Matcher m = new Matcher();
		 * m.setMSogei(new MeterfactVlt() ); m.setM600(m600Vlt);
		 * 
		 * matchingVltList.add(m); } }
		 */

		return matchingVltList;
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

		for (Matcher mTot : tableList) {

			totBet += mTot.getMSogei().getBet() - mTot.getM600().getBet();
			totWin += mTot.getMSogei().getWin() - mTot.getM600().getWin();
			totGamesPlayed += mTot.getMSogei().getGamesPlayed()
					- mTot.getM600().getGamesPlayed();
			totTotalIn += ((MeterfactVlt) mTot.getMSogei()).getTotalIn()
					- ((MeterfactVlt) mTot.getM600()).getTotalIn();
			totTotalOut += ((MeterfactVlt) mTot.getMSogei()).getTotalOut()
					- ((MeterfactVlt) mTot.getM600()).getTotalOut();

		}
	}

	public static void setFilteredTotals() {
		totBet = 0d;
		totWin = 0d;
		totGamesPlayed = 0l;
		totTotalIn = 0d;
		totTotalOut = 0d;

		for (Matcher mTot : filteredTableList) {

			totBet += mTot.getMSogei().getBet() - mTot.getM600().getBet();
			totWin += mTot.getMSogei().getWin() - mTot.getM600().getWin();
			totGamesPlayed += mTot.getMSogei().getGamesPlayed()
					- mTot.getM600().getGamesPlayed();
			totTotalIn += ((MeterfactVlt) mTot.getMSogei()).getTotalIn()
					- ((MeterfactVlt) mTot.getM600()).getTotalIn();
			totTotalOut += ((MeterfactVlt) mTot.getMSogei()).getTotalOut()
					- ((MeterfactVlt) mTot.getM600()).getTotalOut();

		}
	}

	public void setTableListAndCalculateTotals(List<Matcher> dataList) {
		setTableList(dataList);
		setTotals();
	}

	public List<Matcher> getTableList() {
		return tableList;
	}

	public void setTableList(List<Matcher> tableList) {
		this.tableList = tableList;
	}

	public List<Matcher> getFilteredTableList() {
		return filteredTableList;
	}

	public void setFilteredTableList(List<Matcher> filteredTableList) {
		TableBeanMatcherVlt.filteredTableList = filteredTableList;
	}

	public Matcher getSelectedObj() {
		return selectedObj;
	}

	public void setSelectedObj(Matcher selectedObj) {
		System.out.println(selectedObj.getMSogei());
		this.selectedObj = selectedObj;
	}

	public Matcher[] getSelectedObjs() {
		return selectedObjs;
	}

	public void setSelectedObjs(Matcher[] selectedObjs) {
		this.selectedObjs = selectedObjs;
	}

	public MatcherVltDataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(MatcherVltDataModel dataModel) {
		this.dataModel = dataModel;
	}

	public void refreshTableList() throws DataAccessException {

		ReportBean rBean = findBean("reportBean");
		ArrayList<Matcher> actTableList = new ArrayList<Matcher>();

		actTableList = (ArrayList<Matcher>) this.getDataModel().getData();

		actTableList = (ArrayList<Matcher>) filterOnDay(actTableList, rBean);
		actTableList = (ArrayList<Matcher>) filterOnClose(actTableList, rBean);

		actTableList = (ArrayList<Matcher>) filterOnPreferences(actTableList,
				rBean);

		actTableList = (ArrayList<Matcher>) filterMillionvltLocMatcher(
				actTableList, rBean);

		this.setTableList(actTableList);
		this.setTotals();

		rBean.setTooManyRecords(actTableList.size());
	}

	public List<Matcher> filterOnDay(ArrayList<Matcher> list, ReportBean rBean) {

		InfoPageBean infoPageBean = findBean("infoPageBean");
		infoPageBean.changeDettagliFiltriGiorni(rBean.isModSingTotDay());

		if (rBean.isModSingTotDay()) {

			HashMap<String, Matcher> hSum = new HashMap<String, Matcher>();

			for (Matcher mSum : list) {
				if (hSum.containsKey(mSum.getSistemagiocodim().getLoc()
						.getAAMS_LOCATION_ID()
						+ "-"
						+ mSum.getSistemagiocodim().getVlt().getAAMS_VLT_ID())) {

					Matcher mHash = hSum.get(mSum.getSistemagiocodim().getLoc()
							.getAAMS_LOCATION_ID()
							+ "-"
							+ mSum.getSistemagiocodim().getVlt()
									.getAAMS_VLT_ID());

					mHash.sumValue(mSum);

				} else {
					Matcher mHash = new Matcher();

					mHash = mSum.clone();

					hSum.put(mHash.getSistemagiocodim().getLoc()
							.getAAMS_LOCATION_ID()
							+ "-"
							+ mSum.getSistemagiocodim().getVlt()
									.getAAMS_VLT_ID(), mHash);
				}

			}

			List<Matcher> summedList = new ArrayList<Matcher>(hSum.values());

			return summedList;

		} else {
			return list;

		}

	}

	public List<Matcher> filterOnClose(ArrayList<Matcher> list, ReportBean rBean) {

		InfoPageBean infoPageBean = findBean("infoPageBean");
		infoPageBean.changeDettagliFiltriOpen(rBean.isModAllOpenLoc());
		List<Matcher> tempList = new ArrayList<Matcher>();

		if (rBean.isModAllOpenLoc()) {

			for (Matcher m : list) {
				if (rBean.getAvaliableLocations().contains(
						m.getSistemagiocodim().getLoc()))
					tempList.add(m);
			}
			return tempList;
		} else {

			return list;
		}

	}

	public List<Matcher> filterOnPreferences(ArrayList<Matcher> list,
			ReportBean rBean) throws DataAccessException {

		ArrayList<Matcher> mOut = new ArrayList<Matcher>();
		LoginBeanBirs loginBean = findBean("loginBean");

		if (rBean.getMatcherPreferences().size() > 0) {

			String pref = "";

			for (String s : rBean.getMatcherPreferences()) {
				pref = pref + s;
			}

			if (!pref.equals("btg"))
				for (Matcher m : list) {
					if (pref.equals("bt")
							&& (m.isDiffBet() || m.isDiffWin()
									|| m.isDiffTotalIn() || m.isDiffTotalOut())) {
						mOut.add(m);
					} else if (pref.equals("bg")
							&& (m.isDiffBet() || m.isDiffWin() || m
									.isDiffGamesPlayed())) {
						mOut.add(m);
					} else if (pref.equals("tg")
							&& (m.isDiffTotalIn() || m.isDiffTotalOut() || m
									.isDiffGamesPlayed())) {
						mOut.add(m);
					} else if (pref.equals("b")
							&& (m.isDiffBet() || m.isDiffWin())) {
						mOut.add(m);
					} else if (pref.equals("t")
							&& (m.isDiffTotalIn() || m.isDiffTotalOut())) {
						mOut.add(m);
					} else if (pref.equals("g") && (m.isDiffGamesPlayed())) {
						mOut.add(m);
					}
				}
			else
				mOut = (ArrayList<Matcher>) list;

			loginBean.getLoggedUser().setMatcherPreferences(
					rBean.getMatcherPreferences());
			UtentiDAO.getInstance().modificaPreferenzeConfronti(pref,
					loginBean.getUsername());
		} else {

			sendErrorMessageToUser("IMPOSSIBILE CAMBIARE PREFERENZE CONFRONTI",
					"Deve esserci almeno un criterio");

			rBean.setMatcherPreferences(loginBean.getLoggedUser()
					.matcherPreferencesToListString());
			mOut = (ArrayList<Matcher>) list;
		}

		return mOut;
	}

	public List<Matcher> filterMillionvltLocMatcher(ArrayList<Matcher> list,
			ReportBean rBean) {

		InfoPageBean infoPageBean = findBean("infoPageBean");

		if (rBean.isModExcludeMillionVlt()) {
			infoPageBean.addDettagliFiltri("VLT MILIONARIE ESCLUSE");

			List<Matcher> mTot = list;

			List<Matcher> mOut = new ArrayList<Matcher>();

			for (Matcher mSum : mTot) {
				if (!mSum.getMSogei().isMilionarie())
					mOut.add(mSum);
			}

			return mOut;
		} else {
			infoPageBean.removeDettagliFiltri("VLT MILIONARIE ESCLUSE");

			return list;

		}

	}

	public void handleFilter(FilterEvent f) {
		ReportBean reportBean = findBean("reportBean");
		reportBean.setTooManyRecords(filteredTableList.size());
		setFilteredTotals();
	}

}
