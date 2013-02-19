//package it.betplus.birs.db.dao;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import it.bplus.db.DBConnectionManager;
//import it.bplus.exception.DataAccessException;
//import it.bplus.model.Comuni;
//import it.bplus.util.IErrorCodes;
//
//public class ComuniDAO  {
//
//	protected static Logger logger = Logger.getLogger(ComuniDAO.class);
//
//
//	private static ComuniDAO instance;
//
//	private ComuniDAO() {
//		super();
//	}
//
//
//	public static synchronized ComuniDAO getInstance() {
//		if (instance == null)
//		{
//			synchronized(ComuniDAO.class) {      //1
//				ComuniDAO inst = instance;         //2
//				if (inst == null)
//				{
//					synchronized(ComuniDAO.class) {  //3
//						instance = new ComuniDAO();
//					}
//					//instance = inst;               //5
//				}
//			}
//		}
//		return instance;
//	}
//
//	public List<Comune> getComuniByIdProvincia(String id_provincia) throws DataAccessException{
//		PreparedStatement ps = null;
//		Connection conn = null;
//		ResultSet rs = null;
//		List<Comune> comlist = null;
//
//		try{
//			conn = DBConnectionManager.DataMartConnectionFactory();
//
//			String sql = "select * from comuni where id_prov=? order by nome asc";
//
//			ps = conn.prepareStatement(sql);
//			ps.setString(1, id_provincia);
//			rs = ps.executeQuery();
//			
//			comlist = new ArrayList<Comune>();
//			while (rs.next()) {
//				Comune com = new Comune();
//				com.setIdComune(rs.getLong("ID_COMUNE"));
//				com.setCadastrialCode("CADASTRIAL_CODE");
//				com.setNome(rs.getString("NOME"));
//
//				comlist.add(com);
//			}
//			
//			logger.debug("getComuniByProvincia: found "+ comlist.size() + " item");
//		
//		} catch (SQLException e) {
//			logger.error("getComuniByProvincia: SQL failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY,
//					e.toString(), e);
//
//		} catch (RuntimeException e) {
//			logger.error("getComuniByProvincia failed", e);
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY,
//					e.toString());
//		} finally {
//			try {
//				DBConnectionManager.CloseConnection(rs, ps, conn);
//			} catch (DataAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				logger.error(e);
//			}
//		}
//		return comlist;
//	}
//
//	
//	
////	public Comuni findComuneByCodIstat(String codiceIstat) throws DataAccessException{
////		PreparedStatement ps = null;
////		Connection conn = null;
////		ResultSet rs = null;
////		Comuni com=null;
////
////		try{
////			conn = DBConnectionManager.DataMartConnectionFactory();
////
////			String sql = "select * from comuni where COD_ISTAT=? order by nome asc";
////
////			ps = conn.prepareStatement(sql);
////			ps.setString(1, codiceIstat);
////			rs = ps.executeQuery();
////			
////			while (rs.next()) {
////				com = new Comuni();
////				com.setIdCom(rs.getString("ID_COM"));
////				com.setCadastrialCode("CADASTRIAL_CODE");
////				com.setNome(rs.getString("NOME"));
////
////			}
////		} catch (SQLException e) {
////			logger.error("findComuneByCodIstat: SQL failed", e);
////			e.printStackTrace();
////			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY,
////					e.toString(), e);
////
////		} catch (RuntimeException e) {
////			logger.error("findComuneByCodIstat failed", e);
////			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY,
////					e.toString());
////		} finally {
////			try {
////				DBConnectionManager.CloseConnection(rs, ps, conn);
////			} catch (DataAccessException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////				logger.error(e);
////			}
////		}
////		return com;
////	}
//	
//	
//	public Comune findComuneByCodCatastale(String codiceCatasto) throws DataAccessException{
//		PreparedStatement ps = null;
//		Connection conn = null;
//		ResultSet rs = null;
//		Comune com=null;
//
//		try{
//			conn = DBConnectionManager.DataMartConnectionFactory();
//
//			String sql = "select * from comuni where cadastrial_code=? order by nome asc";
//
//			ps = conn.prepareStatement(sql);
//			ps.setString(1, codiceCatasto);
//			rs = ps.executeQuery();
//			
//			
//			while (rs.next()) {
//				com = new Comune();
//				com.setIdCom(rs.getString("ID_COM"));
//				com.setCadastrialCode("CADASTRIAL_CODE");
//				com.setNome(rs.getString("NOME"));
//			}
//			
//		} catch (SQLException e) {
//			logger.error("findComuneByCodCatastale: SQL failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY,
//					e.toString(), e);
//
//		} catch (RuntimeException e) {
//			logger.error("findComuneByCodCatastale failed", e);
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY,
//					e.toString());
//		} finally {
//			try {
//				DBConnectionManager.CloseConnection(rs, ps, conn);
//			} catch (DataAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				logger.error(e);
//			}
//		}
//		return com;
//
//	}
//	
//
//}