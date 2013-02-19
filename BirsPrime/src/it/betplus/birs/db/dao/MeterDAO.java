package it.betplus.birs.db.dao;

import it.betplus.birs.connector.DBConnectionManager;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.Clientedim;
import it.betplus.birs.primitive.Comune;
import it.betplus.birs.primitive.Game;
import it.betplus.birs.primitive.Gestore;
import it.betplus.birs.primitive.Location;
import it.betplus.birs.primitive.MeterfactGame;
import it.betplus.birs.primitive.MeterfactLocation;
import it.betplus.birs.primitive.MeterfactRegion;
import it.betplus.birs.primitive.MeterfactSysGame;
import it.betplus.birs.primitive.MeterfactVlt;
import it.betplus.birs.primitive.Nazione;
import it.betplus.birs.primitive.ProbMillionVlt;
import it.betplus.birs.primitive.Provincia;
import it.betplus.birs.primitive.Regione;
import it.betplus.birs.primitive.Sistemagiocodim;
import it.betplus.birs.primitive.Spaziodim;
import it.betplus.birs.primitive.Tempodim;
import it.betplus.birs.primitive.Vlt;
import it.betplus.web.framework.utils.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class MeterDAO {

	protected static Logger log = Logger.getLogger(MeterDAO.class);

	private static MeterDAO instance;

	private MeterDAO() {
		super();
	}

	public static synchronized MeterDAO getInstance() {
		if (instance == null) {
			synchronized (MeterDAO.class) {
				instance = new MeterDAO();
			}
		}
		return instance;
	}

	public List<ProbMillionVlt> retrieveMeter_vlt_milionarie(Date data1)
			throws DataAccessException {

		List<ProbMillionVlt> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "select vista1.aams_location_id,vista1.location_name, vista1.num_vlt_attive num_vlt_giocanti, "
					+ "vista1.total_bet_sala,vista1.media_bet_sala , vista2.aams_vlt_id, vista2.gs_vlt_id ,"
					+ "vista2.bet_vlt from  ( "
					+ "SELECT aams_location_id, location_name , count(distinct(aams_vlt_id)) "
					+ "as num_vlt_attive, sum(sum_bet) as total_bet_sala, avg(sum_bet) as media_bet_sala  FROM "
					+ "(SELECT mf.aams_location_code as aams_location_id,loc.commercial_name as location_name, "
					+ "mf.aams_vlt_code as aams_vlt_id, sum(mf.bet) as sum_bet  FROM meter_vlt mf "
					+ "inner join aggregate.birslocation loc on loc.AAMS_LOCATION_CODE = mf.AAMS_LOCATION_CODE "
					+ "where mf.data  = ? and mf.AAMS_GAMESYSTEM_CODE != '1711000065' "
					+ "group by mf.aams_location_code, loc.commercial_name, mf.aams_vlt_code)  vistai "
					+ "group by aams_location_id, location_name) vista1 "
					+ "right join "
					+ "(SELECT mf.aams_location_code as aams_location_id, "
					+ "mf.aams_vlt_code as aams_vlt_id, mf.gs_vlt_code as gs_vlt_id, sum(mf.bet) as bet_vlt "
					+ "FROM meter_vlt mf "
					+ "where mf.`DATA` = ? and mf.AAMS_GAMESYSTEM_CODE != '1711000065' "
					+ "group by mf.aams_location_code, mf.aams_vlt_code, mf.gs_vlt_code "
					+ "having sum(mf.bet)>=1500000 "
					+ "order by mf.aams_location_code asc) vista2 "
					+ "on vista1.aams_location_id=vista2.aams_location_id order by vista1.location_name asc";

			/*
			 * "select vista1.aams_location_id,vista1.location_name, vista1.num_vlt_attive num_vlt_giocanti,"
			 * +
			 * "vista1.total_bet_sala,vista1.media_bet_sala , vista2.aams_vlt_id, vista2.gs_vlt_id ,"
			 * + "vista2.bet_vlt " + "from  ( " +
			 * "SELECT aams_location_id, location_name , count(distinct(aams_vlt_id)) "
			 * +
			 * "as num_vlt_attive, sum(sum_bet) as total_bet_sala, avg(sum_bet) as media_bet_sala  FROM "
			 * +
			 * "(SELECT sdim.aams_location_code as aams_location_id,loc.commercial_name as location_name, "
			 * +
			 * "sdim.aams_vlt_code as aams_vlt_id, sum(mf.bet) as sum_bet  FROM meterfact mf  "
			 * + "inner join tempodim tdim on mf.dmtempo_id=tdim.id " +
			 * "left outer join sistemagiocodim sdim on mf.DMSISTEMAGIOCO_ID = sdim.id "
			 * +
			 * "inner join aggregate.birslocation loc on loc.AAMS_LOCATION_CODE = sdim.AAMS_LOCATION_CODE "
			 * +
			 * "where date(tdim.data)  = ? and sdim.AAMS_GAMESYSTEM_CODE != '1711000065' "
			 * +
			 * "group by sdim.aams_location_code, loc.commercial_name, sdim.aams_vlt_code)  vistai  "
			 * + "group by aams_location_id, location_name) vista1 " +
			 * "right join " +
			 * "(SELECT sdim.aams_location_code as aams_location_id, " +
			 * "sdim.aams_vlt_code as aams_vlt_id, sdim.gs_vlt_code as gs_vlt_id, sum(mf.bet) as bet_vlt "
			 * + "FROM meterfact mf " +
			 * "inner join tempodim tdim on mf.dmtempo_id=tdim.id " +
			 * "left outer join sistemagiocodim sdim on mf.DMSISTEMAGIOCO_ID = sdim.id "
			 * +
			 * "where date(tdim.data) = ?  and sdim.AAMS_GAMESYSTEM_CODE != '1711000065' "
			 * +
			 * "group by sdim.aams_location_code, sdim.aams_vlt_code, sdim.gs_vlt_code "
			 * + "having sum(mf.win)>=1500000 " +
			 * "order by sdim.aams_location_code asc) vista2 " +
			 * "on vista1.aams_location_id=vista2.aams_location_id order by vista1.location_name asc"
			 * ;
			 */

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());

			log.info(data1_);
			ps.setDate(1, data1_);
			ps.setDate(2, data1_);

			rs = ps.executeQuery();

			meters = new ArrayList<ProbMillionVlt>();

			while (rs.next()) {
				ProbMillionVlt mvb = new ProbMillionVlt();
				mvb.setAAMS_LOCATION_ID(rs.getString("AAMS_LOCATION_ID"));
				mvb.setLOCATION_NAME(rs.getString("LOCATION_NAME"));
				mvb.setNUM_VLT(rs.getInt("num_vlt_giocanti"));
				mvb.setTOTAL_BET_SALA(rs.getDouble("TOTAL_BET_SALA") / 100);
				mvb.setMEDIA_BET_VLT(rs.getDouble("MEDIA_BET_SALA") / 100);
				mvb.setMAX_BET_VLT(rs.getDouble("BET_VLT") / 100);
				mvb.setAAMS_VLT_ID(rs.getString("AAMS_VLT_ID"));
				mvb.setGS_VLT_ID(rs.getString("GS_VLT_ID"));

				Double pmb_a = mvb.getMAX_BET_VLT() / mvb.getTOTAL_BET_SALA();
				pmb_a = pmb_a * 100;
				int pmb = pmb_a.intValue();
				mvb.setPERC_MAX_BET(pmb);

				medie_vlt_ultimi_7_e_30gg(data1, mvb, connDataMart);

				meters.add(mvb);
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_vlt_milionarie: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		}

		catch (Exception e) {
			log.error("retrieveMeter_vlt_milionarie failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public void medie_vlt_ultimi_7_e_30gg(Date data, ProbMillionVlt mmv,
			Connection connDataMart) throws DataAccessException {

		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			java.sql.Date data7ggPrima = new java.sql.Date(DateUtils
					.calcPrevious7gg(data).getTime());
			java.sql.Date data30ggPrima = new java.sql.Date(DateUtils
					.calcPreviousMonth(data).getTime());

			String sql = "select avg(v1.bet_vlt) MEDIA_7GG, avg(v2.bet_vlt) MEDIA_30GG "
					+ "from (SELECT mf.data,mf.aams_vlt_code as aams_vlt_id, sum(mf.bet) as bet_vlt "
					+ "FROM meterfact mf "
					+ "where mf.data between ? and ? "
					+ "and mf.aams_vlt_code like ? "
					+ "GROUP BY mf.data, mf.aams_vlt_code) v1 "
					+ "left join (SELECT mf.data,mf.aams_vlt_code as aams_vlt_id, sum(mf.bet) as bet_vlt "
					+ "FROM meterfact mf "
					+ "where mf.data between ? and ? "
					+ "and mf.aams_vlt_code like ? "
					+ "GROUP BY mf.data, mf.aams_vlt_code) v2 on v1.aams_vlt_id=v2.aams_vlt_id";

			/*
			 * "select avg(v1.bet_vlt) MEDIA_7GG, avg(v2.bet_vlt) MEDIA_30GG  "
			 * +
			 * "from (SELECT tdim.data,sdim.aams_vlt_code as aams_vlt_id, sum(mf.bet) as bet_vlt "
			 * + "FROM meterfact mf " +
			 * "inner join tempodim tdim on mf.dmtempo_id=tdim.id " +
			 * "left outer join sistemagiocodim sdim on mf.DMSISTEMAGIOCO_ID= sdim.id "
			 * + "where tdim.data between ? and ? " +
			 * "and sdim.aams_vlt_code like ?  " +
			 * "GROUP BY tdim.data, sdim.aams_vlt_code) v1 " +
			 * "left join (SELECT tdim.data,sdim.aams_vlt_code as aams_vlt_id, sum(mf.bet) as bet_vlt "
			 * + "FROM meterfact mf  " +
			 * "inner join tempodim tdim on mf.dmtempo_id=tdim.id " +
			 * "left outer join sistemagiocodim sdim on mf.DMSISTEMAGIOCO_ID = sdim.id "
			 * 0 + "where tdim.data between ? and ? " +
			 * "and sdim.aams_vlt_code like ? " +
			 * "GROUP BY tdim.data, sdim.aams_vlt_code) v2 on v1.aams_vlt_id=v2.aams_vlt_id"
			 * ;
			 */

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data.getTime());

			ps.setDate(1, data7ggPrima);
			ps.setDate(2, data1_);
			ps.setString(3, mmv.getAAMS_VLT_ID());

			ps.setDate(4, data30ggPrima);
			ps.setDate(5, data1_);
			ps.setString(6, mmv.getAAMS_VLT_ID());

			log.info(sql + " 7gg: " + data7ggPrima + " 30gg " + data30ggPrima);

			rs = ps.executeQuery();

			while (rs.next()) {
				mmv.setMEDIA_7GG(rs.getDouble("MEDIA_7GG") / 100);
				mmv.setMEDIA_30GG(rs.getDouble("MEDIA_30GG") / 100);
			}

		} catch (SQLException e) {
			log.error("media_vlt_ultimi_7gg: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		}

		catch (Exception e) {
			log.error("media_vlt_ultimi_7gg failed", e);
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
	}

	public List<MeterfactSysGame> retrieveMeter_sysGioco(Date data1, Date data2)
			throws DataAccessException {

		List<MeterfactSysGame> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN,"
					+ "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, "
					+ "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT,"
					+ "sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT,"
					+ "this_.AAMS_GAMESYSTEM_CODE as AAMS_GAMESYSTEM_ID,"
					+ "tempodim5_.DATA as DATA from meter_location this_ "
					+ "inner join TEMPODIM tempodim5_ on this_.DMTEMPO_ID=tempodim5_.ID "
					+ "WHERE tempodim5_.DATA between ? and  ? "
					+ "GROUP BY date(tempodim5_.DATA),this_.AAMS_GAMESYSTEM_CODE "
					+ "ORDER BY this_.AAMS_GAMESYSTEM_CODE,tempodim5_.DATA";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setDate(1, data1_);
			ps.setTimestamp(2, data2_);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactSysGame>();

			while (rs.next()) {
				MeterfactSysGame m = new MeterfactSysGame();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setNetWin(rs.getDouble("NET_WIN") / 100);
				m.setHouseWin(rs.getDouble("HOUSE_WIN") / 100);
				m.setSupplierProfit(rs.getDouble("SUPPLIER_PROFIT") / 100);
				m.setOperatorsProfit(rs.getDouble("OPERATORS_PROFIT") / 100);
				m.setBplusNetProfit(rs.getDouble("BPLUS_NET_PROFIT") / 100);

				// TEMPO DIM

				tDim.setAllData(rs.getDate("DATA"));

				// SYSDIM
				sysDim.setAAMS_GAMESYSTEM_ID(rs.getLong("AAMS_GAMESYSTEM_ID"));

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				System.out.println(meters.size());
				int i=0;
				if(meters.size()>1)
				if(m.getSistemagiocodim().getAAMS_GAMESYSTEM_ID()!=meters.get(meters.size()-1).getSistemagiocodim().getAAMS_GAMESYSTEM_ID())
					
					while(DateUtils.isDateEquals(meters.get(i).getTempodim().getData(),m.getTempodim().getData())){
							System.out.println(meters.get(i).getTempodim().getData());
							System.out.println(m.getTempodim().getData());
							System.out.println("ancora diversi");
							System.out.println(meters.get(meters.size()-1));
							i++;
					}
					
					meters.add(m);

			}
			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_sysGioco: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_sysGioco ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_sysGioco failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public List<MeterfactLocation> retrieveMeter_locations(Date data1,
			Date data2, String sysGame) throws DataAccessException {

		List<MeterfactLocation> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*, loc.COMMERCIAL_NAME as LOCATION_NAME, loc.ADDRESS as LOCATION_ADDRESS,loc.STREET_NUMBER as NUMERO_CIVICO, "
					+ "loc.AAMS_LOCATION_CODE as AAMS_LOCATION_ID,top.DESCRIPTION as TOPONIMO, "
					+ "reg.NOME as REGIONE, prov.SIGLA as PROVINCIA, com.NOME as COMUNE from ( "
					+ "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
					+ "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
					+ "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
					+ "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, "
					+ "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, "
					+ "sum(total_in) TOTAL_IN,sum(total_out) TOTAL_OUT,sum(ticket_in) TICKET_IN,sum(ticket_out) TICKET_OUT,sum(coin_in) COIN_IN, "
					+ "sum(bill_in) BILL_IN,sum(card_in) CARD_IN,sum(prepaid_in) TOTAL_PREPAID_IN, sum(HANDPAY) HANDPAY,"
					+ "this_.DATA, this_.AAMS_LOCATION_CODE, clientidim6_.COD_GESTORE, "
					+ "this_.AAMS_GAMESYSTEM_CODE AAMS_GAMESYSTEM_ID "
					+ "from meter_location this_ "
					+ "inner join CLIENTEDIM clientidim6_ on this_.DMCLIENTE_ID=clientidim6_.ID "
					+ "WHERE this_.AAMS_GAMESYSTEM_CODe like ? "
					+ "and this_.DATA between ? and  ? "
					+ "GROUP BY this_.DATA,this_.AAMS_LOCATION_CODE,this_.AAMS_GAMESYSTEM_CODE ) x "
					+ "inner join aggregate.birslocation loc on x.AAMS_LOCATION_CODE = loc.AAMS_LOCATION_CODE "
					+ "inner join aggregate.birscomuni com on loc.CADASTRAL_CODE = com.CADASTRAL_CODE  "
					+ "inner join aggregate.birsprovince prov on com.ID_PROV=prov.ID_PROV  "
					+ "inner join aggregate.birstoponimo top on loc.ID_TOPONIMO = top.ID  "
					+ "inner join aggregate.birsregioni reg on prov.ID_REG=reg.ID_REG";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setString(1, sysGame);
			ps.setDate(2, data1_);
			ps.setTimestamp(3, data2_);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactLocation>();

			while (rs.next()) {
				MeterfactLocation m = new MeterfactLocation();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setTotalIn(rs.getDouble("TOTAL_IN") / 100);
				m.setTotalOut(rs.getDouble("TOTAL_OUT") / 100);
				m.setTicketIn(rs.getDouble("TICKET_IN") / 100);
				m.setTicketOut(rs.getDouble("TICKET_OUT") / 100);
				m.setCoinIn(rs.getDouble("COIN_IN") / 100);
				m.setBillIn(rs.getDouble("BILL_IN") / 100);
				m.setCardIn(rs.getDouble("CARD_IN") / 100);
				m.setPrepaidIn(rs.getDouble("TOTAL_PREPAID_IN") / 100);
				m.setHandpay(rs.getDouble("HANDPAY") / 100);
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);
				m.setPreu(rs.getDouble("PREU") / 100);
				m.setAams(rs.getDouble("AAMS") / 100);
				m.setNetWin(rs.getDouble("NET_WIN") / 100);
				m.setHouseWin(rs.getDouble("HOUSE_WIN") / 100);
				m.setSupplierProfit(rs.getDouble("SUPPLIER_PROFIT") / 100);
				m.setOperatorsProfit(rs.getDouble("OPERATORS_PROFIT") / 100);
				m.setBplusNetProfit(rs.getDouble("BPLUS_NET_PROFIT") / 100);

				// TEMPO DIM

				tDim.setAllData(rs.getDate("DATA"));

				// CLIENTE DIM

				Gestore g = new Gestore();
				g.setCod_gestore(rs.getString("COD_GESTORE"));

				cDim.setGestore(g);

				// SPAZIO DIM

				Nazione n = new Nazione();
				n.setNome("ITALIA");

				Regione r = new Regione();
				r.setNome(rs.getString("REGIONE"));
				r.setNazioni(n);

				Provincia p = new Provincia();
				p.setSigla(rs.getString("PROVINCIA"));
				p.setRegioni(r);

				Comune c = new Comune();
				c.setNome(rs.getString("COMUNE"));
				c.setProvincia(p);

				sDim.setNazione(n);
				sDim.setComune(c);
				sDim.setProvincia(p);
				sDim.setRegione(r);

				// SYSTEMA GIOCO DIM

				Location l = new Location();
				l.setCOMMERCIAL_NAME(rs.getString("LOCATION_NAME"));
				l.setTOPONIMO(rs.getString("TOPONIMO"));
				l.setADDRESS(rs.getString("LOCATION_ADDRESS"));
				l.setSTREET_NUMBER(rs.getString("NUMERO_CIVICO"));
				l.setAAMS_LOCATION_ID(rs.getString("AAMS_LOCATION_ID"));

				sysDim.setAAMS_GAMESYSTEM_ID(rs.getLong("AAMS_GAMESYSTEM_ID"));
				sysDim.setLoc(l);

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);

			}
			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_locations: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_locations ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_locations failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public List<MeterfactLocation> retrieveMeter_locations_hour(Date data1,
			Date data2, String sysGame) throws DataAccessException {

		List<MeterfactLocation> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*, loc.COMMERCIAL_NAME as LOCATION_NAME, loc.ADDRESS as LOCATION_ADDRESS, "
					+ "loc.STREET_NUMBER as NUMERO_CIVICO, loc.AAMS_LOCATION_CODE as AAMS_LOCATION_ID, "
					+ "top.DESCRIPTION as TOPONIMO, reg.NOME as REGIONE, prov.SIGLA as PROVINCIA, "
					+ "com.NOME as COMUNE from ( select sum(this_.BET) as BET, sum(this_.WIN) as WIN, "
					+ "sum(this_.GAMES_PLAYED) as GAMES_PLAYED, sum(this_.JACKPOT_WIN) as JACKPOT_WINS, "
					+ "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
					+ "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
					+ "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, "
					+ "sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, sum(total_in) TOTAL_IN, "
					+ "sum(total_out) TOTAL_OUT,sum(ticket_in) TICKET_IN,sum(ticket_out) TICKET_OUT, "
					+ "sum(coin_in) COIN_IN, sum(bill_in) BILL_IN,sum(card_in) CARD_IN,sum(prepaid_in) TOTAL_PREPAID_IN, "
					+ "sum(HANDPAY) HANDPAY,this_.DATA,tempodim6_.ORA, this_.AAMS_LOCATION_CODE, clientidim6_.COD_GESTORE, "
					+ "this_.AAMS_GAMESYSTEM_CODE AAMS_GAMESYSTEM_ID from meter_location_hour this_ "
					+ "inner join CLIENTEDIM clientidim6_ on this_.DMCLIENTE_ID=clientidim6_.ID "
					+ "inner join TEMPODIM tempodim6_ on this_.DMTEMPO_ID = tempodim6_.ID "
					+ "WHERE this_.AAMS_GAMESYSTEM_CODe like ? and this_.DATA between ? and  ? "
					+ "GROUP BY this_.DATA,tempodim6_.ORA,this_.AAMS_LOCATION_CODE,this_.AAMS_GAMESYSTEM_CODE ORDER BY ORA DESC ) x "
					+ "inner join aggregate.birslocation loc on x.AAMS_LOCATION_CODE = loc.AAMS_LOCATION_CODE "
					+ "inner join aggregate.birscomuni com on loc.CADASTRAL_CODE = com.CADASTRAL_CODE "
					+ "inner join aggregate.birsprovince prov on com.ID_PROV=prov.ID_PROV "
					+ "inner join aggregate.birstoponimo top on loc.ID_TOPONIMO = top.ID "
					+ "inner join aggregate.birsregioni reg on prov.ID_REG=reg.ID_REG";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setString(1, sysGame);
			ps.setDate(2, data1_);
			ps.setTimestamp(3, data2_);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactLocation>();

			while (rs.next()) {
				MeterfactLocation m = new MeterfactLocation();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setTotalIn(rs.getDouble("TOTAL_IN") / 100);
				m.setTotalOut(rs.getDouble("TOTAL_OUT") / 100);
				m.setTicketIn(rs.getDouble("TICKET_IN") / 100);
				m.setTicketOut(rs.getDouble("TICKET_OUT") / 100);
				m.setCoinIn(rs.getDouble("COIN_IN") / 100);
				m.setBillIn(rs.getDouble("BILL_IN") / 100);
				m.setCardIn(rs.getDouble("CARD_IN") / 100);
				m.setPrepaidIn(rs.getDouble("TOTAL_PREPAID_IN") / 100);
				m.setHandpay(rs.getDouble("HANDPAY") / 100);
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);
				m.setPreu(rs.getDouble("PREU") / 100);
				m.setAams(rs.getDouble("AAMS") / 100);
				m.setNetWin(rs.getDouble("NET_WIN") / 100);
				m.setHouseWin(rs.getDouble("HOUSE_WIN") / 100);
				m.setSupplierProfit(rs.getDouble("SUPPLIER_PROFIT") / 100);
				m.setOperatorsProfit(rs.getDouble("OPERATORS_PROFIT") / 100);
				m.setBplusNetProfit(rs.getDouble("BPLUS_NET_PROFIT") / 100);

				// TEMPO DIM

				tDim.setAllData(rs.getDate("DATA"));
				tDim.setHour(rs.getInt("ORA"));

				// CLIENTE DIM

				Gestore g = new Gestore();
				g.setCod_gestore(rs.getString("COD_GESTORE"));

				cDim.setGestore(g);

				// SPAZIO DIM

				Nazione n = new Nazione();
				n.setNome("ITALIA");

				Regione r = new Regione();
				r.setNome(rs.getString("REGIONE"));
				r.setNazioni(n);

				Provincia p = new Provincia();
				p.setSigla(rs.getString("PROVINCIA"));
				p.setRegioni(r);

				Comune c = new Comune();
				c.setNome(rs.getString("COMUNE"));
				c.setProvincia(p);

				sDim.setNazione(n);
				sDim.setComune(c);
				sDim.setProvincia(p);
				sDim.setRegione(r);

				// SYSTEMA GIOCO DIM

				Location l = new Location();
				l.setCOMMERCIAL_NAME(rs.getString("LOCATION_NAME"));
				l.setTOPONIMO(rs.getString("TOPONIMO"));
				l.setADDRESS(rs.getString("LOCATION_ADDRESS"));
				l.setSTREET_NUMBER(rs.getString("NUMERO_CIVICO"));
				l.setAAMS_LOCATION_ID(rs.getString("AAMS_LOCATION_ID"));

				sysDim.setAAMS_GAMESYSTEM_ID(rs.getLong("AAMS_GAMESYSTEM_ID"));
				sysDim.setLoc(l);

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);

			}
			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_locations_hour: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_locations_hour ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_locations_hour failed", e);
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

	public List<MeterfactLocation> retrieveMeter_location(Date data1,
			Date data2, String aams_location_id) throws DataAccessException {

		List<MeterfactLocation> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*, loc.COMMERCIAL_NAME as LOCATION_NAME, loc.ADDRESS as LOCATION_ADDRESS,loc.STREET_NUMBER as NUMERO_CIVICO,"
					+ "loc.AAMS_LOCATION_CODE as AAMS_LOCATION_ID,top.DESCRIPTION as TOPONIMO, "
					+ "reg.NOME as REGIONE, prov.SIGLA as PROVINCIA, com.NOME as COMUNE from ( "
					+ "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
					+ "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
					+ "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
					+ "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, "
					+ "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, "
					+ "sum(total_in) TOTAL_IN,sum(total_out) TOTAL_OUT,sum(ticket_in) TICKET_IN,sum(ticket_out) TICKET_OUT,sum(coin_in) COIN_IN, "
					+ "sum(bill_in) BILL_IN,sum(card_in) CARD_IN,sum(prepaid_in) TOTAL_PREPAID_IN, "
					+ "this_.DATA, this_.AAMS_LOCATION_CODE, clientidim6_.COD_GESTORE, "
					+ "this_.AAMS_GAMESYSTEM_CODE AAMS_GAMESYSTEM_ID "
					+ "from meter_location this_ "
					+ "inner join CLIENTEDIM clientidim6_ on this_.DMCLIENTE_ID=clientidim6_.ID "
					+ "WHERE this_.AAMS_LOCATION_CODE like ? "
					+ "and this_.DATA between ? and  ? "
					+ "GROUP BY this_.DATA,this_.AAMS_LOCATION_CODE,this_.AAMS_GAMESYSTEM_CODE ) x "
					+ "inner join aggregate.birslocation loc on x.AAMS_LOCATION_CODE = loc.AAMS_LOCATION_CODE "
					+ "inner join aggregate.birscomuni com on loc.CADASTRAL_CODE = com.CADASTRAL_CODE  "
					+ "inner join aggregate.birsprovince prov on com.ID_PROV=prov.ID_PROV  "
					+ "inner join aggregate.birstoponimo top on loc.ID_TOPONIMO = top.ID  "
					+ "inner join aggregate.birsregioni reg on prov.ID_REG=reg.ID_REG";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setString(1, aams_location_id);
			ps.setDate(2, data1_);
			ps.setTimestamp(3, data2_);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactLocation>();

			while (rs.next()) {
				MeterfactLocation m = new MeterfactLocation();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setTotalIn(rs.getDouble("TOTAL_IN") / 100);
				m.setTotalOut(rs.getDouble("TOTAL_OUT") / 100);
				m.setTicketIn(rs.getDouble("TICKET_IN") / 100);
				m.setTicketOut(rs.getDouble("TICKET_OUT") / 100);
				m.setCoinIn(rs.getDouble("COIN_IN") / 100);
				m.setBillIn(rs.getDouble("BILL_IN") / 100);
				m.setCardIn(rs.getDouble("CARD_IN") / 100);
				m.setPrepaidIn(rs.getDouble("TOTAL_PREPAID_IN") / 100);
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);
				m.setPreu(rs.getDouble("PREU") / 100);
				m.setAams(rs.getDouble("AAMS") / 100);
				m.setNetWin(rs.getDouble("NET_WIN") / 100);
				m.setHouseWin(rs.getDouble("HOUSE_WIN") / 100);
				m.setSupplierProfit(rs.getDouble("SUPPLIER_PROFIT") / 100);
				m.setOperatorsProfit(rs.getDouble("OPERATORS_PROFIT") / 100);
				m.setBplusNetProfit(rs.getDouble("BPLUS_NET_PROFIT") / 100);

				// TEMPO DIM

				tDim.setAllData(rs.getDate("DATA"));

				// CLIENTE DIM

				Gestore g = new Gestore();
				g.setCod_gestore(rs.getString("COD_GESTORE"));

				cDim.setGestore(g);

				// SPAZIO DIM

				Nazione n = new Nazione();
				n.setNome("ITALIA");

				Regione r = new Regione();
				r.setNome(rs.getString("REGIONE"));
				r.setNazioni(n);

				Provincia p = new Provincia();
				p.setSigla(rs.getString("PROVINCIA"));
				p.setRegioni(r);

				Comune c = new Comune();
				c.setNome(rs.getString("COMUNE"));
				c.setProvincia(p);

				sDim.setNazione(n);
				sDim.setComune(c);
				sDim.setProvincia(p);
				sDim.setRegione(r);

				// SYSTEMA GIOCO DIM

				Location l = new Location();
				l.setCOMMERCIAL_NAME(rs.getString("LOCATION_NAME"));
				l.setTOPONIMO(rs.getString("TOPONIMO"));
				l.setADDRESS(rs.getString("LOCATION_ADDRESS"));
				l.setSTREET_NUMBER(rs.getString("NUMERO_CIVICO"));
				l.setAAMS_LOCATION_ID(rs.getString("AAMS_LOCATION_ID"));

				sysDim.setAAMS_GAMESYSTEM_ID(rs.getLong("AAMS_GAMESYSTEM_ID"));
				sysDim.setLoc(l);

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);

			}
			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_location: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_location ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_location failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public List<MeterfactVlt> retrieveMeter_vlts(Date data1, Date data2,
			String sysGame) throws DataAccessException {

		List<MeterfactVlt> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*, loc.COMMERCIAL_NAME as LOCATION_NAME, loc.ADDRESS as LOCATION_ADDRESS,loc.STREET_NUMBER as NUMERO_CIVICO, "
					+ "loc.AAMS_LOCATION_CODE as AAMS_LOCATION_ID,top.DESCRIPTION as TOPONIMO, "
					+ "reg.NOME as REGIONE, prov.SIGLA as PROVINCIA, com.NOME as COMUNE, vlt.AAMS_VLT_MODEL_CODE  from ( "
					+ "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
					+ "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
					+ "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
					+ "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, "
					+ "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, "
					+ "count(distinct(this_.AAMS_VLT_CODE)) as NUM_VLT, "
					+ "sum(total_in) TOTAL_IN,sum(total_out) TOTAL_OUT,sum(ticket_in) TICKET_IN,sum(ticket_out) TICKET_OUT,sum(coin_in) COIN_IN, "
					+ "sum(bill_in) BILL_IN,sum(card_in) CARD_IN,sum(prepaid_in) TOTAL_PREPAID_IN,sum(HANDPAY) HANDPAY, "
					+ "this_.DATA, this_.AAMS_LOCATION_CODE, clientidim6_.COD_GESTORE, "
					+ "this_.AAMS_GAMESYSTEM_CODE AAMS_GAMESYSTEM_ID,this_.AAMS_VLT_CODE AAMS_VLT_ID "
					+ "from meter_vlt this_ "
					+ "inner join CLIENTEDIM clientidim6_ on this_.DMCLIENTE_ID=clientidim6_.ID "
					+ "WHERE this_.AAMS_GAMESYSTEM_CODe like ? "
					+ "and this_.DATA between ? and  ? "
					+ "GROUP BY this_.DATA,this_.AAMS_VLT_CODE,this_.AAMS_LOCATION_CODE,this_.AAMS_GAMESYSTEM_CODE ) x "
					+ "inner join aggregate.birsvlt vlt on x.AAMS_VLT_ID = vlt.AAMS_VLT_CODE "
					+ "inner join aggregate.birslocation loc on x.AAMS_LOCATION_CODE = loc.AAMS_LOCATION_CODE "
					+ "inner join aggregate.birscomuni com on loc.CADASTRAL_CODE = com.CADASTRAL_CODE  "
					+ "inner join aggregate.birsprovince prov on com.ID_PROV=prov.ID_PROV "
					+ "inner join aggregate.birstoponimo top on loc.ID_TOPONIMO = top.ID "
					+ "inner join aggregate.birsregioni reg on prov.ID_REG=reg.ID_REG";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setString(1, sysGame);
			ps.setDate(2, data1_);
			ps.setTimestamp(3, data2_);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactVlt>();

			while (rs.next()) {
				MeterfactVlt m = new MeterfactVlt();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setTotalIn(rs.getDouble("TOTAL_IN") / 100);
				m.setTotalOut(rs.getDouble("TOTAL_OUT") / 100);
				m.setTicketIn(rs.getDouble("TICKET_IN") / 100);
				m.setTicketOut(rs.getDouble("TICKET_OUT") / 100);
				m.setCoinIn(rs.getDouble("COIN_IN") / 100);
				m.setBillIn(rs.getDouble("BILL_IN") / 100);
				m.setCardIn(rs.getDouble("CARD_IN") / 100);
				m.setPrepaidIn(rs.getDouble("TOTAL_PREPAID_IN") / 100);
				m.setHandpay(rs.getDouble("HANDPAY") / 100);
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);
				m.setPreu(rs.getDouble("PREU") / 100);
				m.setAams(rs.getDouble("AAMS") / 100);
				m.setNetWin(rs.getDouble("NET_WIN") / 100);
				m.setHouseWin(rs.getDouble("HOUSE_WIN") / 100);
				m.setSupplierProfit(rs.getDouble("SUPPLIER_PROFIT") / 100);
				m.setOperatorsProfit(rs.getDouble("OPERATORS_PROFIT") / 100);
				m.setBplusNetProfit(rs.getDouble("BPLUS_NET_PROFIT") / 100);

				// TEMPO DIM

				tDim.setAllData(rs.getDate("DATA"));

				// CLIENTE DIM

				Gestore g = new Gestore();
				g.setCod_gestore(rs.getString("COD_GESTORE"));

				cDim.setGestore(g);

				// SPAZIO DIM

				Nazione n = new Nazione();
				n.setNome("ITALIA");

				Regione r = new Regione();
				r.setNome(rs.getString("REGIONE"));
				r.setNazioni(n);

				Provincia p = new Provincia();
				p.setSigla(rs.getString("PROVINCIA"));
				p.setRegioni(r);

				Comune c = new Comune();
				c.setNome(rs.getString("COMUNE"));
				c.setProvincia(p);

				sDim.setNazione(n);
				sDim.setComune(c);
				sDim.setProvincia(p);
				sDim.setRegione(r);

				// SYSTEMA GIOCO DIM

				Location l = new Location();
				l.setCOMMERCIAL_NAME(rs.getString("LOCATION_NAME"));
				l.setTOPONIMO(rs.getString("TOPONIMO"));
				l.setADDRESS(rs.getString("LOCATION_ADDRESS"));
				l.setSTREET_NUMBER(rs.getString("NUMERO_CIVICO"));
				l.setAAMS_LOCATION_ID(rs.getString("AAMS_LOCATION_ID"));

				Vlt v = new Vlt();
				v.setAAMS_VLT_ID(rs.getString("AAMS_VLT_ID"));
				v.setVLT_MODEL_ID(rs.getInt("AAMS_VLT_MODEL_CODE"));

				sysDim.setAAMS_GAMESYSTEM_ID(rs.getLong("AAMS_GAMESYSTEM_ID"));
				sysDim.setVlt(v);
				sysDim.setLoc(l);

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_vlts: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_vlts ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_vlts failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public List<MeterfactVlt> retrieveMeter_vlt(Date data1, Date data2,
			String aams_vlt_id) throws DataAccessException {

		List<MeterfactVlt> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*, loc.COMMERCIAL_NAME as LOCATION_NAME, loc.ADDRESS as LOCATION_ADDRESS,loc.STREET_NUMBER as NUMERO_CIVICO, "
					+ "loc.AAMS_LOCATION_CODE as AAMS_LOCATION_ID,top.DESCRIPTION as TOPONIMO, "
					+ "reg.NOME as REGIONE, prov.SIGLA as PROVINCIA, com.NOME as COMUNE from ( "
					+ "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
					+ "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
					+ "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
					+ "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, "
					+ "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, "
					+ "count(distinct(this_.AAMS_VLT_CODE)) as NUM_VLT, "
					+ "sum(total_in) TOTAL_IN,sum(total_out) TOTAL_OUT,sum(ticket_in) TICKET_IN,sum(ticket_out) TICKET_OUT,sum(coin_in) COIN_IN, "
					+ "sum(bill_in) BILL_IN,sum(card_in) CARD_IN,sum(prepaid_in) TOTAL_PREPAID_IN, "
					+ "this_.DATA, this_.AAMS_LOCATION_CODE, clientidim6_.COD_GESTORE, "
					+ "this_.AAMS_GAMESYSTEM_CODE AAMS_GAMESYSTEM_ID, this_.AAMS_VLT_CODE AAMS_VLT_ID  "
					+ "from meter_vlt this_ "
					+ "inner join CLIENTEDIM clientidim6_ on this_.DMCLIENTE_ID=clientidim6_.ID "
					+ "WHERE this_.AAMS_VLT_CODE like ? "
					+ "and this_.DATA between ? and  ? "
					+ "GROUP BY this_.DATA,this_.AAMS_VLT_CODE,this_.AAMS_LOCATION_CODE,this_.AAMS_GAMESYSTEM_CODE ) x "
					+ "inner join aggregate.birslocation loc on x.AAMS_LOCATION_CODE = loc.AAMS_LOCATION_CODE "
					+ "inner join aggregate.birscomuni com on loc.CADASTRAL_CODE = com.CADASTRAL_CODE "
					+ "inner join aggregate.birsprovince prov on com.ID_PROV=prov.ID_PROV "
					+ "inner join aggregate.birstoponimo top on loc.ID_TOPONIMO = top.ID "
					+ "inner join aggregate.birsregioni reg on prov.ID_REG=reg.ID_REG";

			/*
			 * "select x.*,y.*, loc.COMMERCIAL_NAME as LOCATION_NAME, loc.ADDRESS as LOCATION_ADDRESS,loc.STREET_NUMBER as NUMERO_CIVICO,"
			 * +
			 * "loc.AAMS_LOCATION_CODE as AAMS_LOCATION_ID,top.DESCRIPTION as TOPONIMO,"
			 * +
			 * "reg.NOME as REGIONE, prov.SIGLA as PROVINCIA, com.NOME as COMUNE from ( select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED,"
			 * +
			 * "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU,"
			 * +
			 * "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, "
			 * +
			 * "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, count(distinct(sysgiocodim4_.AAMS_VLT_CODE)) as NUM_VLT,"
			 * +
			 * "date(tempodim5_.DATA) as DATA, sysgiocodim4_.AAMS_LOCATION_CODE, clientidim6_.COD_GESTORE, "
			 * + "sysgiocodim4_.AAMS_GAMESYSTEM_CODE as AAMS_GAMESYSTEM_ID, " +
			 * "sysgiocodim4_.AAMS_VLT_CODE as AAMS_VLT_ID "+
			 * "from METERFACT this_ " +
			 * "inner join `importlog` import on import.UNIQUE_SESSION_ID=this_.UNIQUE_SESSION_ID "
			 * +
			 * "and import.UNIQUE_SESSION_ID in (Select max(ii.UNIQUE_SESSION_ID) from importlog ii "
			 * +
			 * "where START_DATE between import.START_DATE and import.END_DATE group by AAMS_GAME_SYSTEM_CODE) "
			 * +
			 * "inner join TEMPODIM tempodim5_ on this_.DMTEMPO_ID=tempodim5_.ID "
			 * +
			 * "inner join CLIENTEDIM clientidim6_ on this_.DMCLIENTE_ID=clientidim6_.ID "
			 * +
			 * "inner join SPAZIODIM spaziodim1_ on this_.DMSPAZIO_ID=spaziodim1_.ID "
			 * +
			 * "inner join SISTEMAGIOCODIM sysgiocodim4_ on this_.DMSISTEMAGIOCO_ID=sysgiocodim4_.ID "
			 * + "WHERE sysgiocodim4_.AAMS_VLT_CODE like ? "+
			 * "and tempodim5_.DATA between ? and  ? "+
			 * "and import.START_DATE between ?  and  ? "+
			 * "GROUP BY date(tempodim5_.DATA),sysgiocodim4_.AAMS_VLT_CODE,sysgiocodim4_.AAMS_LOCATION_CODE,sysgiocodim4_.AAMS_GAMESYSTEM_CODE ) x "
			 * + "inner join ( "+
			 * "select  me.data, sum(total_in) TOTAL_IN,sum(total_out) TOTAL_OUT,sum(ticket_in) TICKET_IN,sum(ticket_out) TICKET_OUT,sum(coin_in) COIN_IN, "
			 * +
			 * "sum(bill_in) BILL_IN,sum(card_in) CARD_IN,sum(total_prepaid_in) TOTAL_PREPAID_IN,"
			 * +
			 * "me.aams_location_code,me.aams_vlt_code, me.aams_gamesystem_code "
			 * + "from (" +
			 * "select me.data, avg(total_in) total_in,avg(total_out) total_out,avg(ticket_in) ticket_in, "
			 * +
			 * "avg(ticket_out) ticket_out,avg(coin_in) coin_in,avg(bill_in) bill_in,avg(card_in) card_in, "
			 * +
			 * "avg(prepaid_in) total_prepaid_in, me.unique_session_id, s.aams_location_code, s.aams_gamesystem_code, s.aams_vlt_code "
			 * + "from METERFACT me, sistemagiocodim s " +
			 * "where me.data between ? and ? " +
			 * "and me.DMSISTEMAGIOCO_ID = s.ID "+
			 * "group by me.data, me.unique_session_id, s.aams_location_code, s.aams_gamesystem_code , s.aams_vlt_code ) me "
			 * +
			 * "group by me.data, me.aams_location_code, me.aams_gamesystem_code,me.aams_vlt_code ) y "
			 * +
			 * "on x.data = y.data and x.AAMS_GAMESYSTEM_ID = y.AAMS_GAMESYSTEM_code and x.aams_location_code = y.AAMS_LOCATION_CODE and x.AAMS_VLT_ID = y.aams_vlt_code "
			 * +
			 * "inner join aggregate.birslocation loc on x.AAMS_LOCATION_CODE = loc.AAMS_LOCATION_CODE "
			 * +
			 * "inner join aggregate.birscomuni com on loc.CADASTRAL_CODE = com.CADASTRAL_CODE "
			 * +
			 * "inner join aggregate.birsprovince prov on com.ID_PROV=prov.ID_PROV "
			 * +
			 * "inner join aggregate.birstoponimo top on loc.ID_TOPONIMO = top.ID "
			 * +
			 * "inner join aggregate.birsregioni reg on prov.ID_REG=reg.ID_REG";
			 */

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setString(1, aams_vlt_id);
			ps.setDate(2, data1_);
			ps.setTimestamp(3, data2_);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactVlt>();

			while (rs.next()) {
				MeterfactVlt m = new MeterfactVlt();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setTotalIn(rs.getDouble("TOTAL_IN") / 100);
				m.setTotalOut(rs.getDouble("TOTAL_OUT") / 100);
				m.setTicketIn(rs.getDouble("TICKET_IN") / 100);
				m.setTicketOut(rs.getDouble("TICKET_OUT") / 100);
				m.setCoinIn(rs.getDouble("COIN_IN") / 100);
				m.setBillIn(rs.getDouble("BILL_IN") / 100);
				m.setCardIn(rs.getDouble("CARD_IN") / 100);
				m.setPrepaidIn(rs.getDouble("TOTAL_PREPAID_IN") / 100);
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);
				m.setPreu(rs.getDouble("PREU") / 100);
				m.setAams(rs.getDouble("AAMS") / 100);
				m.setNetWin(rs.getDouble("NET_WIN") / 100);
				m.setHouseWin(rs.getDouble("HOUSE_WIN") / 100);
				m.setSupplierProfit(rs.getDouble("SUPPLIER_PROFIT") / 100);
				m.setOperatorsProfit(rs.getDouble("OPERATORS_PROFIT") / 100);
				m.setBplusNetProfit(rs.getDouble("BPLUS_NET_PROFIT") / 100);

				// TEMPO DIM

				tDim.setAllData(rs.getDate("DATA"));

				// CLIENTE DIM

				Gestore g = new Gestore();
				g.setCod_gestore(rs.getString("COD_GESTORE"));

				cDim.setGestore(g);

				// SPAZIO DIM

				Nazione n = new Nazione();
				n.setNome("ITALIA");

				Regione r = new Regione();
				r.setNome(rs.getString("REGIONE"));
				r.setNazioni(n);

				Provincia p = new Provincia();
				p.setSigla(rs.getString("PROVINCIA"));
				p.setRegioni(r);

				Comune c = new Comune();
				c.setNome(rs.getString("COMUNE"));
				c.setProvincia(p);

				sDim.setNazione(n);
				sDim.setComune(c);
				sDim.setProvincia(p);
				sDim.setRegione(r);

				// SYSTEMA GIOCO DIM

				Location l = new Location();
				l.setCOMMERCIAL_NAME(rs.getString("LOCATION_NAME"));
				l.setTOPONIMO(rs.getString("TOPONIMO"));
				l.setADDRESS(rs.getString("LOCATION_ADDRESS"));
				l.setSTREET_NUMBER(rs.getString("NUMERO_CIVICO"));
				l.setAAMS_LOCATION_ID(rs.getString("AAMS_LOCATION_ID"));

				Vlt v = new Vlt();
				v.setAAMS_VLT_ID(rs.getString("AAMS_VLT_ID"));

				sysDim.setAAMS_GAMESYSTEM_ID(rs.getLong("AAMS_GAMESYSTEM_ID"));
				sysDim.setVlt(v);
				sysDim.setLoc(l);

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_vlts: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_vlts ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_vlts failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public List<MeterfactGame> retrieveMeter_games_chart(Date data1,
			Date data2, String sysGame) throws DataAccessException {

		List<MeterfactGame> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*,game.NAME as GAME_NAME from (select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
					+ "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, "
					+ "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, "
					+ "this_.AAMS_GAME_CODE as AAMS_GAME_ID, "
					+ "this_.DATA as DATA from meter_games this_ "
					+ "WHERE this_.AAMS_GAME_CODE != -1 and this_.DATA between ? and  ? and AAMS_GAMESYSTEM_CODE like ? and BET>0 "
					+ "GROUP BY this_.AAMS_GAME_CODE,this_.DATA "
					+ "ORDER BY AAMS_GAME_CODE,DATA) x "
					+ "inner join aggregate.birsgames game on x.AAMS_GAME_ID=game.AAMS_GAME_CODE";

			/*
			 * "select x.*,game.NAME as GAME_NAME from (select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
			 * + "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, " +
			 * "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION," +
			 * "sysgiocodim4_.AAMS_GAME_CODE as AAMS_GAME_ID," +
			 * "tempodim5_.DATA as DATA from METERFACT this_  " +
			 * "inner join TEMPODIM tempodim5_ on this_.DMTEMPO_ID=tempodim5_.ID  "
			 * +
			 * "inner join SISTEMAGIOCODIM sysgiocodim4_ on this_.DMSISTEMAGIOCO_ID=sysgiocodim4_.ID  "
			 * +
			 * "WHERE sysgiocodim4_.AAMS_GAME_CODE != -1 and tempodim5_.DATA between ? and  ? "
			 * + "GROUP BY sysgiocodim4_.AAMS_GAME_CODE,date(tempodim5_.DATA) "
			 * + "ORDER BY AAMS_GAME_CODE,DATA) x " +
			 * "inner join aggregate.birsgames game on x.AAMS_GAME_ID=game.AAMS_GAME_CODE "
			 * ;
			 */

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setDate(1, data1_);
			ps.setTimestamp(2, data2_);
			ps.setString(3, sysGame);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactGame>();

			while (rs.next()) {
				MeterfactGame m = new MeterfactGame();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);

				// TEMPO DIM
				tDim.setAllData(rs.getDate("DATA"));

				// SPAZIO DIM

				Game game = new Game();
				game.setAAMS_GAME_ID(rs.getLong("AAMS_GAME_ID"));
				game.setNAME(rs.getString("GAME_NAME"));

				sysDim.setGame(game);

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_games_chart: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_games_chart ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_games_chart failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public List<MeterfactGame> retrieveMeter_games_chart_by_location(
			Date data1, Date data2, String aams_location_code, String sysGame)
			throws DataAccessException {

		List<MeterfactGame> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*,game.NAME as GAME_NAME from (select AAMS_LOCATION_CODE,sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
					+ "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, "
					+ "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, "
					+ "this_.AAMS_GAME_CODE as AAMS_GAME_ID, "
					+ "this_.DATA as DATA from meter_games this_ "
					+ "WHERE this_.AAMS_GAME_CODE != -1 and this_.DATA between ? and  ? and AAMS_LOCATION_CODE like ? and AAMS_GAMESYSTEM_CODE like ? and BET>0 "
					+ "GROUP BY this_.AAMS_GAME_CODE,this_.DATA "
					+ "ORDER BY AAMS_GAME_CODE,DATA) x "
					+ "inner join aggregate.birsgames game on x.AAMS_GAME_ID=game.AAMS_GAME_CODE";

			/*
			 * "select x.*,game.NAME as GAME_NAME from (select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
			 * + "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, " +
			 * "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION," +
			 * "sysgiocodim4_.AAMS_GAME_CODE as AAMS_GAME_ID," +
			 * "tempodim5_.DATA as DATA from METERFACT this_  " +
			 * "inner join TEMPODIM tempodim5_ on this_.DMTEMPO_ID=tempodim5_.ID  "
			 * +
			 * "inner join SISTEMAGIOCODIM sysgiocodim4_ on this_.DMSISTEMAGIOCO_ID=sysgiocodim4_.ID  "
			 * +
			 * "WHERE sysgiocodim4_.AAMS_GAME_CODE != -1 and tempodim5_.DATA between ? and  ? "
			 * + "GROUP BY sysgiocodim4_.AAMS_GAME_CODE,date(tempodim5_.DATA) "
			 * + "ORDER BY AAMS_GAME_CODE,DATA) x " +
			 * "inner join aggregate.birsgames game on x.AAMS_GAME_ID=game.AAMS_GAME_CODE "
			 * ;
			 */

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setDate(1, data1_);
			ps.setTimestamp(2, data2_);
			ps.setString(3, aams_location_code);
			ps.setString(4, sysGame);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactGame>();

			while (rs.next()) {
				MeterfactGame m = new MeterfactGame();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);

				// TEMPO DIM
				tDim.setAllData(rs.getDate("DATA"));

				// SPAZIO DIM

				Game game = new Game();
				game.setAAMS_GAME_ID(rs.getLong("AAMS_GAME_ID"));
				game.setNAME(rs.getString("GAME_NAME"));

				sysDim.setGame(game);

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_games_chart: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_games_chart ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_games_chart failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public List<MeterfactGame> retrieveMeter_games(Date data1, Date data2,
			String sysGame) throws DataAccessException {

		List<MeterfactGame> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*, loc.COMMERCIAL_NAME as LOCATION_NAME, loc.ADDRESS as LOCATION_ADDRESS,loc.STREET_NUMBER as NUMERO_CIVICO, "
					+ "loc.AAMS_LOCATION_CODE as AAMS_LOCATION_ID,game.NAME as NAME,game.GS_GAMES_CODE as GS_GAME_CODE,top.DESCRIPTION as TOPONIMO, "
					+ "reg.NOME as REGIONE, prov.SIGLA as PROVINCIA, com.NOME as COMUNE from ( "
					+ "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
					+ "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, "
					+ "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
					+ "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
					+ "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, "
					+ "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, "
					+ "sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, "
					+ "this_.DATA, this_.AAMS_GAME_CODE as AAMS_GAME_ID, "
					+ "this_.AAMS_VLT_code as AAMS_VLT_ID, this_.AAMS_LOCATION_CODE, "
					+ "this_.AAMS_GAMESYSTEM_CODE, clientidim6_.COD_GESTORE, "
					+ "this_.AAMS_GAMESYSTEM_CODE as AAMS_GAMESYSTEM_ID "
					+ "from meter_games this_ "
					+ "inner join CLIENTEDIM clientidim6_ on this_.DMCLIENTE_ID=clientidim6_.ID "
					+ "WHERE this_.AAMS_GAMESYSTEM_CODe like ? and this_.AAMS_GAME_CODE != -1 "
					+ "and this_.DATA between ? and  ? "
					+ "GROUP BY this_.DATA,this_.AAMS_GAME_CODE, this_.AAMS_VLT_CODE,this_.AAMS_LOCATION_CODE,this_.AAMS_GAMESYSTEM_CODE ) x "
					+ "inner join aggregate.birslocation loc on x.AAMS_LOCATION_CODE = loc.AAMS_LOCATION_CODE "
					+ "inner join aggregate.birscomuni com on loc.CADASTRAL_CODE = com.CADASTRAL_CODE "
					+ "inner join aggregate.birsprovince prov on com.ID_PROV=prov.ID_PROV "
					+ "inner join aggregate.birstoponimo top on loc.ID_TOPONIMO = top.ID "
					+ "inner join aggregate.birsregioni reg on prov.ID_REG=reg.ID_REG "
					+ "inner join aggregate.birsgames game on game.AAMS_GAME_CODE=x.AAMS_GAME_ID";

			/*
			 * "select x.*, loc.COMMERCIAL_NAME as LOCATION_NAME, loc.ADDRESS as LOCATION_ADDRESS,loc.STREET_NUMBER as NUMERO_CIVICO, "
			 * +
			 * "loc.AAMS_LOCATION_CODE as AAMS_LOCATION_ID,game.NAME as NAME,game.GS_GAMES_CODE as GS_GAME_CODE,top.DESCRIPTION as TOPONIMO, "
			 * +
			 * "reg.NOME as REGIONE, prov.SIGLA as PROVINCIA, com.NOME as COMUNE from ( "
			 * +
			 * "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
			 * + "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, " +
			 * "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
			 * +
			 * "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
			 * + "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, " +
			 * "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, " +
			 * "sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, " +
			 * "date(tempodim5_.DATA) as DATA, " +
			 * "sysgiocodim4_.AAMS_GAME_CODE as AAMS_GAME_ID, " +
			 * "sysgiocodim4_.AAMS_VLT_code as AAMS_VLT_ID, " +
			 * "sysgiocodim4_.AAMS_LOCATION_CODE, " +
			 * "sysgiocodim4_.AAMS_GAMESYSTEM_CODE, clientidim6_.COD_GESTORE, "
			 * + "sysgiocodim4_.AAMS_GAMESYSTEM_CODE as AAMS_GAMESYSTEM_ID  " +
			 * "from METERFACT this_ " +
			 * "inner join `importlog` import on import.UNIQUE_SESSION_ID=this_.UNIQUE_SESSION_ID "
			 * +
			 * "and import.UNIQUE_SESSION_ID in (Select max(ii.UNIQUE_SESSION_ID) from importlog ii where START_DATE between import.START_DATE and import.END_DATE group by AAMS_GAME_SYSTEM_CODE) "
			 * +
			 * "inner join TEMPODIM tempodim5_ on this_.DMTEMPO_ID=tempodim5_.ID "
			 * +
			 * "inner join CLIENTEDIM clientidim6_ on this_.DMCLIENTE_ID=clientidim6_.ID "
			 * +
			 * "inner join SPAZIODIM spaziodim1_ on this_.DMSPAZIO_ID=spaziodim1_.ID "
			 * +
			 * "inner join SISTEMAGIOCODIM sysgiocodim4_ on this_.DMSISTEMAGIOCO_ID=sysgiocodim4_.ID "
			 * + "WHERE sysgiocodim4_.AAMS_GAMESYSTEM_CODe like ? " +
			 * "and tempodim5_.DATA between ? and  ? " +
			 * "and import.START_DATE between ? and  ? " +
			 * "GROUP BY date(tempodim5_.DATA),sysgiocodim4_.AAMS_GAME_CODE," +
			 * "sysgiocodim4_.AAMS_VLT_CODE,sysgiocodim4_.AAMS_LOCATION_CODE,sysgiocodim4_.AAMS_GAMESYSTEM_CODE ) x "
			 * +
			 * "inner join aggregate.birslocation loc on x.AAMS_LOCATION_CODE = loc.AAMS_LOCATION_CODE "
			 * +
			 * "inner join aggregate.birscomuni com on loc.CADASTRAL_CODE = com.CADASTRAL_CODE "
			 * +
			 * "inner join aggregate.birsprovince prov on com.ID_PROV=prov.ID_PROV "
			 * +
			 * "inner join aggregate.birstoponimo top on loc.ID_TOPONIMO = top.ID "
			 * +
			 * "inner join aggregate.birsregioni reg on prov.ID_REG=reg.ID_REG "
			 * +
			 * "inner join aggregate.birsgames game on game.AAMS_GAME_CODE=x.AAMS_GAME_ID"
			 * ;
			 */

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setString(1, sysGame);
			ps.setDate(2, data1_);
			ps.setTimestamp(3, data2_);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactGame>();

			while (rs.next()) {
				MeterfactGame m = new MeterfactGame();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);
				m.setPreu(rs.getDouble("PREU") / 100);
				m.setAams(rs.getDouble("AAMS") / 100);
				m.setNetWin(rs.getDouble("NET_WIN") / 100);
				m.setHouseWin(rs.getDouble("HOUSE_WIN") / 100);
				m.setSupplierProfit(rs.getDouble("SUPPLIER_PROFIT") / 100);
				m.setOperatorsProfit(rs.getDouble("OPERATORS_PROFIT") / 100);
				m.setBplusNetProfit(rs.getDouble("BPLUS_NET_PROFIT") / 100);

				// TEMPO DIM
				tDim.setAllData(rs.getDate("DATA"));

				// CLIENTE DIM
				Gestore g = new Gestore();
				g.setCod_gestore(rs.getString("COD_GESTORE"));

				cDim.setGestore(g);

				// SPAZIO DIM
				Nazione n = new Nazione();
				n.setNome("ITALIA");

				Regione r = new Regione();
				r.setNome(rs.getString("REGIONE"));
				r.setNazioni(n);

				Provincia p = new Provincia();
				p.setSigla(rs.getString("PROVINCIA"));
				p.setRegioni(r);

				Comune c = new Comune();
				c.setNome(rs.getString("COMUNE"));
				c.setProvincia(p);

				sDim.setNazione(n);
				sDim.setComune(c);
				sDim.setProvincia(p);
				sDim.setRegione(r);

				// SYSTEMA GIOCO DIM
				Location l = new Location();
				l.setCOMMERCIAL_NAME(rs.getString("LOCATION_NAME"));
				l.setTOPONIMO(rs.getString("TOPONIMO"));
				l.setADDRESS(rs.getString("LOCATION_ADDRESS"));
				l.setSTREET_NUMBER(rs.getString("NUMERO_CIVICO"));
				l.setAAMS_LOCATION_ID(rs.getString("AAMS_LOCATION_ID"));

				Vlt v = new Vlt();
				v.setAAMS_VLT_ID(rs.getString("AAMS_VLT_ID"));

				Game game = new Game();
				game.setGS_GAME_ID(rs.getLong("GS_GAME_CODE"));
				game.setAAMS_GAME_ID(rs.getLong("AAMS_GAME_ID"));
				game.setNAME(rs.getString("NAME"));

				sysDim.setAAMS_GAMESYSTEM_ID(rs.getLong("AAMS_GAMESYSTEM_ID"));
				sysDim.setGame(game);
				sysDim.setVlt(v);
				sysDim.setLoc(l);

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_games: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_games ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_games failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public List<MeterfactGame> retrieveMeter_grouped_games(Date data1,
			Date data2, String sysGame) throws DataAccessException {

		List<MeterfactGame> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*,game.NAME as NAME,game.GS_GAMES_CODE as GS_GAME_CODE,game.DATE_IN as DATE_IN from ( "
					+ "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
					+ "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, "
					+ "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
					+ "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
					+ "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, "
					+ "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, "
					+ "sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, "
					+ "this_.DATA, this_.AAMS_GAME_CODE as AAMS_GAME_ID, "
					+ "this_.AAMS_GAMESYSTEM_CODE as AAMS_GAMESYSTEM_ID "
					+ "from meter_games this_ "
					+ "WHERE this_.AAMS_GAMESYSTEM_CODe like ? and this_.AAMS_GAME_CODE != -1 "
					+ "and this_.DATA between ? and  ? "
					+ "GROUP BY this_.AAMS_GAME_CODE,this_.AAMS_GAMESYSTEM_CODE ) x "
					+ "inner join aggregate.birsgames game on game.AAMS_GAME_CODE=x.AAMS_GAME_ID ";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setString(1, sysGame);
			ps.setDate(2, data1_);
			ps.setTimestamp(3, data2_);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactGame>();

			while (rs.next()) {
				MeterfactGame m = new MeterfactGame();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);
				m.setPreu(rs.getDouble("PREU") / 100);
				m.setAams(rs.getDouble("AAMS") / 100);
				m.setNetWin(rs.getDouble("NET_WIN") / 100);
				m.setHouseWin(rs.getDouble("HOUSE_WIN") / 100);
				m.setSupplierProfit(rs.getDouble("SUPPLIER_PROFIT") / 100);
				m.setOperatorsProfit(rs.getDouble("OPERATORS_PROFIT") / 100);
				m.setBplusNetProfit(rs.getDouble("BPLUS_NET_PROFIT") / 100);

				// TEMPO DIM
				tDim.setAllData(rs.getDate("DATA"));

				// SYSTEMA GIOCO DIM

				Game game = new Game();
				game.setGS_GAME_ID(rs.getLong("GS_GAME_CODE"));
				game.setAAMS_GAME_ID(rs.getLong("AAMS_GAME_ID"));
				game.setNAME(rs.getString("NAME"));
				game.setTs(new java.util.Date(rs.getTimestamp("DATE_IN")
						.getTime()));

				sysDim.setAAMS_GAMESYSTEM_ID(rs.getLong("AAMS_GAMESYSTEM_ID"));
				sysDim.setGame(game);

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_grouped_games: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_grouped_games ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_grouped_games failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public List<MeterfactGame> retrieveMeter_location_games(Date data1,
			Date data2, String sysGame) throws DataAccessException {

		List<MeterfactGame> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*, loc.COMMERCIAL_NAME as LOCATION_NAME, loc.ADDRESS as LOCATION_ADDRESS,loc.STREET_NUMBER as NUMERO_CIVICO, "
					+ "loc.AAMS_LOCATION_CODE as AAMS_LOCATION_ID,game.NAME as NAME,game.GS_GAMES_CODE as GS_GAME_CODE,top.DESCRIPTION as TOPONIMO, "
					+ "reg.NOME as REGIONE, prov.SIGLA as PROVINCIA, com.NOME as COMUNE from ( "
					+ "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
					+ "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, "
					+ "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
					+ "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
					+ "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, "
					+ "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, "
					+ "sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, "
					+ "this_.DATA, this_.AAMS_GAME_CODE as AAMS_GAME_ID, "
					+ "this_.AAMS_LOCATION_CODE, "
					+ "clientidim6_.COD_GESTORE, "
					+ "this_.AAMS_GAMESYSTEM_CODE as AAMS_GAMESYSTEM_ID "
					+ "from meter_games this_ "
					+ "inner join CLIENTEDIM clientidim6_ on this_.DMCLIENTE_ID=clientidim6_.ID "
					+ "WHERE this_.AAMS_GAMESYSTEM_CODe like ? and this_.AAMS_GAME_CODE != -1 "
					+ "and this_.DATA between ? and  ? "
					+ "GROUP BY this_.DATA,this_.AAMS_GAME_CODE, this_.AAMS_LOCATION_CODE,this_.AAMS_GAMESYSTEM_CODE ) x "
					+ "inner join aggregate.birslocation loc on x.AAMS_LOCATION_CODE = loc.AAMS_LOCATION_CODE "
					+ "inner join aggregate.birscomuni com on loc.CADASTRAL_CODE = com.CADASTRAL_CODE "
					+ "inner join aggregate.birsprovince prov on com.ID_PROV=prov.ID_PROV "
					+ "inner join aggregate.birstoponimo top on loc.ID_TOPONIMO = top.ID "
					+ "inner join aggregate.birsregioni reg on prov.ID_REG=reg.ID_REG "
					+ "inner join aggregate.birsgames game on game.AAMS_GAME_CODE=x.AAMS_GAME_ID";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setString(1, sysGame);
			ps.setDate(2, data1_);
			ps.setTimestamp(3, data2_);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactGame>();

			while (rs.next()) {
				MeterfactGame m = new MeterfactGame();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);
				m.setPreu(rs.getDouble("PREU") / 100);
				m.setAams(rs.getDouble("AAMS") / 100);
				m.setNetWin(rs.getDouble("NET_WIN") / 100);
				m.setHouseWin(rs.getDouble("HOUSE_WIN") / 100);
				m.setSupplierProfit(rs.getDouble("SUPPLIER_PROFIT") / 100);
				m.setOperatorsProfit(rs.getDouble("OPERATORS_PROFIT") / 100);
				m.setBplusNetProfit(rs.getDouble("BPLUS_NET_PROFIT") / 100);

				// TEMPO DIM
				tDim.setAllData(rs.getDate("DATA"));

				// CLIENTE DIM
				Gestore g = new Gestore();
				g.setCod_gestore(rs.getString("COD_GESTORE"));

				cDim.setGestore(g);

				// SPAZIO DIM
				Nazione n = new Nazione();
				n.setNome("ITALIA");

				Regione r = new Regione();
				r.setNome(rs.getString("REGIONE"));
				r.setNazioni(n);

				Provincia p = new Provincia();
				p.setSigla(rs.getString("PROVINCIA"));
				p.setRegioni(r);

				Comune c = new Comune();
				c.setNome(rs.getString("COMUNE"));
				c.setProvincia(p);

				sDim.setNazione(n);
				sDim.setComune(c);
				sDim.setProvincia(p);
				sDim.setRegione(r);

				// SYSTEMA GIOCO DIM
				Location l = new Location();
				l.setCOMMERCIAL_NAME(rs.getString("LOCATION_NAME"));
				l.setTOPONIMO(rs.getString("TOPONIMO"));
				l.setADDRESS(rs.getString("LOCATION_ADDRESS"));
				l.setSTREET_NUMBER(rs.getString("NUMERO_CIVICO"));
				l.setAAMS_LOCATION_ID(rs.getString("AAMS_LOCATION_ID"));

				Game game = new Game();
				game.setGS_GAME_ID(rs.getLong("GS_GAME_CODE"));
				game.setAAMS_GAME_ID(rs.getLong("AAMS_GAME_ID"));
				game.setNAME(rs.getString("NAME"));

				sysDim.setAAMS_GAMESYSTEM_ID(rs.getLong("AAMS_GAMESYSTEM_ID"));
				sysDim.setGame(game);
				sysDim.setLoc(l);

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_games: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_games ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_games failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public List<MeterfactRegion> retrieveMeter_region(Date data1, Date data2,
			int id_region) throws DataAccessException {

		List<MeterfactRegion> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*, reg.NOME as REGIONE from ( "
					+ "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
					+ "sum(this_.JACKPOT_WIN) as JACKPOT_WINS,  "
					+ "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
					+ "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
					+ "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, "
					+ "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, "
					+ "sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, "
					+ "sum(total_in) TOTAL_IN,sum(total_out) TOTAL_OUT, "
					+ "sum(ticket_in) TICKET_IN,sum(ticket_out) TICKET_OUT,sum(coin_in) COIN_IN, "
					+ "sum(bill_in) BILL_IN,sum(card_in) CARD_IN,sum(prepaid_in) TOTAL_PREPAID_IN, "
					+ "this_.DATA as DATA, "
					+ "spaziodim1_.ID_REGIONE as ID_REGIONE "
					+ "from meter_location this_ "
					+ "inner join SPAZIODIM spaziodim1_ on this_.DMSPAZIO_ID=spaziodim1_.ID "
					+ "WHERE spaziodim1_.ID_REGIONE = ? and "
					+ "this_.DATA between ? and  ? "
					+ "GROUP BY this_.DATA,spaziodim1_.ID_REGIONE ) x "
					+ "inner join aggregate.birsregioni reg on x.ID_REGIONE=reg.ID_REG ";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setInt(1, id_region);
			ps.setDate(2, data1_);
			ps.setTimestamp(3, data2_);

			log.info(id_region + "**" + data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactRegion>();

			while (rs.next()) {
				MeterfactRegion m = new MeterfactRegion();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setTotalIn(rs.getDouble("TOTAL_IN") / 100);
				m.setTotalOut(rs.getDouble("TOTAL_OUT") / 100);
				m.setTicketIn(rs.getDouble("TICKET_IN") / 100);
				m.setTicketOut(rs.getDouble("TICKET_OUT") / 100);
				m.setCoinIn(rs.getDouble("COIN_IN") / 100);
				m.setBillIn(rs.getDouble("BILL_IN") / 100);
				m.setCardIn(rs.getDouble("CARD_IN") / 100);
				m.setPrepaidIn(rs.getDouble("TOTAL_PREPAID_IN") / 100);
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);
				m.setPreu(rs.getDouble("PREU") / 100);
				m.setAams(rs.getDouble("AAMS") / 100);
				m.setNetWin(rs.getDouble("NET_WIN") / 100);
				m.setHouseWin(rs.getDouble("HOUSE_WIN") / 100);
				m.setSupplierProfit(rs.getDouble("SUPPLIER_PROFIT") / 100);
				m.setOperatorsProfit(rs.getDouble("OPERATORS_PROFIT") / 100);
				m.setBplusNetProfit(rs.getDouble("BPLUS_NET_PROFIT") / 100);

				// TEMPO DIM

				tDim.setAllData(rs.getDate("DATA"));

				// CLIENTE DIM

				// SPAZIO DIM

				Nazione n = new Nazione();
				n.setNome("ITALIA");

				Regione r = new Regione();
				r.setIdReg(rs.getInt("ID_REGIONE"));
				r.setNome(rs.getString("REGIONE"));
				r.setNazioni(n);

				Provincia p = new Provincia();
				p.setRegioni(r);

				Comune c = new Comune();
				c.setProvincia(p);

				sDim.setNazione(n);
				sDim.setComune(c);
				sDim.setProvincia(p);
				sDim.setRegione(r);

				// SYSTEMA GIOCO DIM

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_region: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_region ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_region failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public List<MeterfactRegion> retrieveMeter_filtered_regions(Date data1,
			Date data2, String sysGame, List<Location> avaliableLocations)
			throws DataAccessException {

		List<MeterfactRegion> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*, reg.NOME as REGIONE from ( "
					+ "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
					+ "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, "
					+ "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
					+ "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
					+ "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, "
					+ "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, "
					+ "sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, "
					+ "sum(total_in) TOTAL_IN,sum(total_out) TOTAL_OUT,sum(ticket_in) TICKET_IN,sum(ticket_out) TICKET_OUT,sum(coin_in) COIN_IN, "
					+ "sum(bill_in) BILL_IN,sum(card_in) CARD_IN,sum(prepaid_in) TOTAL_PREPAID_IN, "
					+ "this_.DATA as DATA, spaziodim1_.ID_REGIONE as ID_REGIONE, "
					+ "this_.AAMS_GAMESYSTEM_CODE from meter_location this_ "
					+ "inner join `importlog` import on import.UNIQUE_SESSION_ID=this_.UNIQUE_SESSION_ID "
					+ "and import.UNIQUE_SESSION_ID in ( Select max(ii.UNIQUE_SESSION_ID) from importlog ii where START_DATE between import.START_DATE and import.END_DATE group by AAMS_GAME_SYSTEM_CODE) "
					+ "inner join SPAZIODIM spaziodim1_ on this_.DMSPAZIO_ID=spaziodim1_.ID "
					+ "WHERE this_.AAMS_GAMESYSTEM_CODE like ? and "
					+ "this_.DATA between ? and  ? "
					+ "and  this_.AAMS_LOCATION_CODE IN "
					+ Location.toParamenterLocationID(avaliableLocations)
					+ " "
					+ "GROUP BY this_.DATA,spaziodim1_.ID_REGIONE ) x "
					+ "inner join aggregate.birsregioni reg on x.ID_REGIONE=reg.ID_REG ";

			/*
			 * "select x.*,y.*, reg.NOME as REGIONE from ( " +
			 * "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
			 * + "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, " +
			 * "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
			 * +
			 * "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
			 * + "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, " +
			 * "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, " +
			 * "sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, " +
			 * "date(tempodim5_.DATA) as DATA, " +
			 * "spaziodim1_.ID_REGIONE as ID_REGIONE, " +
			 * "sysgiocodim4_.AAMS_GAMESYSTEM_CODE " + "from METERFACT this_ " +
			 * "inner join `importlog` import on import.UNIQUE_SESSION_ID=this_.UNIQUE_SESSION_ID "
			 * +
			 * "and import.UNIQUE_SESSION_ID in ( Select max(ii.UNIQUE_SESSION_ID) from importlog ii where START_DATE between import.START_DATE and import.END_DATE group by AAMS_GAME_SYSTEM_CODE) "
			 * +
			 * "inner join TEMPODIM tempodim5_ on this_.DMTEMPO_ID=tempodim5_.ID "
			 * +
			 * "inner join SPAZIODIM spaziodim1_ on this_.DMSPAZIO_ID=spaziodim1_.ID "
			 * +
			 * "inner join SISTEMAGIOCODIM sysgiocodim4_ on this_.DMSISTEMAGIOCO_ID=sysgiocodim4_.ID "
			 * + "WHERE sysgiocodim4_.AAMS_GAMESYSTEM_CODE like ? and  " +
			 * "tempodim5_.DATA between ? and  ? " +
			 * "and import.START_DATE between ? and  ? " +
			 * "and  sysgiocodim4_.AAMS_LOCATION_CODE IN "
			 * +Location.toParamenterLocationID(avaliableLocations)+" "+
			 * "GROUP BY date(tempodim5_.DATA),spaziodim1_.ID_REGIONE ) x " +
			 * "inner join aggregate.birsregioni reg on x.ID_REGIONE=reg.ID_REG "
			 * + "inner join ( "+
			 * "select  ME.data, sum(total_in) TOTAL_IN,sum(total_out) TOTAL_OUT,sum(ticket_in) TICKET_IN,sum(ticket_out) TICKET_OUT,sum(coin_in) COIN_IN, "
			 * +
			 * "sum(bill_in) BILL_IN,sum(card_in) CARD_IN,sum(total_prepaid_in) TOTAL_PREPAID_IN, "
			 * + "me.id_regione " +
			 * "from ( select me.data, avg(total_in) total_in,avg(total_out) total_out,avg(ticket_in) ticket_in, "
			 * +
			 * "avg(ticket_out) ticket_out,avg(coin_in) coin_in,avg(bill_in) bill_in,avg(card_in) card_in, "
			 * + "avg(prepaid_in) total_prepaid_in, " +
			 * "me.unique_session_id, s.ID_REGIONE " +
			 * "from METERFACT me, spaziodim s " +
			 * "where me.data between ? and ? " + "and me.DMSPAZIO_ID = s.ID " +
			 * "group by me.data, me.unique_session_id, s.ID_REGIONE " + ") me "
			 * + "group by me.data, me.id_regione ) " +
			 * "y on x.data = y.data and x.id_regione = y.id_regione";
			 */

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setString(1, sysGame);
			ps.setDate(2, data1_);
			ps.setTimestamp(3, data2_);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactRegion>();

			while (rs.next()) {
				MeterfactRegion m = new MeterfactRegion();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setTotalIn(rs.getDouble("TOTAL_IN") / 100);
				m.setTotalOut(rs.getDouble("TOTAL_OUT") / 100);
				m.setTicketIn(rs.getDouble("TICKET_IN") / 100);
				m.setTicketOut(rs.getDouble("TICKET_OUT") / 100);
				m.setCoinIn(rs.getDouble("COIN_IN") / 100);
				m.setBillIn(rs.getDouble("BILL_IN") / 100);
				m.setCardIn(rs.getDouble("CARD_IN") / 100);
				m.setPrepaidIn(rs.getDouble("TOTAL_PREPAID_IN") / 100);
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);
				m.setPreu(rs.getDouble("PREU") / 100);
				m.setAams(rs.getDouble("AAMS") / 100);
				m.setNetWin(rs.getDouble("NET_WIN") / 100);
				m.setHouseWin(rs.getDouble("HOUSE_WIN") / 100);
				m.setSupplierProfit(rs.getDouble("SUPPLIER_PROFIT") / 100);
				m.setOperatorsProfit(rs.getDouble("OPERATORS_PROFIT") / 100);
				m.setBplusNetProfit(rs.getDouble("BPLUS_NET_PROFIT") / 100);

				// TEMPO DIM

				tDim.setAllData(rs.getDate("DATA"));

				// CLIENTE DIM

				// SPAZIO DIM

				Nazione n = new Nazione();
				n.setNome("ITALIA");

				Regione r = new Regione();
				r.setIdReg(rs.getInt("ID_REGIONE"));
				r.setNome(rs.getString("REGIONE"));
				r.setNazioni(n);

				Provincia p = new Provincia();
				p.setRegioni(r);

				Comune c = new Comune();
				c.setProvincia(p);

				sDim.setNazione(n);
				sDim.setComune(c);
				sDim.setProvincia(p);
				sDim.setRegione(r);

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_regions: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_regions ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_regions failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public List<MeterfactRegion> retrieveMeter_regions(Date data1, Date data2,
			String sysGame) throws DataAccessException {

		List<MeterfactRegion> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*, reg.NOME as REGIONE from ( "
					+ "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
					+ "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, "
					+ "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
					+ "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
					+ "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, "
					+ "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, "
					+ "sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, "
					+ "sum(total_in) TOTAL_IN,sum(total_out) TOTAL_OUT,sum(ticket_in) TICKET_IN,sum(ticket_out) TICKET_OUT,sum(coin_in) COIN_IN, "
					+ "sum(bill_in) BILL_IN,sum(card_in) CARD_IN,sum(prepaid_in) TOTAL_PREPAID_IN, "
					+ "this_.DATA as DATA, spaziodim1_.ID_REGIONE as ID_REGIONE, "
					+ "this_.AAMS_GAMESYSTEM_CODE from meter_location this_ "
					+ "inner join SPAZIODIM spaziodim1_ on this_.DMSPAZIO_ID=spaziodim1_.ID "
					+ "WHERE this_.AAMS_GAMESYSTEM_CODE like ? and "
					+ "this_.DATA between ? and  ? "
					+ "GROUP BY this_.DATA,spaziodim1_.ID_REGIONE ) x "
					+ "inner join aggregate.birsregioni reg on x.ID_REGIONE=reg.ID_REG ";

			/*
			 * "select x.*,y.*, reg.NOME as REGIONE from ( " +
			 * "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
			 * + "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, " +
			 * "sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
			 * +
			 * "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
			 * + "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, " +
			 * "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, " +
			 * "sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, " +
			 * "date(tempodim5_.DATA) as DATA, " +
			 * "spaziodim1_.ID_REGIONE as ID_REGIONE, " +
			 * "sysgiocodim4_.AAMS_GAMESYSTEM_CODE " + "from METERFACT this_ " +
			 * "inner join `importlog` import on import.UNIQUE_SESSION_ID=this_.UNIQUE_SESSION_ID "
			 * +
			 * "and import.UNIQUE_SESSION_ID in ( Select max(ii.UNIQUE_SESSION_ID) from importlog ii where START_DATE between import.START_DATE and import.END_DATE group by AAMS_GAME_SYSTEM_CODE) "
			 * +
			 * "inner join TEMPODIM tempodim5_ on this_.DMTEMPO_ID=tempodim5_.ID "
			 * +
			 * "inner join SPAZIODIM spaziodim1_ on this_.DMSPAZIO_ID=spaziodim1_.ID "
			 * +
			 * "inner join SISTEMAGIOCODIM sysgiocodim4_ on this_.DMSISTEMAGIOCO_ID=sysgiocodim4_.ID "
			 * + "WHERE sysgiocodim4_.AAMS_GAMESYSTEM_CODE like ? and  " +
			 * "tempodim5_.DATA between ? and  ? " +
			 * "and import.START_DATE between ? and  ? " +
			 * "GROUP BY date(tempodim5_.DATA),spaziodim1_.ID_REGIONE ) x " +
			 * "inner join aggregate.birsregioni reg on x.ID_REGIONE=reg.ID_REG "
			 * + "inner join ( "+
			 * "select  ME.data, sum(total_in) TOTAL_IN,sum(total_out) TOTAL_OUT,sum(ticket_in) TICKET_IN,sum(ticket_out) TICKET_OUT,sum(coin_in) COIN_IN, "
			 * +
			 * "sum(bill_in) BILL_IN,sum(card_in) CARD_IN,sum(total_prepaid_in) TOTAL_PREPAID_IN, "
			 * + "me.id_regione " +
			 * "from ( select me.data, avg(total_in) total_in,avg(total_out) total_out,avg(ticket_in) ticket_in, "
			 * +
			 * "avg(ticket_out) ticket_out,avg(coin_in) coin_in,avg(bill_in) bill_in,avg(card_in) card_in, "
			 * + "avg(prepaid_in) total_prepaid_in, " +
			 * "me.unique_session_id, s.ID_REGIONE " +
			 * "from METERFACT me, spaziodim s " +
			 * "where me.data between ? and ? " + "and me.DMSPAZIO_ID = s.ID " +
			 * "group by me.data, me.unique_session_id, s.ID_REGIONE " + ") me "
			 * + "group by me.data, me.id_regione ) " +
			 * "y on x.data = y.data and x.id_regione = y.id_regione";
			 */

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setString(1, sysGame);
			ps.setDate(2, data1_);
			ps.setTimestamp(3, data2_);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactRegion>();

			while (rs.next()) {
				MeterfactRegion m = new MeterfactRegion();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setTotalIn(rs.getDouble("TOTAL_IN") / 100);
				m.setTotalOut(rs.getDouble("TOTAL_OUT") / 100);
				m.setTicketIn(rs.getDouble("TICKET_IN") / 100);
				m.setTicketOut(rs.getDouble("TICKET_OUT") / 100);
				m.setCoinIn(rs.getDouble("COIN_IN") / 100);
				m.setBillIn(rs.getDouble("BILL_IN") / 100);
				m.setCardIn(rs.getDouble("CARD_IN") / 100);
				m.setPrepaidIn(rs.getDouble("TOTAL_PREPAID_IN") / 100);
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);
				m.setPreu(rs.getDouble("PREU") / 100);
				m.setAams(rs.getDouble("AAMS") / 100);
				m.setNetWin(rs.getDouble("NET_WIN") / 100);
				m.setHouseWin(rs.getDouble("HOUSE_WIN") / 100);
				m.setSupplierProfit(rs.getDouble("SUPPLIER_PROFIT") / 100);
				m.setOperatorsProfit(rs.getDouble("OPERATORS_PROFIT") / 100);
				m.setBplusNetProfit(rs.getDouble("BPLUS_NET_PROFIT") / 100);

				// TEMPO DIM

				tDim.setAllData(rs.getDate("DATA"));

				// CLIENTE DIM

				// SPAZIO DIM

				Nazione n = new Nazione();
				n.setNome("ITALIA");

				Regione r = new Regione();
				r.setIdReg(rs.getInt("ID_REGIONE"));
				r.setNome(rs.getString("REGIONE"));
				r.setNazioni(n);

				Provincia p = new Provincia();
				p.setRegioni(r);

				Comune c = new Comune();
				c.setProvincia(p);

				sDim.setNazione(n);
				sDim.setComune(c);
				sDim.setProvincia(p);
				sDim.setRegione(r);

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_regions: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_regions ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_regions failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

	public int retrieveMeter_SimpleTrendLocation(Date data1, Date data2,
			String aams_location_id) throws DataAccessException {

		int exit_simbol = 0;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		int i = 0;
		Double first_bet = 0d;
		Double second_bet = 0d;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select sum(this_.BET) as BET "
					+ "from METER_vlt this_ "
					+ "WHERE this_.AAMS_LOCATION_CODE like ? "
					+ "and (this_.DATA = ? or this_.DATA =  ?) "
					+ "GROUP BY this_.DATA " + "ORDER BY this_.DATA;";

			/*
			 * "select sum(this_.BET) as BET " + "from METERFACT this_ " +
			 * "inner join TEMPODIM tempodim5_ on this_.DMTEMPO_ID=tempodim5_.ID "
			 * +
			 * "inner join SISTEMAGIOCODIM sysgiocodim4_ on this_.DMSISTEMAGIOCO_ID=sysgiocodim4_.ID "
			 * + "WHERE sysgiocodim4_.AAMS_LOCATION_CODE like ? " +
			 * "and (date(tempodim5_.DATA) = ? or date(tempodim5_.DATA) =  ? ) "
			 * + "GROUP BY date(tempodim5_.DATA)" +
			 * "ORDER BY date(tempodim5_.DATA)";
			 */

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Date data2_ = new java.sql.Date(data2.getTime());

			ps.setString(1, aams_location_id);
			ps.setDate(2, data1_);
			ps.setDate(3, data2_);

			log.info(aams_location_id);
			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			while (rs.next()) {

				if (i == 0) {
					first_bet = rs.getDouble("BET");
				} else {
					second_bet = rs.getDouble("BET");
				}

				i++;
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_SimpleTrendlocation: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_SimpleTrendlocation failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (!DateUtils.isDateEquals(data1, data2)) {

			if (first_bet < second_bet) {
				exit_simbol = 1;
			} else if (first_bet > second_bet) {
				exit_simbol = -1;
			} else {
				exit_simbol = 0;
			}

		} else {
			exit_simbol = 0;
		}

		return exit_simbol;
	}

	public int retrieveMeter_SimpleTrendVlt(Date data1, Date data2,
			String aams_vlt_id) throws DataAccessException {

		int exit_simbol = 0;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		int i = 0;
		Double first_bet = 0d;
		Double second_bet = 0d;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select sum(this_.BET) as BET "
					+ "from METER_vlt this_ "
					+ "WHERE this_.AAMS_VLT_CODE like ? "
					+ "and (this_.DATA = ? or this_.DATA =  ?)"
					+ "GROUP BY this_.DATA " + "ORDER BY this_.DATA";

			/*
			 * "select sum(this_.BET) as BET " + "from METERFACT this_ " +
			 * "inner join TEMPODIM tempodim5_ on this_.DMTEMPO_ID=tempodim5_.ID "
			 * +
			 * "inner join SISTEMAGIOCODIM sysgiocodim4_ on this_.DMSISTEMAGIOCO_ID=sysgiocodim4_.ID "
			 * + "WHERE sysgiocodim4_.AAMS_VLT_CODE like ? " +
			 * "and (date(tempodim5_.DATA) = ? or date(tempodim5_.DATA) =  ? ) "
			 * + "GROUP BY date(tempodim5_.DATA)" +
			 * "ORDER BY date(tempodim5_.DATA)";
			 */

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Date data2_ = new java.sql.Date(data2.getTime());

			ps.setString(1, aams_vlt_id);
			ps.setDate(2, data1_);
			ps.setDate(3, data2_);

			log.info(aams_vlt_id);
			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			while (rs.next()) {

				if (i == 0) {
					first_bet = rs.getDouble("BET");
				} else {
					second_bet = rs.getDouble("BET");
				}

				i++;
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_SimpleTrendlocation: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_SimpleTrendlocation failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (!DateUtils.isDateEquals(data1, data2)) {

			if (first_bet < second_bet) {
				exit_simbol = 1;
			} else if (first_bet > second_bet) {
				exit_simbol = -1;
			} else {
				exit_simbol = 0;
			}

		} else {
			exit_simbol = 0;
		}

		return exit_simbol;
	}

	public List<MeterfactVlt> retrieveMeter_vlts_dismissed(Date data1,
			Date data2, String sysGame) throws DataAccessException {

		List<MeterfactVlt> meters = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			// DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);

			String sql = "select x.*, loc.COMMERCIAL_NAME as LOCATION_NAME, loc.ADDRESS as LOCATION_ADDRESS,loc.STREET_NUMBER as NUMERO_CIVICO, "
					+ "loc.AAMS_LOCATION_CODE as AAMS_LOCATION_ID,top.DESCRIPTION as TOPONIMO, "
					+ "reg.NOME as REGIONE, prov.SIGLA as PROVINCIA, com.NOME as COMUNE, vlt.AAMS_VLT_MODEL_CODE  from ( "
					+ "select sum(this_.BET) as BET, sum(this_.WIN) as WIN, sum(this_.GAMES_PLAYED) as GAMES_PLAYED, "
					+ "sum(this_.JACKPOT_WIN) as JACKPOT_WINS, sum(this_.JACKPOT_CONTRIBUTION) as JACKPOT_CONTRIBUTION, sum(this_.PREU) as PREU, "
					+ "sum(this_.AAMS) as AAMS, sum(this_.NET_WIN) as NET_WIN,sum(this_.HOUSE_WIN) as HOUSE_WIN, "
					+ "sum(IFNULL(this_.SUPPLIER_PROFIT,0)) as SUPPLIER_PROFIT, "
					+ "sum(IFNULL(this_.OPERATORS_PROFIT,0)) as OPERATORS_PROFIT, sum(IFNULL(this_.BPLUS_NET_PROFIT,0))  as BPLUS_NET_PROFIT, "
					+ "count(distinct(this_.AAMS_VLT_CODE)) as NUM_VLT, "
					+ "sum(total_in) TOTAL_IN,sum(total_out) TOTAL_OUT,sum(ticket_in) TICKET_IN,sum(ticket_out) TICKET_OUT,sum(coin_in) COIN_IN, "
					+ "sum(bill_in) BILL_IN,sum(card_in) CARD_IN,sum(prepaid_in) TOTAL_PREPAID_IN,sum(HANDPAY) HANDPAY, "
					+ "this_.DATA, this_.AAMS_LOCATION_CODE, clientidim6_.COD_GESTORE, "
					+ "this_.AAMS_GAMESYSTEM_CODE AAMS_GAMESYSTEM_ID,this_.AAMS_VLT_CODE AAMS_VLT_ID "
					+ "from meter_vlt this_ "
					+ "inner join CLIENTEDIM clientidim6_ on this_.DMCLIENTE_ID=clientidim6_.ID "
					+ "WHERE this_.AAMS_GAMESYSTEM_CODe like ? "
					+ "and this_.DATA between ? and  ? "
					+ "GROUP BY this_.DATA,this_.AAMS_VLT_CODE,this_.AAMS_LOCATION_CODE,this_.AAMS_GAMESYSTEM_CODE ) x "
					+ "inner join (select aams_vlt_code,date_out from aggregate.birsvlt s where s.DATE_OUT between ? "
					+ "and date(?)) dism on dism.AAMS_VLT_CODE = x.AAMS_VLT_ID and dism.date_out = x.DATA "
					+ "inner join aggregate.birsvlt vlt on x.AAMS_VLT_ID = vlt.AAMS_VLT_CODE "
					+ "inner join aggregate.birslocation loc on x.AAMS_LOCATION_CODE = loc.AAMS_LOCATION_CODE "
					+ "inner join aggregate.birscomuni com on loc.CADASTRAL_CODE = com.CADASTRAL_CODE  "
					+ "inner join aggregate.birsprovince prov on com.ID_PROV=prov.ID_PROV "
					+ "inner join aggregate.birstoponimo top on loc.ID_TOPONIMO = top.ID "
					+ "inner join aggregate.birsregioni reg on prov.ID_REG=reg.ID_REG";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			java.sql.Date data1_ = new java.sql.Date(data1.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());

			ps.setString(1, sysGame);
			ps.setDate(2, data1_);
			ps.setTimestamp(3, data2_);
			ps.setDate(4, data1_);
			ps.setTimestamp(5, data2_);

			log.info(data1_ + "****" + data2_);

			rs = ps.executeQuery();

			meters = new ArrayList<MeterfactVlt>();

			while (rs.next()) {
				MeterfactVlt m = new MeterfactVlt();
				Spaziodim sDim = new Spaziodim();
				Tempodim tDim = new Tempodim();
				Sistemagiocodim sysDim = new Sistemagiocodim();
				Clientedim cDim = new Clientedim();

				m.setBet(rs.getDouble("BET") / 100);
				m.setWin(rs.getDouble("WIN") / 100);
				m.setGamesPlayed(rs.getLong("GAMES_PLAYED"));
				m.setTotalIn(rs.getDouble("TOTAL_IN") / 100);
				m.setTotalOut(rs.getDouble("TOTAL_OUT") / 100);
				m.setTicketIn(rs.getDouble("TICKET_IN") / 100);
				m.setTicketOut(rs.getDouble("TICKET_OUT") / 100);
				m.setCoinIn(rs.getDouble("COIN_IN") / 100);
				m.setBillIn(rs.getDouble("BILL_IN") / 100);
				m.setCardIn(rs.getDouble("CARD_IN") / 100);
				m.setPrepaidIn(rs.getDouble("TOTAL_PREPAID_IN") / 100);
				m.setHandpay(rs.getDouble("HANDPAY") / 100);
				m.setJackpotWins(rs.getDouble("JACKPOT_WINS") / 100);
				m.setJackpotContribution(rs.getDouble("JACKPOT_CONTRIBUTION") / 100);
				m.setPreu(rs.getDouble("PREU") / 100);
				m.setAams(rs.getDouble("AAMS") / 100);
				m.setNetWin(rs.getDouble("NET_WIN") / 100);
				m.setHouseWin(rs.getDouble("HOUSE_WIN") / 100);
				m.setSupplierProfit(rs.getDouble("SUPPLIER_PROFIT") / 100);
				m.setOperatorsProfit(rs.getDouble("OPERATORS_PROFIT") / 100);
				m.setBplusNetProfit(rs.getDouble("BPLUS_NET_PROFIT") / 100);

				// TEMPO DIM

				tDim.setAllData(rs.getDate("DATA"));

				// CLIENTE DIM

				Gestore g = new Gestore();
				g.setCod_gestore(rs.getString("COD_GESTORE"));

				cDim.setGestore(g);

				// SPAZIO DIM

				Nazione n = new Nazione();
				n.setNome("ITALIA");

				Regione r = new Regione();
				r.setNome(rs.getString("REGIONE"));
				r.setNazioni(n);

				Provincia p = new Provincia();
				p.setSigla(rs.getString("PROVINCIA"));
				p.setRegioni(r);

				Comune c = new Comune();
				c.setNome(rs.getString("COMUNE"));
				c.setProvincia(p);

				sDim.setNazione(n);
				sDim.setComune(c);
				sDim.setProvincia(p);
				sDim.setRegione(r);

				// SYSTEMA GIOCO DIM

				Location l = new Location();
				l.setCOMMERCIAL_NAME(rs.getString("LOCATION_NAME"));
				l.setTOPONIMO(rs.getString("TOPONIMO"));
				l.setADDRESS(rs.getString("LOCATION_ADDRESS"));
				l.setSTREET_NUMBER(rs.getString("NUMERO_CIVICO"));
				l.setAAMS_LOCATION_ID(rs.getString("AAMS_LOCATION_ID"));

				Vlt v = new Vlt();
				v.setAAMS_VLT_ID(rs.getString("AAMS_VLT_ID"));
				v.setVLT_MODEL_ID(rs.getInt("AAMS_VLT_MODEL_CODE"));

				sysDim.setAAMS_GAMESYSTEM_ID(rs.getLong("AAMS_GAMESYSTEM_ID"));
				sysDim.setVlt(v);
				sysDim.setLoc(l);

				m.setSistemagiocodim(sysDim);
				m.setSpaziodim(sDim);
				m.setClientidim(cDim);
				m.setTempodim(tDim);

				meters.add(m);
			}

			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieveMeter_vlts: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.error(
					"EXCEPTION: retrieveMeter_vlts ArrayIndexOutOfBoundsException",
					e);
			throw new DataAccessException(e.toString(), e);
		} catch (Exception e) {
			log.error("retrieveMeter_vlts failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return meters;
	}

}