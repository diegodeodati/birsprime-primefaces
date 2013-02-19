package it.betplus.birs.servlet;

import it.betplus.birs.util.IConstants;
import it.betplus.birs.util.MailerSender;
import it.betplus.web.framework.utils.DateUtils;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
 
public class ServletMailSSL implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		final ResourceBundle resource = ResourceBundle
				.getBundle(IConstants.ssl_key);
		MailerSender action = new MailerSender();
		Timer timer = new Timer();
		
		Date dateAct  = new Date();
		Date dateStartInizio = DateUtils.createDate(DateUtils.year(dateAct),DateUtils.month(dateAct),DateUtils.day(dateAct),8,0,0,0);
		long interval = 60 * 60 * 1000 * Long.parseLong(resource.getString("interval_hour")) ;		

		
		timer.schedule(action, dateStartInizio, interval);
		
	}
}