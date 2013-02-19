package it.betplus.birs.controller.charts;

import it.betplus.birs.controller.sheets.ReportBean;
import it.betplus.birs.db.dao.MeterDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.MeterfactRegion;
import it.betplus.web.framework.managedbeans.ChartsManagerBean;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.ChartSeries;

@ManagedBean(name = "regionChartBean")
@SessionScoped
public class RegionChartBean extends ChartsManagerBean {

	private static final long serialVersionUID = 7473014196174358563L;

	private boolean selectRegion;
	private Double maxValue = 0d;
	private Double minValue = 0d;

	private ArrayList<MeterfactRegion> meters = new ArrayList<MeterfactRegion>();
	private ArrayList<MeterfactRegion> summedRegionList = new ArrayList<MeterfactRegion>();

	public RegionChartBean() {
		super();
	}

	
	public void pieRegionChartItemSelect(ItemSelectEvent event) {

		log.info("REGION SELECTED: "+summedRegionList.get(event.getItemIndex()));		
		loadMeterFactRegion(summedRegionList.get(event.getItemIndex()).getSpaziodim().getRegione().getIdReg());
		createSelectedRegionLineChart();

		selectRegion = true;
	}

	@Override
	public String refreshChartAction(String chartName) {
		if(chartName.equals("pieRegionChart")){
			loadMeterFactRegions();
			createPieModel();
			return "chart-region";
		}
		
		return null;
	}
	
	private void createPieModel() {

		selectRegion = false;

		LinkedHashMap<String, Number> hRegion = new LinkedHashMap<String, Number>();

		try {

			for (MeterfactRegion mr : summedRegionList) {

				hRegion.put(mr.getSpaziodim().getRegione().getNome(),
						mr.getBet());

			}

			createOrUpdateChart("pieRegionChart", hRegion);
			
		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	private void createSelectedRegionLineChart() {
 
		ArrayList<ChartSeries> pieRegionChartSeries = new ArrayList<ChartSeries>();
		ChartSeries regionBet = new ChartSeries();
		ChartSeries regionWin = new ChartSeries();
		ChartSeries regionHouseWin = new ChartSeries();
		maxValue=0d;
		minValue=0d;

		if (meters.size() > 0) {

			regionBet.setLabel(meters.get(0).getSpaziodim().getRegione()
					.getNome()
					+ " - BET");
			regionWin.setLabel(meters.get(0).getSpaziodim().getRegione()
					.getNome()
					+ " - WIN");
			regionHouseWin.setLabel(meters.get(0).getSpaziodim().getRegione()
					.getNome()
					+ " - HOUSEWIN");

		}

		for (MeterfactRegion m : meters) {

			regionBet.set(m.getTempodim().getDay() + "/"
					+ m.getTempodim().getMonth(), m.getBet());
			regionWin.set(m.getTempodim().getDay() + "/"
					+ m.getTempodim().getMonth(), m.getWin());
			regionHouseWin.set(m.getTempodim().getDay() + "/"
					+ m.getTempodim().getMonth(), m.getHouseWin());

			if (maxValue <= m.getBet())
				maxValue = m.getBet();
			
		    if (minValue>= m.getHouseWin())
		    	minValue = m.getHouseWin();

		}

		pieRegionChartSeries.add(regionBet);
		pieRegionChartSeries.add(regionWin);
		pieRegionChartSeries.add(regionHouseWin);

		try {
			
			createOrUpdateChart("pieRegionSelectedChart", pieRegionChartSeries);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void loadMeterFactRegions() {

		ReportBean rBean = findBean("reportBean");

		try {

			meters = (ArrayList<MeterfactRegion>) MeterDAO.getInstance()
					.retrieveMeter_regions(rBean.getDataS(), rBean.getDataE(),
							"%");
			LinkedHashMap<Integer, MeterfactRegion> hRegion = new LinkedHashMap<Integer, MeterfactRegion>();

			for (MeterfactRegion m : meters) {

				if (hRegion.containsKey(m.getSpaziodim().getRegione()
						.getIdReg())) {

					MeterfactRegion mHash = hRegion.get(m.getSpaziodim()
							.getRegione().getIdReg());

					mHash.sumValue(m);

				} else {

					MeterfactRegion mHash = new MeterfactRegion();

					mHash = m.clone();
				
					hRegion.put(m.getSpaziodim().getRegione().getIdReg(), mHash);

				}

			}

			summedRegionList = new ArrayList<MeterfactRegion>(hRegion.values());

		} catch (DataAccessException e) {
			e.printStackTrace();
		}

	}

	public void loadMeterFactRegion(int idRegion) {

		ReportBean rBean = findBean("reportBean");

		try {

			meters = (ArrayList<MeterfactRegion>) MeterDAO.getInstance()
					.retrieveMeter_region(rBean.getDataS(), rBean.getDataE(),
							idRegion);

		} catch (DataAccessException e) {
			e.printStackTrace();
		}

	}

	public boolean isSelectRegion() {
		return selectRegion;
	}

	public void setSelectRegion(boolean selectRegion) {
		this.selectRegion = selectRegion;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Double getMinValue() {
		return minValue;
	}


	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}


	public ArrayList<MeterfactRegion> getMeters() {
		return meters;
	}

	public void setMeters(ArrayList<MeterfactRegion> meters) {
		this.meters = meters;
	}

	public ArrayList<MeterfactRegion> getSummedRegionList() {
		return summedRegionList;
	}

	public void setSummedRegionList(ArrayList<MeterfactRegion> summedRegionList) {
		this.summedRegionList = summedRegionList;
	}

	

}