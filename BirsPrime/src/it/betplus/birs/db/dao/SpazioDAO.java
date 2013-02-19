//package it.betplus.birs.db.dao;
//
//import it.betplus.birs.connector.DBConnectionManager;
//import it.betplus.birs.exception.DataAccessException;
//import it.betplus.birs.primitive.Spaziodim;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//public class SpazioDAO {
//
//	protected static Logger logger = Logger.getLogger(SpazioDAO.class);
//
//
//
//	private static SpazioDAO instance;
//
//	private SpazioDAO() {
//		super();
//	}
//
//
//
//	public static synchronized SpazioDAO getInstance() {
//		if (instance == null) {
//			synchronized (SpazioDAO.class) {
//				instance = new SpazioDAO();
//			}
//		}
//		return instance;
//	}
//
//
//	
//	
//	
//	
//	public List<Spaziodim> retrieveVltByIdLocation(String id_location) throws DataAccessException{
//		List<Spaziodim> vlts = null;
//		ResultSet rs=null;
//		Statement st=null;
//		Connection connDataMart=null;
//					
//		try {
//			connDataMart=DBConnectionManager.dataMartConnectionFactory();
//			
//			String sql = "select distinct(aams_VLT_id) from spaziodim where aams_location_id='"+id_location+"' order by aams_VLT_id asc";
//			st = connDataMart.createStatement();
//			rs = st.executeQuery(sql);
//			
//			vlts = new ArrayList<Spaziodim>();
//			while (rs.next()){
//				Spaziodim sdim = new Spaziodim();
//				sdim.setAamsVltId(rs.getString("AAMS_VLT_ID"));
//				vlts.add(sdim);
//			}
//			
//		} catch (SQLException e) {
//			logger.error("retrieveVltByIdLocation failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY, e.toString(), e);
//		}
//		catch (Exception e) {
//			logger.error("retrieveVltByIdLocation failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY, e.toString(), e);
//		}
//		finally{
//				try {
//					DBConnectionManager.CloseConnection(rs, st, connDataMart);				
//				} catch (DataAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					logger.error(e);
//				}
//		}
//		return vlts;	
//	}
//
//	
//	
//	
//	public List<Spaziodim> retrieveAllGames() throws DataAccessException{
//		List<Spaziodim> games = null;
//		ResultSet rs=null;
//		Statement st=null;
//		Connection connDataMart=null;
//					
//		try {
//			connDataMart=DBConnectionManager.dataMartConnectionFactory();
//			
//			String sql = "select distinct(gs_game_id), game_name from spaziodim where gs_game_id not in (-1) order by game_name asc";
//			st = connDataMart.createStatement();
//			rs = st.executeQuery(sql);
//			
//			games = new ArrayList<Spaziodim>();
//			while (rs.next()){
//				Spaziodim sdim = new Spaziodim();
//				sdim.setGsGameId(rs.getLong("GS_GAME_ID"));
//				sdim.setGameName(rs.getString("GAME_NAME"));
//				games.add(sdim);
//			}
//			
//		} catch (SQLException e) {
//			logger.error("retrieveAllGames failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY, e.toString(), e);
//		}
//		catch (Exception e) {
//			logger.error("retrieveAllGames failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY, e.toString(), e);
//		}
//		finally{
//				try {
//					DBConnectionManager.CloseConnection(rs, st, connDataMart);				
//				} catch (DataAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					logger.error(e);
//				}
//		}
//		return games;	
//	}
//	
//	
//	
//	public int fixGame() throws DataAccessException{
//		//logger.info("FixGame");
//		List<Long> games = null;
//		HashMap<Long, String> mapGames = null;
//		ResultSet rs=null;
//		PreparedStatement ps=null;
//		Connection connDataMart=null;
//		Connection connStaging=null;
//		int risultato=0;
//		
//		try {
//			connDataMart=DBConnectionManager.dataMartConnectionFactory();		
//			String sql_1 = "select distinct gs_game_id from spaziodim where gs_game_id<>-1 and game_name is null";
//			ps = connDataMart.prepareStatement(sql_1);
//			rs = ps.executeQuery();
//			
//			games = new ArrayList<Long>();
//			while (rs.next()){
//				games.add(rs.getLong("gs_game_id"));
//			}
//			DBConnectionManager.CloseStatement(rs, ps);
//			
//			
//			connStaging=DBConnectionManager.StagingConnectionFactory();
//			String sql_2 = "SELECT GS_GAMES_ID, NAME FROM BIRSGAMES";
//			
//			ps = connStaging.prepareStatement(sql_2);
//			rs = ps.executeQuery();
//			mapGames = new HashMap<Long, String>();
//			
//			while (rs.next()){
//				mapGames.put(rs.getLong("GS_GAMES_ID"), rs.getString("NAME"));
//			}
//			DBConnectionManager.CloseStatement(rs, ps);
//			
//			
//			for (Long id : games){
//				String sql_3 = "update spaziodim set game_name= ? where gs_game_id = ? and game_name is null";
//			
//				ps = connDataMart.prepareStatement(sql_3);
//				ps.setString(1, mapGames.get(id));
//				ps.setLong(2, id);
//			
//				risultato = risultato + ps.executeUpdate();
//			}
//					
//			connDataMart.commit();
//			
//		} catch (SQLException e) {
//			logger.error("fixGame failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY, e.toString(), e);
//		}
//		catch (Exception e) {
//			logger.error("fixGame failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY, e.toString(), e);
//		}
//		finally{
//				try {
//					DBConnectionManager.CloseConnection(connStaging);
//					DBConnectionManager.CloseConnection(rs, ps, connDataMart);				
//				} catch (DataAccessException e) {
//					e.printStackTrace();
//					logger.error(e);
//				}
//		}
//		return risultato;	
//	}
//	
//	
//	public int fixVlt() throws DataAccessException{
//		//logger.info("FixVlt");
//		List<String> vlts = null;
//		ResultSet rs=null;
//		PreparedStatement ps=null;
//		Connection connDataMart=null;
//		int risultato=0;
//		
//		try {
//			connDataMart=DBConnectionManager.dataMartConnectionFactory();
//			
//			String sql_1 = "select distinct aams_vlt_id from spaziodim where civ_vlt is null or gs_vlt_id is null";
//			ps = connDataMart.prepareStatement(sql_1);
//			rs = ps.executeQuery();
//			
//			vlts = new ArrayList<String>();
//			while (rs.next()){
//				vlts.add(rs.getString("AAMS_VLT_ID"));
//			}
//			
//			for (String s : vlts){
//				String sql_2 = "update spaziodim set civ_vlt = (select civ from birsvlt where aams_vlt_id = ? ), gs_vlt_id = (select gs_vlt_code from birsvlt where aams_vlt_id = ? ) " +
//				" where aams_vlt_id = ? ";
//			
//				ps = connDataMart.prepareStatement(sql_2);
//				ps.setString(1, s);
//				ps.setString(2, s);
//				ps.setString(3, s);
//				
//				risultato = risultato + ps.executeUpdate();
//			}
//					
//			connDataMart.commit();
//			
//		} catch (SQLException e) {
//			logger.error("fixVlt failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY, e.toString(), e);
//		}
//		catch (Exception e) {
//			logger.error("fixVlt failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY, e.toString(), e);
//		}
//		finally{
//				try {
//					DBConnectionManager.CloseConnection(rs, ps, connDataMart);				
//				} catch (DataAccessException e) {
//					e.printStackTrace();
//					logger.error(e);
//				}
//		}
//		return risultato;	
//	}
//	
//	
//	
//	public int fixLocation() throws DataAccessException{
//		//logger.info("FixLocation");
//		List<String> vlts = null;
//		ResultSet rs=null;
//		PreparedStatement ps=null;
//		Connection connDataMart=null;
//		int risultato=0;
//		
//		try {
//			connDataMart=DBConnectionManager.dataMartConnectionFactory();
//			
//			String sql_1 = "select distinct aams_location_id from spaziodim where location_name is null or cod_catasto_city is null or location_address is null" +
//					" or numero_civico is null or city is null or district is null or sigla_provincia is null or region is null or toponimo is null";
//			ps = connDataMart.prepareStatement(sql_1);
//			rs = ps.executeQuery();
//			
//			vlts = new ArrayList<String>();
//			while (rs.next()){
//				vlts.add(rs.getString("AAMS_LOCATION_ID"));
//			}
//			
//			for (String s : vlts){
//				String sql_2 = "update spaziodim set location_name = (select commercial_name from birslocation where aams_location_id = ? )," +
//						" cod_catasto_city = (select cadastral_code from birslocation where aams_location_id = ? ), " +
//						" location_address = (select address from birslocation where aams_location_id = ? ), " +
//						" numero_civico = (select street_number from birslocation where aams_location_id = ? ), " +
//						" cap = (select cap from birslocation where aams_location_id = ? ), " +
//						" toponimo = (select t.description from birslocation l inner join birstoponimo t on l.id_toponimo=t.id where l.aams_location_id = ? ), " +   //  SERVE SINONIMO DI BIRSTOPONIMO SU DATAMART
//						" city = (select c.nome from birslocation l inner join comuni c on l.cadastral_code=c.cadastrial_code where l.aams_location_id = ? ), " +
//						" id_comune = (select c.id_comune from birslocation l inner join comuni c on l.cadastral_code=c.cadastrial_code where l.aams_location_id = ? ), " +
//						" id_provincia = (select c.id_prov from birslocation l inner join comuni c on l.cadastral_code=c.cadastrial_code where l.aams_location_id = ? ), " +
//						" district = (select p.nome from birslocation l inner join comuni c on l.cadastral_code=c.cadastrial_code inner join province p on c.id_prov=p.id_prov where l.aams_location_id = ? ), " +
//						" sigla_provincia = (select p.sigla from birslocation l inner join comuni c on l.cadastral_code=c.cadastrial_code inner join province p on c.id_prov=p.id_prov where l.aams_location_id = ? ), " +
//						" id_regione = (select p.id_reg from birslocation l inner join comuni c on l.cadastral_code=c.cadastrial_code inner join province p on c.id_prov=p.id_prov where l.aams_location_id = ? ), " +
//						" region = (select r.nome from birslocation l inner join comuni c on l.cadastral_code=c.cadastrial_code inner join province p on c.id_prov=p.id_prov inner join regioni r on p.id_reg=r.id_reg where l.aams_location_id = ? ), " +
//						" id_area = (select r.id_area from birslocation l inner join comuni c on l.cadastral_code=c.cadastrial_code inner join province p on c.id_prov=p.id_prov inner join regioni r on p.id_reg=r.id_reg where l.aams_location_id = ? ), " +
//						" id_nazione = (select r.id_nazione from birslocation l inner join comuni c on l.cadastral_code=c.cadastrial_code inner join province p on c.id_prov=p.id_prov inner join regioni r on p.id_reg=r.id_reg where l.aams_location_id = ? ), " +
//						" zone = (select a.nome from birslocation l inner join comuni c on l.cadastral_code=c.cadastrial_code inner join province p on c.id_prov=p.id_prov inner join regioni r on p.id_reg=r.id_reg inner join aree a on a.id_area=r.id_area where l.aams_location_id = ? ), " +
//						" nation = 'ITALY' "+
//						" where aams_location_id = ? ";
//			
//				ps = connDataMart.prepareStatement(sql_2);
//				ps.setString(1, s);
//				ps.setString(2, s);
//				ps.setString(3, s);
//				ps.setString(4, s);
//				ps.setString(5, s);
//				ps.setString(6, s);
//				ps.setString(7, s);
//				ps.setString(8, s);
//				ps.setString(9, s);
//				ps.setString(10, s);
//				ps.setString(11, s);
//				ps.setString(12, s);
//				ps.setString(13, s);
//				ps.setString(14, s);
//				ps.setString(15, s);
//				ps.setString(16, s);
//				ps.setString(17, s); // QUANDO CI SARà BIRSTOPONIMO
//				
//				risultato = risultato + ps.executeUpdate();
//			}
//					
//			connDataMart.commit();
//			
//		} catch (SQLException e) {
//			logger.error("fixLocation failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY, e.toString(), e);
//		}
//		catch (Exception e) {
//			logger.error("fixLocation failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY, e.toString(), e);
//		}
//		finally{
//				try {
//					DBConnectionManager.CloseConnection(rs, ps, connDataMart);				
//				} catch (DataAccessException e) {
//					e.printStackTrace();
//					logger.error(e);
//				}
//		}
//		return risultato;	
//	}
//	
//
//}