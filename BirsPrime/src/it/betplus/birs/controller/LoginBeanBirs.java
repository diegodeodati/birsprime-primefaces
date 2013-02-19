package it.betplus.birs.controller;

import it.betplus.birs.controller.sheets.ReportBean;
import it.betplus.birs.db.dao.UtentiDAO;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.Utente;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.LoginBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBeanBirs extends LoginBean {  
	  
	private static final long serialVersionUID = -7859104635092743231L;
	
	protected UserBean loggedUser;
	 
    public LoginBeanBirs() {			
		super();	
		configureOutcomes("general-location", "index");
	}

    //*** Business methods ***//
    @Override
    public Object getUserAuthenticationDTO(String username, String password) throws DataLayerException, Exception{
    	
    	Utente user = null;
    	
    	try {
    		
    		user = UtentiDAO.getInstance().getUtenteByUsernamePassword(username, password);
    		    		
    	} catch (DataAccessException dle) {
			// handle exception from Mysqlfacade
    		throw new DataLayerException(dle.getMessage());
		}
    	
    	return user;
    	
    }

    @Override
	public void resetLoginSession() {
    	
    	// remove user from session bean
		this.loggedUser = null;
		this.username = "";
		this.password = "";
		
    }
	
    @Override
    public String forwardToLoginOutcome() {
    	
    	try {
    		
			ReportBean reportBean = findBean("reportBean");
			return reportBean.filterDataOnLocation();
			
		} catch (DataAccessException e) {
			
			sendErrorMessageToUser("ERRORE LOGIN","UTENTE NON TROVATO");    
		//	log.error(e.getMessage());
		}
    	
    	return null;
    	
    }

    @Override
    public void setLoggedUserFromDTO(Object user) {
    	
    	loggedUser = new UserBean((Utente) user);
    	
	} 
    
   
    
    //*** get & set methods ***//
	public UserBean getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(UserBean loggedUser) {
		this.loggedUser = loggedUser;
	}  

}  
