package it.betplus.birs.util;

import it.betplus.birs.controller.tables.TableBeanMatcherVlt;
import it.betplus.birs.db.dao.LogDAO;
import it.betplus.birs.db.dao.SeicentoDAO;
import it.betplus.birs.db.dao.UtentiDAO;
import it.betplus.birs.primitive.Log;
import it.betplus.birs.primitive.Matcher;
import it.betplus.birs.primitive.MeterfactLocation;
import it.betplus.birs.primitive.MeterfactVlt;
import it.betplus.web.framework.utils.DateUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TimerTask;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailerSender extends TimerTask {
	final ResourceBundle resource = ResourceBundle
			.getBundle(IConstants.ssl_key);

	public MailerSender() {
	}

	@Override
	public void run() {

		if (Boolean.parseBoolean(resource.getString("send_auto_email"))) {
			try {
				javax.mail.internet.InternetAddress[] recipientsList = UtentiDAO
						.getInstance().getEmailByNotification(1);

				generateAndSendEmail(
						recipientsList,
						generateTableMatcher(Matcher.headerToArrayList(),
								TableBeanMatcherVlt.generateMatchingVlt(
										DateUtils.addDays(new java.util.Date(),
												-1), DateUtils.addDays(
												new java.util.Date(), -1))),
						"Confronti", "High", DateUtils.addDays(
								new java.util.Date(), -1));

				generateAndSendEmail(
						recipientsList,
						generateTableTotalGameSystem(
								MeterfactLocation.headerToArrayListNoLoc(),
								SeicentoDAO.getInstance()
										.retrieveTotalMeterList600_system(
												DateUtils.addDays(
														new java.util.Date(),
														-1))),
						"Totali Sistemi Gioco 600", "High", DateUtils.addDays(
								new java.util.Date(), -1));

				generateAndSendEmail(
						recipientsList,
						generateTableDailyLogs(
								Log.headerToArrayList(),
								LogDAO.getInstance().retrieve_logByDate(
										new java.util.Date())),
						"Stato Import Odierno", "High", new Date());

			} catch (MessagingException e) {
				throw new RuntimeException(e);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void generateAndSendEmail(
			javax.mail.internet.InternetAddress[] recipientsList,
			String content, String subject, String priority, Date d)
			throws RuntimeException, AddressException, MessagingException {
		Session session = generateConfigAndProperties();

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("info@betplus.it"));
		message.setRecipients(Message.RecipientType.TO, recipientsList);
		message.setHeader("X-Priority", priority);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		message.setSubject(subject + " " + sdf.format(d));
		message.setContent(content, "text/html");

		Transport.send(message);

	}

	private DecimalFormat generateDecimalFormatter() {
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator(',');
		decimalFormatSymbols.setGroupingSeparator('.');
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00",
				decimalFormatSymbols);
		return decimalFormat;
	}

	private Session generateConfigAndProperties() {
		Properties props = new Properties();

		props.put("mail.smtp.host", resource.getString("smtp"));
		props.put("mail.smtp.socketFactory.port", resource.getString("port"));
		// props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", resource.getString("smtp_auth"));
		props.put("mail.smtp.port", resource.getString("port"));

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(resource
								.getString("mail_user"), resource
								.getString("mail_password"));
					}
				});
		return session;
	}

	private String generateTableMatcher(List<String> hList, List<Matcher> mList) {
		String table = "<img style='float:right;' src=\"http://10.0.140.30/BirsPrime/images/logo.png\"><table style='border:1px solid black;'><tr style='background-color:#1484E6;'>";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		for (String s : hList) {
			table += "<th style='color:white;'>" + s;
		}

		table += "</tr>";

		int i = 0;
		String internalClass;

		for (Matcher m : mList) {

			if (i % 2 == 0) {
				internalClass = "#BCDCF5";
			} else {
				internalClass = "#FFFFFF";
			}

			table = table + "<tr style='background:" + internalClass
					+ ";'><td style='padding: 5px;white-space:nowrap;'>"
					+ sdf.format(m.getTempodim().getData())
					+ "<td style='padding: 5px;'>"
					+ m.getSistemagiocodim().getAAMS_GAMESYSTEM_ID()
					+ "<td style='padding: 5px;'>"
					+ m.getSistemagiocodim().getLoc().getAAMS_LOCATION_ID()
					+ "<td style='padding: 5px;'>"
					+ m.getSistemagiocodim().getLoc().getCOMMERCIAL_NAME()
					+ "<td style='padding: 5px;'>"
					+ m.getSpaziodim().getComune().getNome()
					+ "<td style='padding: 5px;'>"
					+ m.getSpaziodim().getProvincia().getSigla()
					+ "<td style='padding: 5px;'>"
					+ m.getSistemagiocodim().getVlt().getAAMS_VLT_ID()
					+ "<td style='text-align:right;padding: 5px;'>"
					+ m.getMSogei().getBet()
					+ "<td style='text-align:right;padding: 5px;'>"
					+ m.getM600().getBet()
					+ "<td style='text-align:right;padding: 5px;'>"
					+ m.getMSogei().getWin()
					+ "<td style='text-align:right;padding: 5px;'>"
					+ m.getM600().getWin()
					+ "<td style='text-align:right;padding: 5px;'>"
					+ m.getMSogei().getGamesPlayed()
					+ "<td style='text-align:right;padding: 5px;'>"
					+ m.getM600().getGamesPlayed()
					+ "<td style='text-align:right;padding: 5px;'>"
					+ ((MeterfactVlt) m.getMSogei()).getTotalIn()
					+ "<td style='text-align:right;padding: 5px;'>"
					+ ((MeterfactVlt) m.getM600()).getTotalIn()
					+ "<td style='text-align:right;padding: 5px;'>"
					+ ((MeterfactVlt) m.getMSogei()).getTotalOut()
					+ "<td style='text-align:right;padding: 5px;'>"
					+ ((MeterfactVlt) m.getM600()).getTotalOut() + "</tr>";
			i++;
		}

		table += "</table>";

		if (mList.size() == 0 || mList == null) {
			table = "<img style='float:right;' src=\"http://10.0.140.30/BirsPrime/images/logo.png\"><div>Nessuna Discrepanza Rilevata</div>";
		}

		return table;

	}

	private String generateTableTotalGameSystem(List<String> hList,
			List<MeterfactLocation> mList) {
		String table = "<img style='float:right;' src=\"http://10.0.140.30/BirsPrime/images/logo.png\"><table style='border:1px solid black;'><tr style='background-color:#1484E6;'>";
		DecimalFormat df = generateDecimalFormatter();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		for (String s : hList) {
			table += "<th style='color:white;'>" + s;
		}

		table += "</tr>";

		int i = 0;
		String internalClass;

		for (MeterfactLocation m : mList) {

			if (i % 2 == 0) {
				internalClass = "#BCDCF5";
			} else {
				internalClass = "#FFFFFF";
			}

			table = table + "<tr style='background:" + internalClass
					+ ";'><td style='padding: 5px;white-space:nowrap;'>"
					+ sdf.format(m.getTempodim().getData())
					+ "<td style='padding: 5px;'>"
					+ m.getSistemagiocodim().getAAMS_GAMESYSTEM_NAME()
					+ "<td style='text-align:right;padding: 5px;'>"
					+ df.format(m.getBet())
					+ "<td style='text-align:right;padding: 5px;'>"
					+ df.format(m.getWin())
					+ "<td style='text-align:right;padding: 5px;'>"
					+ df.format(m.getGamesPlayed())
					+ "<td style='text-align:right;padding: 5px;'>"
					+ df.format(m.getTotalIn())
					+ "<td style='text-align:right;padding: 5px;'>"
					+ df.format(m.getTotalOut()) + "</tr>";
			i++;
		}

		table += "</table>";

		return table;

	}

	private String generateTableDailyLogs(List<String> hList, List<Log> mList) {
		String table = "<img style='float:right;' src=\"http://10.0.140.30/BirsPrime/images/logo.png\"><table style='border:1px solid black;'><tr style='background-color:#1484E6;'>";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		for (String s : hList) {
			table += "<th style='color:white;'>" + s;
		}

		table += "</tr>";

		int i = 0;
		String internalClass;

		for (Log l : mList) {

			if (i % 2 == 0) {
				internalClass = "#BCDCF5";
			} else {
				internalClass = "#FFFFFF";
			}

			if (l.isTimeForReimport()) {
				internalClass = "#EB8619";
			}

			table = table + "<tr style='background:" + internalClass
					+ ";'><td style='padding: 5px;white-space:nowrap;'>"
					+ sdf.format(l.getDataStart())
					+ "<td style='padding: 5px;'>" + sdf.format(l.getDataEnd())
					+ "<td style='padding: 5px;'>"
					+ sdf.format(l.getOperation_dateStart())
					+ "<td style='padding: 5px;'>"
					+ sdf.format(l.getOperation_dateEnd())
					+ "<td style='padding: 5px;'>" + l.getExit_code()
					+ "<td style='padding: 5px;'>"
					+ l.getAams_game_system_code() + "</tr>";
			i++;
		}

		table += "</table>";

		return table;

	}
}
