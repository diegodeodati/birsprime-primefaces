package it.betplus.birs.controller.charts;

import it.betplus.birs.controller.sheets.ReportBean;
import it.betplus.birs.db.dao.MeterDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.MeterfactVlt;
import it.betplus.web.framework.managedbeans.ChartsManagerBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

@ManagedBean(name = "vltChartBean")
@SessionScoped
public class VltChartBean extends ChartsManagerBean {

	private static final long serialVersionUID = 8368155335338625114L;

	private CartesianChartModel vltChartModel;

	List<MeterfactVlt> meters = new ArrayList<MeterfactVlt>();

	private Double maxValue;

	public VltChartBean() {
		super();
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public CartesianChartModel getChartModel() {
		return vltChartModel;
	}

	@Override
	public String refreshChartAction(String chartName) {
		ReportBean rBean = findBean("reportBean");
		log.debug(rBean.getSelectedLocation());
		log.debug(rBean.getSelectedVlt());

		String code_location = rBean.getSelectedLocation().split("-")[0].trim();

		if (code_location == null || code_location.equals("NO-LOC-ID")
				|| rBean.getSelectedVlt().equals("NO-VLT-ID")) {
			sendErrorMessageToUser("ERRORE LOCATION / VLT",
					"Nessuna Location / Vlt selezionata");
			createChartVlt();

			String redirect = ((HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest())
					.getRequestURI();

			log.info(redirect);

			if (redirect.contains("/BirsPrime/pages/chart/chart-vlt.xhtml"))
				return "";
			else
				return "chart-vlt";

		} else {
			loadMeterFactVlt();
			createChartVlt();
			return "chart-vlt";
		}

	}

	private void loadMeterFactVlt() {
		ReportBean rBean = findBean("reportBean");

		try {
			meters = MeterDAO.getInstance().retrieveMeter_vlt(rBean.getDataS(),
					rBean.getDataE(), rBean.getSelectedVlt());
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

	}

	private void createChartVlt() {
		maxValue = 0d;
		ArrayList<ChartSeries> series = new ArrayList<ChartSeries>();
		ChartSeries vltBet = new ChartSeries();
		ChartSeries vltWin = new ChartSeries();
		ChartSeries vltHouseWin = new ChartSeries();

		if (meters.size() > 0) {
			vltBet.setLabel(meters.get(0).getSistemagiocodim().getVlt()
					.getAAMS_VLT_ID()
					+ " - BET");
			vltWin.setLabel(meters.get(0).getSistemagiocodim().getVlt()
					.getAAMS_VLT_ID()
					+ " - WIN");
			vltHouseWin.setLabel(meters.get(0).getSistemagiocodim().getVlt()
					.getAAMS_VLT_ID()
					+ " - HOUSEWIN");
		}

		for (MeterfactVlt m : meters) {

			vltBet.set(m.getTempodim().getDay() + "/"
					+ m.getTempodim().getMonth(), m.getBet());
			vltWin.set(m.getTempodim().getDay() + "/"
					+ m.getTempodim().getMonth(), m.getWin());
			vltHouseWin.set(m.getTempodim().getDay() + "/"
					+ m.getTempodim().getMonth(), m.getHouseWin());

			if (maxValue <= m.getBet())
				maxValue = m.getBet();
		}

		series.add(vltBet);
		series.add(vltWin);
		series.add(vltHouseWin);

		try {

			createOrUpdateChart("lineVltChart", series);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
