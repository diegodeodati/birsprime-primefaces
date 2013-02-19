package it.betplus.birs.controller.tables;

import it.betplus.birs.db.dao.VltModelDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.MeterfactLocation;
import it.betplus.birs.primitive.VltModel;
import it.betplus.birs.primitive.VltModelBean;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "tableBeanVltModel")
@SessionScoped
public class TableBeanVltModel extends BaseTableBean{


	private static final long serialVersionUID = -7127998751959747683L;
	
	private List<VltModel> tableList;
	private VltModel vltModelGet;
	private List<VltModel> filteredTableList;

	public TableBeanVltModel() throws DataLayerException {
		tableList = new ArrayList<VltModel>();
	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		try {

			List<VltModel> list = VltModelDAO.getInstance().getVltModels();
			setTableList(list);

		} catch (DataAccessException e) {

			throw new DataLayerException(e.getMessage());

		}

	}

	public List<VltModel> getTableList() {
		return tableList;
	}

	public void setTableList(List<VltModel> tableList) {
		this.tableList = tableList;
	}



	

	public void onEdit() {
			try {
				VltModelDAO.getInstance().editVltModel(vltModelGet);
				sendInfoMessageToUser("MODIFICA MODELLO VLT", "Modello Vlt modificato con successo");
			} catch (DataAccessException e) {
				sendErrorMessageToUser("MODIFICA MODELLO VLT", "Errore nella modifica del Modello Vlt");
			}
			
	}
	
	public void onCreate(){
		VltModelBean vltModelBean = findBean("vltModelBean");
		VltModel vModel = new VltModel(vltModelBean.getAAMS_VLT_MODEL_CODE(),vltModelBean.getNAME(),
				vltModelBean.getAAMS_GAME_SYSTEM_CODE(), vltModelBean.getMANUFACTURER(), vltModelBean.getCABINET_TYPE(),
				vltModelBean.getPCT_SUPPLIER());
		int exitCode = 0;
		try {
			exitCode = VltModelDAO.getInstance().insertNewVltModel(vModel);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		if(exitCode!=0){
		sendInfoMessageToUser("INSERIMENTO MODELLO VLT", "Modello Vlt inserito con successo");
		
		List<VltModel> m;
		try {
			m = VltModelDAO.getInstance().getVltModels();
			setTableList(m);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}		
		}
		else
		sendErrorMessageToUser("INSERIMENTO MODELLO VLT", "Errore Modello Vlt non inserito");
	}

	public VltModel getVltModelGet() {
		return vltModelGet;
	}

	public void setVltModelGet(VltModel vltModelGet) {
		this.vltModelGet = vltModelGet;
	}

	/**
	 * @return the filteredTableList
	 */
	public List<VltModel> getFilteredTableList() {
		return filteredTableList;
	}

	/**
	 * @param filteredTableList the filteredTableList to set
	 */
	public void setFilteredTableList(List<VltModel> filteredTableList) {
		this.filteredTableList = filteredTableList;
	}
	
	

		

}
