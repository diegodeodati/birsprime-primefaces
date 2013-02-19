package it.betplus.birs.db.dao;


import it.betplus.birs.connector.DBConnectionManager;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;



public class LogDAO {

	protected static Logger log = Logger.getLogger(LogDAO.class);
	
	private static LogDAO instance;

	private LogDAO() {
		super();
	}


	public static synchronized LogDAO getInstance() {
		if (instance == null) {
			synchronized (LogDAO.class) {
				instance = new LogDAO();
			}
		}
		return instance;
	}
	
	

   public List<Log> retrieve_log()  throws DataAccessException{
	   List<Log> listlog = new ArrayList<Log>();
		ResultSet rs=null;
		PreparedStatement ps=null;
		Connection connDataMart=null;
		
		try {
			connDataMart=DBConnectionManager.dataMartConnectionFactory();

						
			String sql = "select MAX(i.END_DATE) data ,i.AAMS_GAME_SYSTEM_CODE sys_code from importlog i group by i.AAMS_GAME_SYSTEM_CODE";
			
			log.info(sql);
			
			ps = connDataMart.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			while (rs.next()){
				Log l = new Log();
				l.setDataEnd(new java.util.Date(rs.getTimestamp("data").getTime()));
				l.autoSet_aams_game_system_code(rs.getString("sys_code"));

				listlog.add(l);
				
			}	
			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieve_logs: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException( e.toString(), e);
		}
		catch (Exception e) {
			log.error("retrieve_logs failed", e);
			e.printStackTrace();
			throw new DataAccessException( e.toString(), e);
		}
		finally{			
				try {
					rs.close();
					ps.close();					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		}
		return listlog;	   
   }
   
   
   public List<Log> retrieve_logByDate(Date d)  throws DataAccessException{
	   List<Log> listlog = new ArrayList<Log>();
		ResultSet rs=null;
		PreparedStatement ps=null;
		Connection connDataMart=null;
		
		try {
			connDataMart=DBConnectionManager.dataMartConnectionFactory();

						
			String sql = "select i.EXIT_CODE,i.START_DATE, i.END_DATE,i.OPERATION_DATE_START,i.OPERATION_DATE_END,i.AAMS_GAME_SYSTEM_CODE sys_code " +
					"from importlog i WHERE DATE(i.OPERATION_DATE_START) = DATE(?)";
			
			log.info(sql);
			
			ps = connDataMart.prepareStatement(sql);
			ps.setDate(1,new java.sql.Date(d.getTime()));
			
			rs = ps.executeQuery();
			
			while (rs.next()){
				Log l = new Log();
				l.setDataStart(new java.util.Date(rs.getTimestamp("START_DATE").getTime()));
				l.setDataEnd(new java.util.Date(rs.getTimestamp("END_DATE").getTime()));
				l.setOperation_dateStart(new java.util.Date(rs.getTimestamp("OPERATION_DATE_START").getTime()));
				l.setOperation_dateEnd(new java.util.Date(rs.getTimestamp("OPERATION_DATE_END").getTime()));
				l.autoSet_aams_game_system_code(rs.getString("sys_code"));
				l.setExit_code(rs.getInt("EXIT_CODE"));

				listlog.add(l);
				
			}	
			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieve_locations: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException( e.toString(), e);
		}
		catch (Exception e) {
			log.error("retrieve_locations failed", e);
			e.printStackTrace();
			throw new DataAccessException( e.toString(), e);
		}
		finally{			
				try {
					rs.close();
					ps.close();					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		}
		return listlog;	   
   }
 
   
}
