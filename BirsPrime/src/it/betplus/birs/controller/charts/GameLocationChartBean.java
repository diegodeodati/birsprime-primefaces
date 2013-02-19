package it.betplus.birs.controller.charts;

import it.betplus.birs.controller.sheets.ReportBean;
import it.betplus.birs.db.dao.MeterDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.MeterfactGame;
import it.betplus.web.framework.managedbeans.ChartsManagerBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.ChartSeries;

@ManagedBean(name = "gameLocationChartBean")
@SessionScoped
public class GameLocationChartBean extends ChartsManagerBean {

	private static final long serialVersionUID = -6171698541953640768L;

	List<MeterfactGame> meters = new ArrayList<MeterfactGame>();

	private boolean changeGioco = true;

	private Double maxValue = 0d;

	private int internalRangeData = 0;

	public GameLocationChartBean() {
		super();
	}

	
	@Override
	public String refreshChartAction(String chartName) {
		ReportBean rBean = findBean("reportBean");
		
		String code_location=rBean.getSelectedLocation().split("-")[0].trim();
		if (code_location == null
				|| code_location.equals("NO-LOC-ID")) {
			sendErrorMessageToUser("ERRORE LOCATION","Nessuna Location selezionata");
			createChartModel();
			
			String redirect = ((HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest())
					.getRequestURI();

			if (redirect
					.contains("/BirsPrime/pages/chart/chart-game-location.xhtml"))
				return "";
			else
				return "chart-game-location";
		} else {

			loadMeterFactGame();
			createChartModel();
			return "chart-game-location";
		}
		
	}

	public void itemSelect(ItemSelectEvent event) {
		MeterfactGame mAct = meters
				.get((event.getSeriesIndex() * internalRangeData)
						+ event.getItemIndex());
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"GIOCO SELEZIONATO", mAct.getSistemagiocodim().getGame()
						.getNAME()
						+ ", Bet: " + mAct.getBet());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	/*
	 * public String refreshChartModel() { createChartModel(); return
	 * "chart-game"; }
	 */

	private void loadMeterFactGame() {
		ReportBean rBean = findBean("reportBean");
		meters = new ArrayList<MeterfactGame>();
		try {
			String code_location=rBean.getSelectedLocation().split("-")[0].trim();
			
			if (code_location.equals("TUTTE"))
				meters = MeterDAO.getInstance().retrieveMeter_games_chart(
						rBean.getDataS(), rBean.getDataE(),rBean.getSelectedGameSystem());
			else if (!code_location.equals("NO-LOC-ID"))
				meters = MeterDAO.getInstance()
						.retrieveMeter_games_chart_by_location(
								rBean.getDataS(), rBean.getDataE(),
								code_location,rBean.getSelectedGameSystem());
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

	}

	private void createChartModel() {
		maxValue = 0d;
		ChartSeries gioco = new ChartSeries();
		ArrayList<ChartSeries> series = new ArrayList<ChartSeries>();

		int i = 0;

		for (MeterfactGame m : meters) {

			if (changeGioco)
				gioco = new ChartSeries();
			gioco.setLabel(m.getSistemagiocodim().getGame().getNAME());

			gioco.set(m.getTempodim().getDay() + "/"
					+ m.getTempodim().getMonth(), m.getBet());

			if (meters.indexOf(m) < meters.size() - 1) {
				if (m.getSistemagiocodim().getGame().getAAMS_GAME_ID() != meters
						.get(meters.indexOf(m) + 1).getSistemagiocodim()
						.getGame().getAAMS_GAME_ID()) {
					series.add(gioco);
					changeGioco = true;
					internalRangeData = ++i;
					i = 0;
				} else {
					changeGioco = false;
					++i;
				}
			} else {
				series.add(gioco);
				changeGioco = true;
			}

			if (maxValue <= m.getBet())
				maxValue = m.getBet();
		}

		try {
			createOrUpdateChart("lineGameLocationChart", series);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
