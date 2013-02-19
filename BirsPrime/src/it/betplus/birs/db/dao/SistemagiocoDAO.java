package it.betplus.birs.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import it.betplus.birs.connector.DBConnectionManager;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.primitive.SistemaGioco;

public class SistemagiocoDAO {

	protected static Logger log = Logger.getLogger(SistemagiocoDAO.class);

	private static SistemagiocoDAO instance;

	private SistemagiocoDAO() {
		super();
	}

	public static synchronized SistemagiocoDAO getInstance() {
		if (instance == null) {
			synchronized (SistemagiocoDAO.class) { // 3
				instance = new SistemagiocoDAO();
			}
		}
		return instance;
	}

	
	

	public SelectItem[] retrieve_sistema_gioco(boolean opt) throws DataAccessException {
		List<SistemaGioco> listSysG = new ArrayList<SistemaGioco>();
		
		SelectItem[] selectItems = {};
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection connDataMart = null;
		

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "select distinct(s.AAMS_GAMESYSTEM_CODE) AAMS_GAMESYSTEM_CODE,a.NAME NAME " +
						 "from sistemagiocodim s " +
					     "inner join aggregate.birsgamesystem a on s.AAMS_GAMESYSTEM_CODE = a.AAMS_GAME_SYSTEM_CODE";

			log.info(sql);

			ps = connDataMart.prepareStatement(sql);

			rs = ps.executeQuery();

			
			while (rs.next()) {
				SistemaGioco sysG = new SistemaGioco(rs.getString("NAME"),
						rs.getLong("AAMS_GAMESYSTEM_CODE"));
				listSysG.add(sysG);
			}
			
			SelectItem[] sistemagiocodimOptions = new SelectItem[listSysG.size() + 1];  
			int i = 0;
			
			if(opt)
				sistemagiocodimOptions[0] = new SelectItem("", "Tutti");
			else				
			    sistemagiocodimOptions[0] = new SelectItem("%", "Tutti");
			
			
			
			for(SistemaGioco sysG : listSysG){
				sistemagiocodimOptions[i + 1] = new SelectItem(sysG.getAAMS_SISTEMAGIOCO_ID(), sysG.getNOME());  
				i++;
			}

			selectItems = sistemagiocodimOptions;
			
			DBConnectionManager.CloseConnection(connDataMart);

		} catch (Exception e) {
			log.error("retrieve_sistema_gioco: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException( e.toString(), e);
		} finally {
			try {
				rs.close();
				ps.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return selectItems;

	}
}