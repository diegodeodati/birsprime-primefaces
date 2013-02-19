package it.betplus.birs.controller.tables;

import it.betplus.birs.controller.sheets.ReportBean;
import it.betplus.birs.db.dao.MeterDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.ProbMillionVlt;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "tableBeanProbMillionVlt")
@SessionScoped
public class TableBeanProbMillionVlt extends BaseTableBean {



	private static final long serialVersionUID = 7627376406454930583L;
	
	private List<ProbMillionVlt> tableList;
	private ProbMillionVlt selectedObj;
	private ProbMillionVlt[] selectedObjs;

	public TableBeanProbMillionVlt() throws DataLayerException {
		tableList = new ArrayList<ProbMillionVlt>();
	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		ReportBean reportBean = findBean("reportBean");

		try {

			tableList = MeterDAO.getInstance().retrieveMeter_vlt_milionarie(reportBean.getDataM());


		} catch (DataAccessException e) {

			throw new DataLayerException(e.getMessage());

		}

	}

	public List<ProbMillionVlt> getTableList() {
		return tableList;
	}

	public void setTableList(List<ProbMillionVlt> tableList) {
		this.tableList = tableList;
	}

	public ProbMillionVlt getSelectedObj() {
		return selectedObj;
	}

	public void setSelectedObj(ProbMillionVlt selectedObj) {
		this.selectedObj = selectedObj;
	}

	public ProbMillionVlt[] getSelectedObjs() {
		return selectedObjs;
	}

	public void setSelectedObjs(ProbMillionVlt[] selectedObjs) {
		this.selectedObjs = selectedObjs;
	}


}
