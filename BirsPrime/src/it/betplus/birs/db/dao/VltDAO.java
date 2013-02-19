package it.betplus.birs.db.dao;


import it.betplus.birs.connector.DBConnectionManager;
import it.betplus.birs.exception.DataAccessException;
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



public class VltDAO {

	protected static Logger log = Logger.getLogger(VltDAO.class);
	
	private static VltDAO instance;

	private VltDAO() {
		super();
	}


	public static synchronized VltDAO getInstance() {
		if (instance == null) {
			synchronized (VltDAO.class) {
				instance = new VltDAO();
			}
		}
		return instance;
	}
	
	

   public List<Vlt> retrieve_vlts(Date data2, String aams_location_id)  throws DataAccessException{
	   List<Vlt> listVlt = null;
		ResultSet rs=null;
		PreparedStatement ps=null;
		Connection connDataMart=null;
		
		try {
			connDataMart=DBConnectionManager.dataMartConnectionFactory();
			
			//DateFormat df = new SimpleDateFormat(IConstants.DATE_FORMAT);
						
			String sql = "select h.AAMS_VLT_CODE AAMS_VLT_CODE, v.GS_VLT_CODE GS_VLT_CODE " +
					     "from aggregate.vlthistory h " +
					     "inner join aggregate.birsvlt v on h.AAMS_VLT_CODE = v.AAMS_VLT_CODE " +
					     "inner join ( select max(DATE_CHANGE) maximo, AAMS_VLT_CODE from aggregate.vlthistory where DATE_CHANGE <= ? group by AAMS_VLT_CODE ) " +
					     "xx on h.AAMS_VLT_CODE = xx.AAMS_VLT_CODE and  h.DATE_CHANGE = xx.maximo where " +
					     "(v.DATE_OUT is null or v.DATE_OUT >= ?)" +
					     "and h.DATE_CHANGE <=? and h.AAMS_LOCATION_CODE like ?";
			
			log.info(sql);
			
			ps = connDataMart.prepareStatement(sql);
			
			
			java.sql.Timestamp data2_ = new java.sql.Timestamp(DateUtils
					.generateLastSecondDate(data2).getTime());
			

		    ps.setTimestamp(1, data2_);
		    ps.setTimestamp(2, data2_);
		    ps.setTimestamp(3, data2_);
		    ps.setString(4,aams_location_id);
	
		    log.info(aams_location_id+"****"+data2_);
			
			rs = ps.executeQuery();
			
			listVlt = new ArrayList<Vlt>();
			
			while (rs.next()){
				Vlt vlt = new Vlt();
				vlt.setAAMS_VLT_ID(rs.getString("AAMS_VLT_CODE"));
				vlt.setGS_VLT_ID(rs.getString("GS_VLT_CODE"));
				
				listVlt.add(vlt);
				
			}	
			DBConnectionManager.CloseConnection(connDataMart);
		} catch (SQLException e) {
			log.error("retrieve_vlts: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException( e.toString(), e);
		}		
		catch (ArrayIndexOutOfBoundsException e) {
			log.error("EXCEPTION: retrieve_vlts ArrayIndexOutOfBoundsException", e);
			throw new DataAccessException( e.toString(), e);
		}
		catch (Exception e) {
			log.error("retrieve_vlts failed", e);
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
		return listVlt;	   
   }
   
 
   
}
