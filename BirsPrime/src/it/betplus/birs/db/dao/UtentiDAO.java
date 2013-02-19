package it.betplus.birs.db.dao;

import it.betplus.birs.connector.DBConnectionManager;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.exception.DataLayerException;
import it.betplus.birs.primitive.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;

public class UtentiDAO {

	protected static Logger log = Logger.getLogger(UtentiDAO.class);

	private static UtentiDAO instance;

	private UtentiDAO() {
		super();
	}

	public static synchronized UtentiDAO getInstance() {
		if (instance == null) {
			synchronized (UtentiDAO.class) {
				instance = new UtentiDAO();
			}
		}
		return instance;
	}

	public List<String> getRuoli() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connDataMart = null;
		List<String> allrole = new ArrayList<String>();

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "select DISTINCT(RUOLO) RUOLO from aggregate.ruoli";

			ps = connDataMart.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {

				allrole.add(rs.getString("RUOLO"));
			}

			DBConnectionManager.CloseConnection(connDataMart);

		} catch (SQLException e) {
			log.error("getUtenti: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return allrole;

	}

	public List<Utente> getUtenti() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connDataMart = null;
		Utente user = null;
		List<Utente> alluser = new ArrayList<Utente>();

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "select u.*,ruolo from aggregate.utenti u inner join aggregate.ruoli r on u.`USERNAME` = r.`USERNAME`";

			ps = connDataMart.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				user = new Utente();
				user.setUsername(rs.getString("USERNAME"));
				user.setUserPass(rs.getString("USER_PASS"));
				user.setNome(rs.getString("NOME"));
				user.setCognome(rs.getString("COGNOME"));
				user.setRuolo(rs.getString("RUOLO"));
				user.setEmail(rs.getString("EMAIL"));
				user.setEmailPreferences(rs.getInt("EMAIL_PREFERENCES"));

				alluser.add(user);
			}

			DBConnectionManager.CloseConnection(connDataMart);

		} catch (SQLException e) {
			log.error("getUtenti: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return alluser;

	}

	public javax.mail.internet.InternetAddress[] getEmailByNotification(
			int email_level) throws DataAccessException, AddressException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connDataMart = null;
		List<String> emails = new ArrayList<String>();
		javax.mail.internet.InternetAddress[] addressTo = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "select EMAIL from aggregate.utenti where EMAIL_PREFERENCES = ? ";

			ps = connDataMart.prepareStatement(sql);
			ps.setInt(1, email_level);
			rs = ps.executeQuery();

			while (rs.next()) {
				emails.add(rs.getString("EMAIL"));
			}

			addressTo = new javax.mail.internet.InternetAddress[emails.size()];
			int i = 0;

			for (String rec : emails) {
				addressTo[i] = new javax.mail.internet.InternetAddress(rec);
				i++;
			}

			DBConnectionManager.CloseConnection(connDataMart);

		} catch (SQLException e) {
			log.error("getUtenti: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return addressTo;

	}

	public Utente getUtenteByUsernamePassword(String username, String password)
			throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connDataMart = null;
		Utente user = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "select u.*,ruolo from aggregate.utenti u inner join aggregate.ruoli r on u.`USERNAME` = r.`USERNAME` where u.`USERNAME` like ? and u.`USER_PASS` like ?";

			ps = connDataMart.prepareStatement(sql);
			ps.setString(1, "%" + username + "%");
			ps.setString(2, password);
			rs = ps.executeQuery();

			while (rs.next()) {
				user = new Utente();
				user.setUsername(rs.getString("USERNAME"));
				user.setUserPass(rs.getString("USER_PASS"));
				user.setNome(rs.getString("NOME"));
				user.setCognome(rs.getString("COGNOME"));
				user.setRuolo(rs.getString("RUOLO"));
				user.setEmail(rs.getString("EMAIL"));
				user.setMatcherPreferences(rs.getString("MATCHER_PREFERENCES"));
				user.setEmailPreferences(rs.getInt("EMAIL_PREFERENCES"));
			}

			DBConnectionManager.CloseConnection(connDataMart);

		} catch (SQLException e) {
			log.error("getUtenteByUsernamePassword: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;

	}

	public int inserisciNuovoUtente(Utente ut) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connDataMart = null;
		int risultato = 0;

		if (!esisteUsername(ut.getUsername())) {

			try {
				connDataMart = DBConnectionManager.dataMartConnectionFactory();

				connDataMart.setAutoCommit(false);

				String sql = "insert into aggregate.utenti(username, user_pass, nome, cognome,email) values(?,?,?,?,?)";

				ps = connDataMart.prepareStatement(sql);
				ps.setString(1, ut.getUsername());
				ps.setString(2, ut.getUserPass());
				ps.setString(3, ut.getNome());
				ps.setString(4, ut.getCognome());
				ps.setString(5, ut.getEmail());

				risultato = ps.executeUpdate();

				if (ut.getRuolo() != null && !(ut.getRuolo().isEmpty())) {
					if (risultato == 1) {
						String sql_2 = "insert into aggregate.ruoli(username, ruolo) values(?,?)";
						ps = connDataMart.prepareStatement(sql_2);
						ps.setString(1, ut.getUsername());
						ps.setString(2, ut.getRuolo());
						risultato = ps.executeUpdate();
					}
				}

				if (risultato == 1)
					connDataMart.commit();
				else
					connDataMart.rollback();

				DBConnectionManager.CloseConnection(connDataMart);

			} catch (SQLException e) {
				log.error("inserisciNuovoUtente: SQL failed", e);
				e.printStackTrace();
				throw new DataAccessException(e.toString(), e);
			} catch (RuntimeException e) {
				throw new DataAccessException(e.toString());
			} catch (DataLayerException e) {
				e.printStackTrace();
			} finally {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return risultato;
	}

	public boolean esisteUsername(String username) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connDataMart = null;
		ResultSet rs = null;
		boolean esiste = false;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "select username from aggregate.utenti where username = ?";

			ps = connDataMart.prepareStatement(sql);
			ps.setString(1, username);

			rs = ps.executeQuery();

			if (rs.next())
				esiste = true;

			DBConnectionManager.CloseConnection(connDataMart);

		} catch (SQLException e) {
			log.error("verificaUsername: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return esiste;
	}

	public int modificaPreferenzeConfronti(String pref, String username)
			throws DataAccessException {
		PreparedStatement ps = null;
		Connection connDataMart = null;
		int risultato = 0;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();
			connDataMart.setAutoCommit(false);

			String sql = "update aggregate.utenti set MATCHER_PREFERENCES = ? where username=?";

			ps = connDataMart.prepareStatement(sql);
			ps.setString(1, pref);
			ps.setString(2, username);

			risultato = ps.executeUpdate();

			if (risultato > 0)
				connDataMart.commit();
			else
				connDataMart.rollback();

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("modificaPreferenzeConfronti: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return risultato;

	}

	public int modificaUtente(Utente ut, String username)
			throws DataAccessException {
		PreparedStatement ps = null;
		Connection connDataMart = null;
		int risultato = 0;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();
			connDataMart.setAutoCommit(false);

			String sql_1 = "delete from aggregate.ruoli where username=?";
			ps = connDataMart.prepareStatement(sql_1);
			ps.setString(1, username);
			risultato = ps.executeUpdate();

			String sql = "update aggregate.utenti set username=?, user_pass=?, nome=?, cognome=?,email=?,email_preferences=? where username=?";

			ps = connDataMart.prepareStatement(sql);
			ps.setString(1, ut.getUsername());
			ps.setString(2, ut.getUserPass());
			ps.setString(3, ut.getNome());
			ps.setString(4, ut.getCognome());
			ps.setString(5, ut.getEmail());
			ps.setInt(6, ut.getEmailPreferences());
			ps.setString(7, username);

			risultato = ps.executeUpdate();

			if (ut.getRuolo() != null && !(ut.getRuolo().isEmpty())) {
				if (risultato > 0) {
					String sql_2 = "insert into aggregate.ruoli(username, ruolo) values(?,?)";
					ps = connDataMart.prepareStatement(sql_2);
					ps.setString(1, ut.getUsername());
					ps.setString(2, ut.getRuolo());
					risultato = ps.executeUpdate();
				}
			}

			if (risultato > 0)
				connDataMart.commit();
			else
				connDataMart.rollback();

			DBConnectionManager.CloseConnection(connDataMart);

		} catch (SQLException e) {
			log.error("modificaUtente: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return risultato;
	}

	public int eliminaUtente(String username) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connDataMart = null;
		int risultato = 0;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();
			connDataMart.setAutoCommit(false);

			String sql_1 = "delete from aggregate.ruoli where username=?";
			ps = connDataMart.prepareStatement(sql_1);
			ps.setString(1, username);
			risultato = ps.executeUpdate();

			String sql = "delete from aggregate.utenti where username=?";

			ps = connDataMart.prepareStatement(sql);
			ps.setString(1, username);
			risultato = ps.executeUpdate();

			if (risultato > 0)
				connDataMart.commit();
			else
				connDataMart.rollback();

			DBConnectionManager.CloseConnection(connDataMart);

		} catch (SQLException e) {
			log.error("eliminaUtente: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return risultato;
	}
}