//package it.betplus.birs.db.dao;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import it.bplus.db.DBConnectionManager;
//import it.bplus.exception.DataAccessException;
//import it.bplus.model.Regioni;
//import it.bplus.util.IErrorCodes;
//
//public class RegioniDAO {
//
//	protected static Logger logger = Logger.getLogger(RegioniDAO.class);
//
//
//
//	private static RegioniDAO instance;
//
//	private RegioniDAO() {
//		super();
//	}
//
//
//
//	public static synchronized RegioniDAO getInstance() {
//		if (instance == null) {
//			synchronized (RegioniDAO.class) {
//				instance = new RegioniDAO();
//			}
//		}
//		return instance;
//	}
//
//	public List<Regione> getAll() throws DataAccessException{
//		Statement st = null;
//		Connection conn = null;
//		ResultSet rs = null;
//		List<Regione> reglist = null;
//		
//		try{
//			conn = DBConnectionManager.DataMartConnectionFactory();
//			
//			String sql = "select id_reg,nome from regioni order by nome asc";
//			
//			st = conn.createStatement();
//			rs = st.executeQuery(sql);
//			
//			reglist = new ArrayList<Regione>();
//			while (rs.next()){
//				Regione reg = new Regione();
//				reg.setIdReg(rs.getString("ID_REG"));
//				reg.setNome(rs.getString("NOME"));
//				
//				reglist.add(reg);
//			}		
//			if(reglist!=null && reglist.size()>0){
//				logger.debug("successful, " + reglist.size() + " regioni instances found");
//
//			}else{
//				logger.debug(" successful, no regioni instance found");
//				return null;
//			}
//		} catch (SQLException e) {
//			logger.error("regioni_getAll: SQL failed", e);
//			e.printStackTrace();
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY, e.toString(), e);
//		
//		}catch(RuntimeException e){
//			logger.error("regioni_getAll failed", e);
//			throw new DataAccessException(IErrorCodes.DB_ERROR_KEY,e.toString());
//		}
//		finally{
//			try {
//				DBConnectionManager.CloseConnection(rs, st, conn);				
//			} catch (DataAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				logger.error(e);
//			}
//	}
//		
//		return reglist;
//	}
//
//
//}