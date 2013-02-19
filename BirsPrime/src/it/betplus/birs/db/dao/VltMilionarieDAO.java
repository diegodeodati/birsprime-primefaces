package it.betplus.birs.db.dao;

import it.betplus.birs.connector.DBConnectionManager;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.exception.DataLayerException;
import it.betplus.birs.primitive.Meterfact;
import it.betplus.birs.primitive.MeterfactLocation;
import it.betplus.birs.primitive.MeterfactVlt;
import it.betplus.birs.primitive.RealMillionvlt;
import it.betplus.web.framework.utils.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class VltMilionarieDAO {

	protected static Logger log = Logger.getLogger(VltMilionarieDAO.class);

	private static VltMilionarieDAO instance;

	private VltMilionarieDAO() {
		super();
	}

	public static synchronized VltMilionarieDAO getInstance() {
		if (instance == null) {
			synchronized (VltMilionarieDAO.class) {
				instance = new VltMilionarieDAO();
			}
		}
		return instance;
	}

	public boolean isMilionarie(Meterfact m) {

		Connection connDataMart = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean out = false;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			if (m instanceof MeterfactLocation) {

				String sql = "select count(vm.aams_location_code) c "
						+ "from aggregate.birsvltmilionarie where aams_location_code like ? and  data=date(?)";

				ps = connDataMart.prepareStatement(sql);
				ps.setString(1, m.getSistemagiocodim().getLoc()
						.getAAMS_LOCATION_ID());
				ps.setDate(2, new java.sql.Date(m.getTempodim().getData()
						.getTime()));
			} else if (m instanceof MeterfactVlt) {
				String sql = "select count(vm.aams_location_code) c "
						+ "from aggregate.birsvltmilionarie where aams_location_code like ?  and aams_vlt_code like = ? and  data=date(?)";

				ps = connDataMart.prepareStatement(sql);
				ps.setString(1, m.getSistemagiocodim().getLoc()
						.getAAMS_LOCATION_ID());
				ps.setString(2, m.getSistemagiocodim().getVlt()
						.getAAMS_VLT_ID());
				ps.setDate(3, new java.sql.Date(m.getTempodim().getData()
						.getTime()));

			}

			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getInt(1) > 0)
					out = true;

			}

			ps.close();
			rs.close();

		} catch (DataLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return out;
	}

	public HashMap<String, RealMillionvlt> getRangeVltMilionarieByLocation(
			Date dStart, Date dEnd) throws Exception {

		HashMap<String, RealMillionvlt> hashMillionVlt = new HashMap<String, RealMillionvlt>();

		Connection connDataMart = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "select aams_vlt_code,data,vm.aams_location_code,l.COMMERCIAL_NAME,bet,win,bet_reale,win_reale from aggregate.birsvltmilionarie vm "
					+ "inner join aggregate.birslocation l on vm.aams_location_code=l.AAMS_LOCATION_CODE  where data between date(?) and date(?)";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);
			ps.setDate(1, new java.sql.Date(dStart.getTime()));
			ps.setDate(2, new java.sql.Date(dEnd.getTime()));
			rs = ps.executeQuery();

			while (rs.next()) {
				RealMillionvlt vltM = new RealMillionvlt();
				vltM.setAams_vlt_id(rs.getString("aams_vlt_code"));
				vltM.setData(new java.util.Date(rs.getDate("data").getTime()));
				vltM.setAams_location_id(rs.getString("aams_location_code"));
				vltM.setLocation_name(rs.getString("COMMERCIAL_NAME"));
				vltM.setBet(rs.getLong("bet") / 100);
				vltM.setWin(rs.getLong("win") / 100);
				vltM.setBet_reale(rs.getLong("bet_reale") / 100);
				vltM.setWin_reale(rs.getLong("win_reale") / 100);

				hashMillionVlt
						.put(DateUtils.formatDateToString(vltM.getData(),
								"d-M-yyyy") + "-" + vltM.getAams_location_id(),
								vltM);
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (DataLayerException e) {

			log.error("getAllVltMilionarieDay: generic error", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (SQLException e) {

			log.error("getAllVltMilionarieDay: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return hashMillionVlt;
	}

	public HashMap<String, RealMillionvlt> getRangeVltMilionarieByVlt(
			Date dStart, Date dEnd) throws Exception {

		HashMap<String, RealMillionvlt> hashMillionVlt = new HashMap<String, RealMillionvlt>();

		Connection connDataMart = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "select aams_vlt_code,data,vm.aams_location_code,l.COMMERCIAL_NAME,bet,win,bet_reale,win_reale from aggregate.birsvltmilionarie vm "
					+ "inner join aggregate.birslocation l on vm.aams_location_code=l.AAMS_LOCATION_CODE  where data between date(?) and date(?)";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);
			ps.setDate(1, new java.sql.Date(dStart.getTime()));
			ps.setDate(2, new java.sql.Date(dEnd.getTime()));
			rs = ps.executeQuery();

			while (rs.next()) {
				RealMillionvlt vltM = new RealMillionvlt();
				vltM.setAams_vlt_id(rs.getString("aams_vlt_code"));
				vltM.setData(new java.util.Date(rs.getDate("data").getTime()));
				vltM.setAams_location_id(rs.getString("aams_location_code"));
				vltM.setLocation_name(rs.getString("COMMERCIAL_NAME"));
				vltM.setBet(rs.getLong("bet") / 100);
				vltM.setWin(rs.getLong("win") / 100);
				vltM.setBet_reale(rs.getLong("bet_reale") / 100);
				vltM.setWin_reale(rs.getLong("win_reale") / 100);

				hashMillionVlt.put(
						DateUtils.formatDateToString(vltM.getData(),
								"d-M-yyyy") + "-" + vltM.getAams_vlt_id(),
						vltM);

			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (DataLayerException e) {

			log.error("getAllVltMilionarieDay: generic error", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (SQLException e) {

			log.error("getAllVltMilionarieDay: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return hashMillionVlt;
	}

	public List<RealMillionvlt> getAllVltMilionarieDay(Date d)
			throws DataAccessException {
		List<RealMillionvlt> listVltMilionarie = new ArrayList<RealMillionvlt>();

		Connection connDataMart = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "select aams_vlt_code,data,vm.aams_location_code,l.COMMERCIAL_NAME,bet,win,bet_reale,win_reale from aggregate.birsvltmilionarie vm "
					+ "inner join aggregate.birslocation l on vm.aams_location_code=l.AAMS_LOCATION_CODE  where data = date(?)";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);
			ps.setDate(1, new java.sql.Date(d.getTime()));
			rs = ps.executeQuery();

			while (rs.next()) {
				RealMillionvlt vltM = new RealMillionvlt();
				vltM.setAams_vlt_id(rs.getString("aams_vlt_code"));
				vltM.setData(new java.sql.Date(rs.getDate("data").getTime()));
				vltM.setAams_location_id(rs.getString("aams_location_code"));
				vltM.setLocation_name(rs.getString("COMMERCIAL_NAME"));
				vltM.setBet(rs.getLong("bet") / 100);
				vltM.setWin(rs.getLong("win") / 100);
				vltM.setBet_reale(rs.getLong("bet_reale") / 100);
				vltM.setWin_reale(rs.getLong("win_reale") / 100);

				listVltMilionarie.add(vltM);

			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (DataLayerException e) {

			log.error("getAllVltMilionarieDay: generic error", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (SQLException e) {

			log.error("getAllVltMilionarieDay: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listVltMilionarie;
	}

}
