package it.betplus.birs.controller.tables;

import it.betplus.birs.controller.UserBean;
import it.betplus.birs.db.dao.UtentiDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.Utente;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean(name = "tableBeanUser")
@SessionScoped
public class TableBeanUser extends BaseTableBean {

	private static final long serialVersionUID = -7127998751959747683L;

	private List<Utente> tableList;
	private Utente userGet;
	private List<String> avaliableRole;
	private SelectItem[] emailLevelOptions;

	public TableBeanUser() throws DataLayerException {
		tableList = new ArrayList<Utente>();
	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		try {

			List<Utente> m = UtentiDAO.getInstance().getUtenti();
			setTableList(m);
			List<String> r = UtentiDAO.getInstance().getRuoli();
			setAvaliableRole(r);
			createEmailLevelOptions();

		} catch (DataAccessException e) {

			throw new DataLayerException(e.getMessage());

		}

	}

	public List<Utente> getTableList() {
		return tableList;
	}

	public void setTableList(List<Utente> tableList) {
		this.tableList = tableList;
	}

	public List<String> getAvaliableRole() {
		return avaliableRole;
	}

	public void setAvaliableRole(List<String> avaliableRole) {
		this.avaliableRole = avaliableRole;
	}

	public void onRemove() {
		try {
			UtentiDAO.getInstance().eliminaUtente(userGet.getUsername());
			sendInfoMessageToUser("CANCELLAZIONE UTENTE",
					"Utente cancellato con successo");
			List<Utente> m = UtentiDAO.getInstance().getUtenti();
			setTableList(m);
		} catch (DataAccessException e) {
			sendErrorMessageToUser("CANCELLAZIONE UTENTE",
					"Errore nella cancellatzione utente");
		}

	}

	public void onEdit() {
		try {
			UtentiDAO.getInstance().modificaUtente(userGet,
					userGet.getUsername());
			sendInfoMessageToUser("MODIFICA UTENTE",
					"Utente modificato con successo");
		} catch (DataAccessException e) {
			sendErrorMessageToUser("MODIFICA UTENTE",
					"Errore nella modifica utente");
		}

	}

	public void onCreate() {
		UserBean userBean = findBean("userBean");
		Utente ut = new Utente(userBean.getUsername(), userBean.getUserPass(),
				userBean.getNome(), userBean.getCognome(), userBean.getRuolo(),
				userBean.getMatcherPreferences(), userBean.getEmail(),
				userBean.getEmailPreferences());
		int exitCode = 0;
		try {
			exitCode = UtentiDAO.getInstance().inserisciNuovoUtente(ut);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (exitCode != 0) {
			sendInfoMessageToUser("INSERIMENTO UTENTE",
					"Utente inserito con successo");
			List<Utente> m;
			try {
				m = UtentiDAO.getInstance().getUtenti();
				setTableList(m);
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		} else
			sendErrorMessageToUser("INSERIMENTO UTENTE",
					"Errore Utente non inserito");
	}

	public Utente getUserGet() {
		return userGet;
	}

	public void setUserGet(Utente userGet) {
		this.userGet = userGet;
	}

	private void createEmailLevelOptions() {
		emailLevelOptions = new SelectItem[4];

		emailLevelOptions[0] = new SelectItem(0, "Non Impostate");
		emailLevelOptions[1] = new SelectItem(1, "Sistema");
		emailLevelOptions[2] = new SelectItem(2, "Contabili");
		emailLevelOptions[3] = new SelectItem(3, "Tutte");
	}

	public SelectItem[] getEmailLevelOptions() {
		return emailLevelOptions;
	}

	public void setEmailLevelOptions(SelectItem[] emailLevelOptions) {
		this.emailLevelOptions = emailLevelOptions;
	}


}
