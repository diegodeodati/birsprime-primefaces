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
//import it.bplus.model.Province;
//import it.bplus.util.IErrorCodes;
//
//public class ProvinceDAO {
//
//	protected static Logger logger = Logger.getLogger(ProvinceDAO.class);
//
//
//	private static ProvinceDAO instance;
//
//	private ProvinceDAO() {
//		super();
//	}
//
//
//	public static synchronized ProvinceDAO getInstance() {
//		if (instance == null) {
//			synchronized (ProvinceDAO.class) { 
//				instance = new ProvinceDAO();
//			}
//		}
//		return instance;
//	}
//
//
//
//	public List<Provincia> getProvinceByIdRegione(String id_regione) throws DataAccessException{
//		PreparedStatement ps = null;
//		Connection conn = null;
//		ResultSet rs = null;
//		List<Provincia> prolist = null;
//		try{
//			conn = DBConnectionManager.DataMartConnectionFactory();
//
//			String sql = "select * from province where id_reg=? order by nome asc";
//
//			ps = conn.prepareStatement(sql);
//			ps.setString(1, id_regione);
//			rs = ps.executeQuery();
//
//			prolist = new ArrayList<Provincia>();
//			while (rs.next()) {
//				Provincia pro = new Provincia();
//				pro.setIdProv(rs.getString("ID_PROV"));
//				pro.setSigla("SIGLA");
//				pro.setNome(rs.getString("NOME"));
//
//				prolist.add(pro);
//			}
//			if (prolist != null && prolist.size() > 0) {
//				logger.debug("successful, " + prolist.size()
//						+ " province instances found");
//
//			} else {
//				logger.debug(" successful, no province instance found");
//				return null;
//			}
//		} catch (SQLException e) {
//			logger.error("getProvinceByIdRegione: SQL failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY,
//					e.toString(), e);
//
//		} catch (RuntimeException e) {
//			logger.error("getProvinceByIdRegione failed", e);
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
//		return prolist;
//	}
//}