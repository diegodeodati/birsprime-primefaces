package it.betplus.birs.controller.sheets;

import it.betplus.birs.controller.LoginBeanBirs;
import it.betplus.birs.controller.tables.TableBeanGame;
import it.betplus.birs.controller.tables.TableBeanGroupedGame;
import it.betplus.birs.controller.tables.TableBeanLocation;
import it.betplus.birs.controller.tables.TableBeanLocationGame;
import it.betplus.birs.controller.tables.TableBeanLocationHour;
import it.betplus.birs.controller.tables.TableBeanMatcherLocation;
import it.betplus.birs.controller.tables.TableBeanMatcherVlt;
import it.betplus.birs.controller.tables.TableBeanProbMillionVlt;
import it.betplus.birs.controller.tables.TableBeanRealMillionVlt;
import it.betplus.birs.controller.tables.TableBeanRegion;
import it.betplus.birs.controller.tables.TableBeanUser;
import it.betplus.birs.controller.tables.TableBeanVlt;
import it.betplus.birs.controller.tables.TableBeanVltDismissed;
import it.betplus.birs.controller.tables.TableBeanVltModel;
import it.betplus.birs.db.dao.LocationDAO;
import it.betplus.birs.db.dao.SistemagiocoDAO;
import it.betplus.birs.db.dao.VltDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.exception.DataLayerException;
import it.betplus.birs.primitive.Location;
import it.betplus.birs.primitive.Vlt;
import it.betplus.birs.util.IConstants;
import it.betplus.web.framework.managedbeans.GeneralBean;
import it.betplus.web.framework.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.DateSelectEvent;

@ManagedBean(name = "reportBean")
@SessionScoped
public class ReportBean extends GeneralBean {

	private boolean modSingTotDay;
	private boolean modAllOpenLoc;
	private boolean modExcludeMillionVlt;

	private List<String> matcherPreferences;

	private boolean tooManyRecords;
	private boolean errorDate = false;

	private Date dataS;
	private Date dataE;
	private Date dataM;

	private Date oggi = new Date();

	private boolean dateChanged = true;

	private int selectedRegion = -1;

	private String selectedGameSystem = "%";
	private SelectItem[] avaliableSistemaGioco = {};

	private String selectedLocation = "NO-LOC-ID";
	private List<Location> avaliableLocations = new ArrayList<Location>();

	private String selectedVlt = "NO-VLT-ID";
	private List<Vlt> avaliableVlts = new ArrayList<Vlt>();

	private boolean noLocations = true;
	private boolean noVlts = true;

	private SelectItem[] sistemagiocodimOptions = {};
	private SelectItem[] contabilityOptions;
	private int betOpt;

	public boolean isDateChanged() {
		return dateChanged;
	}

	public void setDateChanged(boolean dateChanged) {
		this.dateChanged = dateChanged;
	}

	// ************** SELECT FOR CUSTOM FILTER

	public int getSelectedRegion() {
		return selectedRegion;
	}

	public void setSelectedRegion(int selectedRegion) {
		this.selectedRegion = selectedRegion;
	}

	public String getSelectedLocation() {
		return selectedLocation;
	}

	public void setSelectedLocation(String selectedLocation) {
		this.selectedLocation = selectedLocation;
	}

	public void populateAvaliableLocations() {

		if (dateChanged) {
			try {

				Location actualSelectedLoc = new Location();
				actualSelectedLoc.setAAMS_LOCATION_ID(selectedLocation);

				avaliableLocations.clear();

				avaliableLocations = LocationDAO.getInstance()
						.retrieve_locations(dataS, dataE);

				if (!avaliableLocations.contains(actualSelectedLoc)) {
					selectedLocation = "NO-LOC-ID";
					selectedVlt = "NO-VLT-ID";
					avaliableVlts.clear();
				}

			} catch (DataAccessException e) {
				e.printStackTrace();
			}
			dateChanged = false;
		}

		if (avaliableLocations.size() == 0)
			noLocations = true;
		else
			noLocations = false;
	}

	public List<Location> getAvaliableLocations() {
		return avaliableLocations;
	}

	public String getSelectedGameSystem() {
		return selectedGameSystem;
	}

	public void setSelectedGameSystem(String selectedGameSystem) {
		this.selectedGameSystem = selectedGameSystem;
	}

	public SelectItem[] getAvaliableSistemaGioco() {
		return avaliableSistemaGioco;
	}

	public void setAvaliableSistemaGioco(SelectItem[] avaliableSistemaGioco) {
		this.avaliableSistemaGioco = avaliableSistemaGioco;
	}

	public void setAvaliableLocations(List<Location> avaliableLocations) {
		this.avaliableLocations = avaliableLocations;
	}

	public String getSelectedVlt() {
		return selectedVlt;
	}

	public void setSelectedVlt(String selectedVlt) {
		this.selectedVlt = selectedVlt;
	}

	public void populateAvaliableVlts() {

		String code_location=selectedLocation.split("-")[0].trim();
		
		if (selectedLocation != "" && selectedLocation != null
				&& !selectedLocation.equals("NO-LOC-ID"))
			try {
				avaliableVlts.clear();
				avaliableVlts = VltDAO.getInstance().retrieve_vlts(dataE,
						code_location);

				selectedVlt = "NO-VLT-ID";

			} catch (DataAccessException e) {
				e.printStackTrace();
			}

		if (avaliableVlts.size() == 0)
			noVlts = true;
		else
			noVlts = false;

	}

	public List<Vlt> getAvaliableVlts() {
		return avaliableVlts;
	}

	public void setAvaliableVlts(List<Vlt> avaliableVlts) {
		this.avaliableVlts = avaliableVlts;
	}

	public void refreshVlts() {
		populateAvaliableVlts();

	}

	public boolean isNoLocations() {
		return noLocations;
	}

	public void setNoLocations(boolean noLocations) {
		this.noLocations = noLocations;
	}

	public boolean isNoVlts() {
		return noVlts;
	}

	public void setNoVlts(boolean noVlts) {
		this.noVlts = noVlts;
	}

	public List<Location> completeLocation(String query) {
		List<Location> results = new ArrayList<Location>();

		for (Location loc : avaliableLocations) {
			if (loc.getCOMMERCIAL_NAME().contains(query.toUpperCase())
					|| loc.getAAMS_LOCATION_ID().contains(query.toUpperCase())) {
				results.add(loc);
			}
		}

		return results;
	}

	public List<Location> completeLocationWithAll(String query) {
		List<Location> results = new ArrayList<Location>();

		Location locAll = new Location();
		locAll.setCOMMERCIAL_NAME("TUTTE LE SALE");
		locAll.setAAMS_LOCATION_ID("%");

		results.add(locAll);
		for (Location loc : avaliableLocations) {
			if (loc.getCOMMERCIAL_NAME().contains(query.toUpperCase())
					|| loc.getAAMS_LOCATION_ID().contains(query.toUpperCase())) {
				results.add(loc);
			}
		}

		return results;
	}

	public List<Vlt> completeVlt(String query) {
		List<Vlt> results = new ArrayList<Vlt>();

		for (Vlt vlt : avaliableVlts) {
			if (vlt.getAAMS_VLT_ID().contains(query.toUpperCase())) {
				results.add(vlt);
			}
		}

		return results;
	}

	// *********************

	public boolean isErrorDate() {
		return errorDate;
	}

	public void setErrorDate(boolean errorDate) {
		this.errorDate = errorDate;
	}

	public boolean isTooManyRecords() {
		return tooManyRecords;
	}

	public void setTooManyRecords(int size) {
		if (size > 60000) {
			tooManyRecords = true;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_FATAL,
									"EXPORT DISABILITATO",
									"Limite Record per Export raggiunto. Si consiglia di effettuare il Totale Giorni"));
		} else
			tooManyRecords = false;
	}

	
	public ReportBean() throws DataLayerException {
		dataS = new Date();
		dataE = new Date();
		dataM = new Date();
		modSingTotDay = false;
		modAllOpenLoc = false;
		
		tooManyRecords = false;
		

		LoginBeanBirs loginBean = findBean("loginBean");

		try {
			populateAvaliableLocations();
			createFilterOptions();
			// avaliableLocations =
			// LocationDAO.getInstance().retrieve_locations(dataS, dataE);
			avaliableSistemaGioco = SistemagiocoDAO.getInstance()
					.retrieve_sistema_gioco(false);
			sistemagiocodimOptions = SistemagiocoDAO.getInstance()
					.retrieve_sistema_gioco(true);
			matcherPreferences = loginBean.getLoggedUser()
					.matcherPreferencesToListString();

		} catch (DataAccessException e) {
			e.printStackTrace();
		}

	}

	public void setDataS(Date ds) {
		dataS = ds;
	}

	public void setDataE(Date de) {
		dataE = de;
	}

	public Date getDataS() {
		return dataS;
	}

	public Date getDataE() {
		return dataE;
	}

	public Date getDataM() {
		return dataM;
	}

	public void setDataM(Date dataM) {
		this.dataM = dataM;
	}

	public String getDataStoString() {
		return DateUtils.dateToString(dataS, "dd/MM/yyyy");
	}

	public String getDataEtoString() {
		return DateUtils.dateToString(dataE, "dd/MM/yyyy");
	}

	public String getDataMtoString() {
		return DateUtils.dateToString(dataM, "dd/MM/yyyy");
	}

	public void changeDataS(DateSelectEvent event) {
		dateChanged = true;
		if (DateUtils.isDateAfter(event.getDate(), dataE)
				&& !DateUtils.isDateEquals(event.getDate(), dataE)) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"ERRORE DATA", DateUtils.dateToString(
									event.getDate(), "dd/MM/yyyy")
									+ " successiva a "
									+ DateUtils.dateToString(dataE,
											"dd/MM/yyyy")));
			errorDate = true;
		} else {
			errorDate = false;
			populateAvaliableLocations();
		}
	}

	public void changeDataE(DateSelectEvent event) {
		dateChanged = true;
		if (DateUtils.isDateAfter(dataS, event.getDate())
				&& !DateUtils.isDateEquals(dataS, event.getDate())) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"ERRORE DATA", DateUtils.dateToString(dataS,
									"dd/MM/yyyy")
									+ " successiva a "
									+ DateUtils.dateToString(event.getDate(),
											"dd/MM/yyyy")));
			errorDate = true;
		} else {
			errorDate = false;
			populateAvaliableLocations();
		}
	}

	public boolean isModSingTotDay() {
		return modSingTotDay;
	}

	public void setModSingTotDay(boolean mst) {		
		this.modSingTotDay = mst;
	}

	
	public boolean isModAllOpenLoc() {
		return modAllOpenLoc;
	}

	public void setModAllOpenLoc(boolean maol) {
		this.modAllOpenLoc = maol;
	}

	public boolean isModExcludeMillionVlt() {
		return modExcludeMillionVlt;
	}

	public void setModExcludeMillionVlt(boolean modExcludeMillionVlt) {
		this.modExcludeMillionVlt = modExcludeMillionVlt;
	}


	public List<String> getMatcherPreferences() {
		return matcherPreferences;
	}

	public void setMatcherPreferences(List<String> matcherPreferences)
			throws DataAccessException {
		this.matcherPreferences = matcherPreferences;
	}

	public String filterUser() throws DataAccessException {

		TableBeanUser tabBeanUser = findBean("tableBeanUser");
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		tabBeanUser.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/BirsPrime/pages/report/user.xhtml"))
			return "";
		else
			return "user";

	}

	public String filterVltModel() throws DataAccessException {

		TableBeanVltModel tabBeanVltModel = findBean("tableBeanVltModel");
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		tabBeanVltModel.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/BirsPrime/pages/report/vlt-model.xhtml"))
			return "";
		else
			return "vlt-model";

	}

	public String filterUploadImgGame() throws DataAccessException {

		TableBeanUser tabBeanUser = findBean("tableBeanUser");
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		tabBeanUser.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/BirsPrime/pages/report/upload-img-game.xhtml"))
			return "";
		else
			return "upload-img-game";

	}

	public String filterMillionVlt() throws DataAccessException {
		TableBeanProbMillionVlt tabBeanProbMillionVlt = findBean("tableBeanProbMillionVlt");
		TableBeanRealMillionVlt tabBeanRealMillionVlt = findBean("tableBeanRealMillionVlt");

		ArrayList<Object> arrayParams = new ArrayList<Object>();
		tabBeanProbMillionVlt.populateTable(arrayParams);
		tabBeanRealMillionVlt.populateTable(arrayParams);

		modSingTotDay = false;
		modAllOpenLoc = false;
		betOpt = 0;

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/BirsPrime/pages/report/million-vlt.xhtml"))
			return "";
		else
			return "million-vlt";

	}

	public String filterDataMatcherOnVlt() throws DataAccessException {
		TableBeanMatcherVlt tabBeanMatcherVlt = findBean("tableBeanMatcherVlt");

		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add("17110000%");
		tabBeanMatcherVlt.populateTable(arrayParams);

		setTooManyRecords(tabBeanMatcherVlt.getTableList().size());

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect
				.contains("/BirsPrime/pages/report/matcher-sogei-600-vlt.xhtml"))
			return "";
		else
			return "matcher-vlt";

	}

	public String filterDataMatcherOnLocation() throws DataAccessException {
		TableBeanMatcherLocation tabBeanMatcherLocation = findBean("tableBeanMatcherLocation");

		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add("17110000%");
		tabBeanMatcherLocation.populateTable(arrayParams);
		
		setTooManyRecords(tabBeanMatcherLocation.getTableList().size());

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect
				.contains("/BirsPrime/pages/report/matcher-sogei-600-location.xhtml"))
			return "";
		else
			return "matcher-location";

	}

	public String filterDataOnLocationHour() throws DataAccessException {
		TableBeanLocationHour tabBeanLocationHour = findBean("tableBeanLocationHour");

		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add("17110000%");
		tabBeanLocationHour.populateTable(arrayParams);

		setTooManyRecords(tabBeanLocationHour.getTableList().size());

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect
				.contains("/BirsPrime/pages/report/general/general-location-hour.xhtml"))
			return "";
		else
			return "general-location-hour";

	}

	public String filterDataOnLocationHourInspired() throws DataAccessException {
		TableBeanLocationHour tabBeanLocationHour = findBean("tableBeanLocationHour");

		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(String.valueOf(IConstants.SYS_GIOCO_INSPIRED));
		tabBeanLocationHour.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect
				.contains("/BirsPrime/pages/report/inspired/inspired-location-hour.xhtml"))
			return "";
		else
			return "inspired-location-hour";

	}

	public String filterDataOnLocationHourNovomatic()
			throws DataAccessException {
		TableBeanLocationHour tabBeanLocationHour = findBean("tableBeanLocationHour");

		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(String.valueOf(IConstants.SYS_GIOCO_NOVOMATIC));
		tabBeanLocationHour.populateTable(arrayParams);


		setTooManyRecords(tabBeanLocationHour.getTableList().size());

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect
				.contains("/BirsPrime/pages/report/novomatic/novomatic-location-hour.xhtml"))
			return "";
		else
			return "novomatic-location-hour";

	}

	public String filterDataOnLocation() throws DataAccessException {
		TableBeanLocation tabBeanLocation = findBean("tableBeanLocation");

		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add("17110000%");
		tabBeanLocation.populateTable(arrayParams);

		setTooManyRecords(tabBeanLocation.getTableList().size());

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect
				.contains("/BirsPrime/pages/report/general/general-location.xhtml"))
			return "";
		else
			return "general-location";

	}

	public String filterDataOnLocationNovomatic() throws DataAccessException {
		TableBeanLocation tabBeanLocation = findBean("tableBeanLocation");

		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(String.valueOf(IConstants.SYS_GIOCO_NOVOMATIC));
		tabBeanLocation.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect
				.contains("/BirsPrime/pages/report/novomatic/novomatic-location.xhtml"))
			return "";
		else
			return "novomatic-location";
		// return "general-location.xhtml?faces-redirect=true";

	}

	public String filterDataOnLocationInspired() throws DataAccessException {
		TableBeanLocation tabBeanLocation = findBean("tableBeanLocation");

		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(String.valueOf(IConstants.SYS_GIOCO_INSPIRED));
		tabBeanLocation.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect
				.contains("/BirsPrime/pages/report/inspired/inspired-location.xhtml"))
			return "";
		else
			return "inspired-location";

	}

	public String filterDataOnVltDismissed() throws DataAccessException {
		TableBeanVltDismissed tabBeanVltDismissed = findBean("tableBeanVltDismissed");

		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add("17110000%");
		tabBeanVltDismissed.populateTable(arrayParams);

		setTooManyRecords(tabBeanVltDismissed.getTableList().size());

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		if (redirect
				.contains("/BirsPrime/pages/report/general/general-vlt-dismissed.xhtml"))
			return "";
		else
			return "general-vlt-dismissed";
		// return "general-vlt.xhtml?faces-redirect=true";

	}

	public String filterDataOnVlt() throws DataAccessException {
		TableBeanVlt tabBeanVlt = findBean("tableBeanVlt");

		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add("17110000%");
		tabBeanVlt.populateTable(arrayParams);


		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		if (redirect
				.contains("/BirsPrime/pages/report/general/general-vlt.xhtml"))
			return "";
		else
			return "general-vlt";
		// return "general-vlt.xhtml?faces-redirect=true";

	}

	public String filterDataOnVltNovomatic() throws DataAccessException {
		TableBeanVlt tabBeanVlt = findBean("tableBeanVlt");

		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(String.valueOf(IConstants.SYS_GIOCO_NOVOMATIC));
		tabBeanVlt.populateTable(arrayParams);

		setTooManyRecords(tabBeanVlt.getTableList().size());

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		if (redirect
				.contains("/BirsPrime/pages/report/novomatic/novomatic-vlt.xhtml"))
			return "";
		else
			return "novomatic-vlt";

	}

	public String filterDataOnVltInspired() throws DataAccessException {
		TableBeanVlt tabBeanVlt = findBean("tableBeanVlt");

		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(String.valueOf(IConstants.SYS_GIOCO_INSPIRED));
		tabBeanVlt.populateTable(arrayParams);

		setTooManyRecords(tabBeanVlt.getTableList().size());

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		if (redirect
				.contains("/BirsPrime/pages/report/inspired/inspired-vlt.xhtml"))
			return "";
		else
			return "inspired-vlt";

	}

	public String filterDataOnGroupedGame() throws DataAccessException {
		TableBeanGroupedGame tabBeanGroupedGame = findBean("tableBeanGroupedGame");

		// genero la nuova lista ma non la salvo nel modello per permettere il
		// reset
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add("17110000%");
		tabBeanGroupedGame.populateTable(arrayParams);


		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		if (redirect
				.contains("/BirsPrime/pages/report/general/general-grouped-game.xhtml"))
			return "";
		else
			return "general-grouped-game";
		// return "general-vlt.xhtml?faces-redirect=true";

	}
	
	public String filterDataOnGroupedGameNovomatic() throws DataAccessException {
		TableBeanGroupedGame tabBeanGroupedGame = findBean("tableBeanGroupedGame");

		// genero la nuova lista ma non la salvo nel modello per permettere il
		// reset
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(String.valueOf(IConstants.SYS_GIOCO_NOVOMATIC));
		tabBeanGroupedGame.populateTable(arrayParams);


		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		if (redirect
				.contains("/BirsPrime/pages/report/novomatic/novomatic-grouped-game.xhtml"))
			return "";
		else
			return "novomatic-grouped-game";
		// return "general-vlt.xhtml?faces-redirect=true";

	}
	
	
	public String filterDataOnGroupedGameInspired() throws DataAccessException {
		TableBeanGroupedGame tabBeanGroupedGame = findBean("tableBeanGroupedGame");

		// genero la nuova lista ma non la salvo nel modello per permettere il
		// reset
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(String.valueOf(IConstants.SYS_GIOCO_INSPIRED));
		tabBeanGroupedGame.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		if (redirect
				.contains("/BirsPrime/pages/report/inspired/inspired-grouped-game.xhtml"))
			return "";
		else
			return "inspired-grouped-game";
		// return "general-vlt.xhtml?faces-redirect=true";

	}
	
	
	public String filterDataOnGame() throws DataAccessException {
		TableBeanGame tabBeanGame = findBean("tableBeanGame");

		// genero la nuova lista ma non la salvo nel modello per permettere il
		// reset
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add("17110000%");
		tabBeanGame.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		if (redirect
				.contains("/BirsPrime/pages/report/general/general-game.xhtml"))
			return "";
		else
			return "general-game";
		// return "general-vlt.xhtml?faces-redirect=true";

	}

	public String filterDataOnGameNovomatic() throws DataAccessException {
		TableBeanGame tabBeanGame = findBean("tableBeanGame");

		// genero la nuova lista ma non la salvo nel modello per permettere il
		// reset
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(String.valueOf(IConstants.SYS_GIOCO_NOVOMATIC));
		tabBeanGame.populateTable(arrayParams);


		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		if (redirect
				.contains("/BirsPrime/pages/report/novomatic/novomatic-game.xhtml"))
			return "";
		else
			return "novomatic-game";
		// return "general-vlt.xhtml?faces-redirect=true";

	}

	public String filterDataOnGameInspired() throws DataAccessException {
		TableBeanGame tabBeanGame = findBean("tableBeanGame");

		// genero la nuova lista ma non la salvo nel modello per permettere il
		// reset
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(String.valueOf(IConstants.SYS_GIOCO_INSPIRED));
		tabBeanGame.populateTable(arrayParams);

		
		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		if (redirect
				.contains("/BirsPrime/pages/report/inspired/inspired-game.xhtml"))
			return "";
		else
			return "inspired-game";
		// return "general-vlt.xhtml?faces-redirect=true";

	}

	public String filterDataOnLocationGame() throws DataAccessException {
		TableBeanLocationGame tabBeanLocationGame = findBean("tableBeanLocationGame");

		// genero la nuova lista ma non la salvo nel modello per permettere il
		// reset
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add("17110000%");
		tabBeanLocationGame.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		if (redirect
				.contains("/BirsPrime/pages/report/general/general-location-game.xhtml"))
			return "";
		else
			return "general-location-game";
		// return "general-vlt.xhtml?faces-redirect=true";

	}

	public String filterDataOnLocationGameNovomatic()
			throws DataAccessException {
		TableBeanLocationGame tabBeanLocationGame = findBean("tableBeanLocationGame");

		// genero la nuova lista ma non la salvo nel modello per permettere il
		// reset
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(IConstants.SYS_GIOCO_NOVOMATIC);
		tabBeanLocationGame.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		if (redirect
				.contains("/BirsPrime/pages/report/novomatic/novomatic-location-game.xhtml"))
			return "";
		else
			return "novomatic-location-game";
		// return "general-vlt.xhtml?faces-redirect=true";

	}

	public String filterDataOnLocationGameInspired() throws DataAccessException {
		TableBeanLocationGame tabBeanLocationGame = findBean("tableBeanLocationGame");

		// genero la nuova lista ma non la salvo nel modello per permettere il
		// reset
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(IConstants.SYS_GIOCO_INSPIRED);
		tabBeanLocationGame.populateTable(arrayParams);

	
		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		if (redirect
				.contains("/BirsPrime/pages/report/inspired/inspired-location-game.xhtml"))
			return "";
		else
			return "inspired-location-game";
		// return "general-vlt.xhtml?faces-redirect=true";

	}

	public String filterDataOnRegion() throws DataAccessException {

		TableBeanRegion tabBeanRegion = findBean("tableBeanRegion");

		// genero la nuova lista ma non la salvo nel modello per permettere il
		// reset
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add("17110000%");
		tabBeanRegion.populateTable(arrayParams);


		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);
		if (redirect
				.contains("/BirsPrime/pages/report/general/general-region.xhtml"))
			return "";
		else
			return "general-region";
		// return "general-region.xhtml?faces-redirect=true";

	}

	public String filterDataOnRegionNovomatic() throws DataAccessException {

		TableBeanRegion tabBeanRegion = findBean("tableBeanRegion");

		// genero la nuova lista ma non la salvo nel modello per permettere il
		// reset
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(String.valueOf(IConstants.SYS_GIOCO_NOVOMATIC));
		tabBeanRegion.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);
		if (redirect
				.contains("/BirsPrime/pages/report/novomatic/novomatic-region.xhtml"))
			return "";
		else
			return "novomatic-region";
		// return "general-region.xhtml?faces-redirect=true";

	}

	public String filterDataOnRegionInspired() throws DataAccessException {

		TableBeanRegion tabBeanRegion = findBean("tableBeanRegion");

		// genero la nuova lista ma non la salvo nel modello per permettere il
		// reset
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(String.valueOf(IConstants.SYS_GIOCO_INSPIRED));
		tabBeanRegion.populateTable(arrayParams);

		modSingTotDay = false;
		modAllOpenLoc = false;
		betOpt = 0;

		setTooManyRecords(tabBeanRegion.getTableList().size());

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);
		if (redirect
				.contains("/BirsPrime/pages/report/inspired/inspired-region.xhtml"))
			return "";
		else
			return "inspired-region";

	}

	public String goToReimport() {
		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		System.out.println(redirect);
		if (redirect.contains("/BirsPrime/pages/reimport.xhtml"))
			return "";
		else
			return "reimport";

	}

	public String goToBi() {
		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();
		log.info(redirect);

		System.out.println(redirect);
		if (redirect.contains("/BirsPrime/pages/business-intelligence.xhtml"))
			return "";
		else
			return "business-intelligence";

	}

	/*
	 * FUNZIONE NON RICHIESTA public void resetData(){ TableBeanLocation
	 * tabBeanLocation = findBean("tableBeanLocation"); List<MeterfactLocation>
	 * m = tabBeanLocation.getMeterfactLocationDataModel().getData();
	 * tabBeanLocation.setMaterfactlocations(m);
	 * 
	 * dataS=oldDataS; dataE=oldDataE;
	 * 
	 * 
	 * modSingTot=false; }
	 */

	public Date getOggi() {
		return oggi;
	}


	public SelectItem[] getSistemagiocodimOptions() {
		return sistemagiocodimOptions;
	}

	public void setSistemagiocodimOptions(SelectItem[] sistemagiocodimOptions) {
		this.sistemagiocodimOptions = sistemagiocodimOptions;
	}

	public int getBetOpt() {
		return betOpt;
	}

	public void setBetOpt(int betOpt) {
		this.betOpt = betOpt;
	}

	private void createFilterOptions() {
		contabilityOptions = new SelectItem[4];

		contabilityOptions[0] = new SelectItem(0, "Tutti");
		contabilityOptions[1] = new SelectItem(1, "=0");
		contabilityOptions[2] = new SelectItem(2, ">0 <1000");
		contabilityOptions[3] = new SelectItem(3, ">=1000");
	}

	public SelectItem[] getContabilityOptions() {
		return contabilityOptions;
	}

	public void setContabilityOptions(SelectItem[] contabilityOptions) {
		this.contabilityOptions = contabilityOptions;
	}

}
