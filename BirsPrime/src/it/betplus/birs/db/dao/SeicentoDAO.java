package it.betplus.birs.db.dao;

import it.betplus.birs.connector.DBConnectionManager;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.Location;
import it.betplus.birs.primitive.MeterfactLocation;
import it.betplus.birs.primitive.MeterfactVlt;
import it.betplus.birs.primitive.Sistemagiocodim;
import it.betplus.birs.primitive.Tempodim;
import it.betplus.birs.primitive.Vlt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class SeicentoDAO {

	protected static Logger log = Logger.getLogger(SeicentoDAO.class);

	private static SeicentoDAO instance;

	private SeicentoDAO() {
		super();
	}

	public static synchronized SeicentoDAO getInstance() {
		if (instance == null) {
			synchronized (SeicentoDAO.class) {
				instance = new SeicentoDAO();
			}
		}
		return instance;
	}

	public ArrayList<MeterfactLocation> retrieveTotalMeterList600_system(
			Date data1) throws DataAccessException {
		ArrayList<MeterfactLocation> meters = new ArrayList<MeterfactLocation>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {

			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "SELECT sum(BET) BET,sum(BET_NUM) BET_NUM,sum(WIN) WIN,sum(TOT_IN) TOT_IN,sum(TOT_OUT) TOT_OUT,AAMS_GAMES_SYSTEM_CODE, DATA "
					+ "FROM aggregate.birsaamsmeter met where data = date(?) group by AAMS_GAMES_SYSTEM_CODE";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			ps.setDate(1, data1_);

			rs = ps.executeQuery();

			while (rs.next()) {
				MeterfactLocation mLoc = new MeterfactLocation();

				mLoc.setBet((double) rs.getLong("BET") / 100);
				mLoc.setWin((double) rs.getLong("WIN") / 100);
				mLoc.setGamesPlayed(rs.getLong("BET_NUM"));
				mLoc.setTotalIn((double) rs.getLong("TOT_IN") / 100);
				mLoc.setTotalOut((double) rs.getLong("TOT_OUT") / 100);

				// SISTEMA GIOCO DIM
				Sistemagiocodim sysDim = new Sistemagiocodim();
				sysDim.setAAMS_GAMESYSTEM_ID(rs
						.getLong("AAMS_GAMES_SYSTEM_CODE"));

				// TEMPO DIM

				Tempodim tDim = new Tempodim();
				tDim.setAllData(rs.getDate("data"));

				mLoc.setSistemagiocodim(sysDim);
				mLoc.setTempodim(tDim);

				meters.add(mLoc);

			}

			DBConnectionManager.CloseConnection(connDataMart);

		} catch (Exception e) {
			log.error("retrieveTotalMeterList600_system: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return meters;

	}

	public HashMap<String, MeterfactLocation> retrieveMeterMap600_locations(
			Date data1, Date data2) throws DataAccessException {

		HashMap<String, MeterfactLocation> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "SELECT met.AAMS_LOCATION_CODE, BET,BET_NUM, WIN, TOT_IN, TOT_OUT, AAMS_GAMES_SYSTEM_CODE, DATA ,commercial_name "
					+ "FROM aggregate.birsaamsmeter met "
					+ "inner join aggregate.birslocation loc on met.AAMS_LOCATION_CODE = loc.AAMS_LOCATION_CODE "
					+ "where data between ? and ? "
					+ "group by aams_location_code,AAMS_GAMES_SYSTEM_CODE, data";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Date data2_ = new java.sql.Date(data2.getTime());
			ps.setDate(1, data1_);
			ps.setDate(2, data2_);

			rs = ps.executeQuery();

			meters = new HashMap<String, MeterfactLocation>();

			while (rs.next()) {
				MeterfactLocation mLoc = new MeterfactLocation();

				mLoc.setBet((double) rs.getLong("BET") / 100);
				mLoc.setWin((double) rs.getLong("WIN") / 100);
				mLoc.setGamesPlayed(rs.getLong("BET_NUM"));
				mLoc.setTotalIn((double) rs.getLong("TOT_IN") / 100);
				mLoc.setTotalOut((double) rs.getLong("TOT_OUT") / 100);

				// SISTEMA GIOCO DIM

				Location loc = new Location();
				loc.setAAMS_LOCATION_ID(rs.getString("aams_location_code"));
				loc.setCOMMERCIAL_NAME(rs.getString("commercial_name"));

				// Vlt vlt = new Vlt();
				// vlt.setAAMS_VLT_ID(rs.getString("aams_vlt_code"));

				Sistemagiocodim sysDim = new Sistemagiocodim();
				sysDim.setAAMS_GAMESYSTEM_ID(rs
						.getLong("AAMS_GAMES_SYSTEM_CODE"));
				// sysDim.setVlt(vlt);
				sysDim.setLoc(loc);

				// TEMPO DIM

				Tempodim tDim = new Tempodim();
				tDim.setAllData(rs.getDate("data"));

				mLoc.setSistemagiocodim(sysDim);
				mLoc.setTempodim(tDim);

				meters.put(mLoc.getSistemagiocodim().getLoc()
						.getAAMS_LOCATION_ID()
						+ "-"
						+ mLoc.getSistemagiocodim().getAAMS_GAMESYSTEM_ID()
						+ "-" + mLoc.getTempodim().simpleTempodimToString(),
						mLoc);

			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeterMap600_locations: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeterMap600_locations failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return meters;
	}

	public HashMap<String, MeterfactVlt> retrieveMeterMap600_vlts(Date data1,
			Date data2) throws DataAccessException {

		HashMap<String, MeterfactVlt> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "select aams_vlt_code, data, sum(bet) as bet,sum(win) as win, sum(bet_num) as bet_num, "
					+ "sum(tot_in) as tot_in,sum(tot_out) as tot_out from aggregate.birsaamsmeters met "
					+ "where data between ? and ? "
					+ "group by aams_vlt_code, data";

			/*
			 * "select aams_vlt_code,aams_location_code, commercial_name, data, sum(bet) as bet,sum(win) as win, sum(bet_num) as bet_num, "
			 * +
			 * "sum(tot_in) as tot_in,sum(tot_out) as tot_out, v.aams_game_system_code from "
			 * +
			 * "(select loc.aams_location_code,loc.commercial_name,vlt.gs_vlt_code,met.aams_vlt_code,met.data, "
			 * +
			 * "met.bet,met.win,met.bet_num,met.tot_in,met.tot_out,vlt.aams_game_system_code "
			 * + "from aggregate.birsaamsmeters met " +
			 * "left join aggregate.birsvlt vlt on met.aams_vlt_code =vlt.aams_vlt_code "
			 * +
			 * "left join aggregate.birslocation loc on vlt.aams_location_code=loc.aams_location_code "
			 * +
			 * "where data between ? and ? and vlt.aams_location_code is not null ) v "
			 * +
			 * "group by aams_vlt_code,aams_location_code,aams_game_system_code, data "
			 * +
			 * "order by data,aams_game_system_code,aams_location_code,aams_vlt_code asc "
			 * ;
			 */

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Date data2_ = new java.sql.Date(data2.getTime());
			ps.setDate(1, data1_);
			ps.setDate(2, data2_);

			rs = ps.executeQuery();

			meters = new HashMap<String, MeterfactVlt>();

			while (rs.next()) {
				MeterfactVlt mVlt = new MeterfactVlt();

				mVlt.setBet((double) rs.getLong("BET") / 100);
				mVlt.setWin((double) rs.getLong("WIN") / 100);
				mVlt.setGamesPlayed(rs.getLong("BET_NUM"));
				mVlt.setTotalIn((double) rs.getLong("TOT_IN") / 100);
				mVlt.setTotalOut((double) rs.getLong("TOT_OUT") / 100);

				// SISTEMA GIOCO DIM

				// Location loc = new Location();
				// loc.setAAMS_LOCATION_ID(rs.getString("aams_location_code"));
				// loc.setCOMMERCIAL_NAME(rs.getString("commercial_name"));

				Vlt vlt = new Vlt();
				vlt.setAAMS_VLT_ID(rs.getString("aams_vlt_code"));

				Sistemagiocodim sysDim = new Sistemagiocodim();
				// sysDim.setAAMS_GAMESYSTEM_ID(rs.getLong("aams_game_system_code"));
				sysDim.setVlt(vlt);
				// sysDim.setLoc(loc);

				// TEMPO DIM

				Tempodim tDim = new Tempodim();
				tDim.setAllData(rs.getDate("data"));

				mVlt.setSistemagiocodim(sysDim);
				mVlt.setTempodim(tDim);

				meters.put(mVlt.getSistemagiocodim().getVlt().getAAMS_VLT_ID()
						+ "-" + mVlt.getTempodim().simpleTempodimToString(),
						mVlt);
			}
			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeterMap600_locations: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeterMap600_locations failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return meters;
	}

	public Location inQualeLocation(String code_id, Date data,
			Connection connStaging) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Location loc = new Location();

		try {

			String sql = "select vh.aams_location_code aams_location_code,l.commercial_name commercial_name"
					+ "from (select aams_location_code "
					+ "from vlthistory "
					+ "where aams_vlt_code= ? "
					+ "and date_change<=? "
					+ "order by date_change desc limit 1)  vh "
					+ "inner join birslocation l on l.aams_location_code = vh.aams_location_code";

			log.info(sql);

			ps = connStaging.prepareStatement(sql);

			java.sql.Date dataSql = new java.sql.Date(data.getTime());

			ps.setString(1, code_id);
			ps.setDate(2, dataSql);

			rs = ps.executeQuery();

			while (rs.next()) {
				loc.setCOMMERCIAL_NAME(rs.getString("commercial_name"));
				loc.setAAMS_LOCATION_ID(rs.getString("aams_location_code"));
			}

		} catch (SQLException e) {
			log.error("inQualeLocation failed", e);
			e.printStackTrace();
		} catch (Exception e) {
			log.error("inQualeLocation failed", e);
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return loc;
	}

}
