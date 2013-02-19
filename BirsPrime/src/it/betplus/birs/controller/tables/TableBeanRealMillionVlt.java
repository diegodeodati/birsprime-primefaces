package it.betplus.birs.controller.tables;

import it.betplus.birs.controller.sheets.ReportBean;
import it.betplus.birs.db.dao.VltMilionarieDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.RealMillionvlt;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "tableBeanRealMillionVlt")
@SessionScoped
public class TableBeanRealMillionVlt extends BaseTableBean {

	private static final long serialVersionUID = 7627376406454930583L;

	private List<RealMillionvlt> tableList;
	private RealMillionvlt selectedObj;
	private RealMillionvlt[] selectedObjs;

	public TableBeanRealMillionVlt() throws DataLayerException {
		tableList = new ArrayList<RealMillionvlt>();
	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		ReportBean reportBean = findBean("reportBean");

		try {

			tableList = VltMilionarieDAO.getInstance().getAllVltMilionarieDay(reportBean.getDataM());

		} catch (DataAccessException e) {

			throw new DataLayerException(e.getMessage());

		}

	}

	public List<RealMillionvlt> getTableList() {
		return tableList;
	}

	public void setTableList(List<RealMillionvlt> tableList) {
		this.tableList = tableList;
	}

	public RealMillionvlt getSelectedObj() {
		return selectedObj;
	}

	public void setSelectedObj(RealMillionvlt selectedObj) {
		this.selectedObj = selectedObj;
	}

	public RealMillionvlt[] getSelectedObjs() {
		return selectedObjs;
	}

	public void setSelectedObjs(RealMillionvlt[] selectedObjs) {
		this.selectedObjs = selectedObjs;
	}

}
