package it.betplus.birs.db.dao;


import it.betplus.birs.connector.DBConnectionManager;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.Location;
import it.betplus.web.framework.utils.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;



public class LocationDAO {

	protected static Logger log = Logger.getLogger(LocationDAO.class);
	
	private static LocationDAO instance;

	private LocationDAO() {
		super();
	}


	public static synchronized LocationDAO getInstance() {
		if (instance == null) {
			synchronized (LocationDAO.class) {
				instance = new LocationDAO();
			}
		}
		return instance;
	}
	
	

   public List<Location> retrieve_locations(Date data1, Date data2)  throws DataAccessException{
	   List<Location> listLoc = null;
		ResultSet rs=null;
		PreparedStatement ps=null;
		Connection connDataMart=null;
		
		try {
			connDataMart=DBConnectionManager.dataMartConnectionFactory();

						
			String sql = "select AAMS_LOCATION_CODE,COMMERCIAL_NAME from aggregate.BIRSLOCATION " +
					"where OPENING_DATE<=? and (CESSATION_DATE>=? or CESSATION_DATE is NULL) order by COMMERCIAL_NAME";
			
			log.info(sql);
			
			ps = connDataMart.prepareStatement(sql);
			
				
			
			java.sql.Date data1_ = new java.sql.Date(data2.getTime());
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());
			
			ps.setDate(1, data1_);
			ps.setTimestamp(2, data2_);
	
			log.info(data1_+"****"+data2_);
			
			rs = ps.executeQuery();
			
			listLoc = new ArrayList<Location>();
			
			while (rs.next()){
				Location loc = new Location();
				loc.setAAMS_LOCATION_ID(rs.getString("AAMS_LOCATION_CODE"));
				loc.setCOMMERCIAL_NAME(rs.getString("COMMERCIAL_NAME"));
				
				listLoc.add(loc);
				
			}	
			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieve_locations: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException( e.toString(), e);
		}		
		catch (ArrayIndexOutOfBoundsException e) {
			log.error("EXCEPTION: retrieve_locations ArrayIndexOutOfBoundsException", e);
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
		return listLoc;	   
   }
   
 
   
}
