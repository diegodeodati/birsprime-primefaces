package it.betplus.birs.db.dao;

import it.betplus.birs.connector.DBConnectionManager;
import it.betplus.birs.exception.DataAccessException;
import it.betplus.birs.exception.DataLayerException;
import it.betplus.birs.primitive.VltModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class VltModelDAO {

	protected static Logger log = Logger.getLogger(VltModelDAO.class);

	private static VltModelDAO instance;

	private VltModelDAO() {
		super();
	}

	public static synchronized VltModelDAO getInstance() {
		if (instance == null) {
			synchronized (VltModelDAO.class) {
				instance = new VltModelDAO();
			}
		}
		return instance;
	}
	


	public List<VltModel> getVltModels() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connDataMart = null;
		VltModel vModel = null;
		List<VltModel> allVltModel = new ArrayList<VltModel>();

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "select AAMS_VLT_MODEL_CODE,NAME,AAMS_GAME_SYSTEM_CODE,MANUFACTURER,CABINET_TYPE,PCT_SUPPLIER " +
					"from aggregate.birsvltmodel";

			ps = connDataMart.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {

				vModel = new VltModel();
				vModel.setAAMS_VLT_MODEL_CODE(rs.getLong("AAMS_VLT_MODEL_CODE"));
				vModel.setNAME(rs.getString("NAME"));
				vModel.setAAMS_GAME_SYSTEM_CODE(rs.getLong("AAMS_GAME_SYSTEM_CODE"));
				vModel.setMANUFACTURER(rs.getString("MANUFACTURER"));
				vModel.setCABINET_TYPE(rs.getString("CABINET_TYPE"));
				vModel.setPCT_SUPPLIER(rs.getDouble("PCT_SUPPLIER"));
				

				allVltModel.add(vModel);
			}

			DBConnectionManager.CloseConnection(connDataMart);

		} catch (SQLException e) {
			log.error("getUtenti: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return allVltModel;

	}

	
	public int insertNewVltModel(VltModel vModel) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connDataMart = null;
		int risultato = 0;

		if (!vltModelExist(vModel.getAAMS_VLT_MODEL_CODE())) {

			try {
				connDataMart = DBConnectionManager.dataMartConnectionFactory();

				connDataMart.setAutoCommit(false);
				
				String sql = "INSERT INTO aggregate.birsvltmodel " +
							 "(AAMS_VLT_MODEL_CODE, NAME, AAMS_GAME_SYSTEM_CODE, MANUFACTURER, CABINET_TYPE, PCT_SUPPLIER) " +
							 "VALUES (?, ?, ?, ?, ?, ?)";

				ps = connDataMart.prepareStatement(sql);
				ps.setLong(1, vModel.getAAMS_VLT_MODEL_CODE());
				ps.setString(2, vModel.getNAME());
				ps.setLong(3,vModel.getAAMS_GAME_SYSTEM_CODE());
				ps.setString(4, vModel.getMANUFACTURER());
				ps.setString(5, vModel.getCABINET_TYPE());
				ps.setDouble(6, vModel.getPCT_SUPPLIER());

				risultato = ps.executeUpdate();

				if (risultato == 1)
					connDataMart.commit();
				else
					connDataMart.rollback();

				DBConnectionManager.CloseConnection(connDataMart);

			} catch (SQLException e) {
				log.error("inserisciNuovoUtente: SQL failed", e);
				e.printStackTrace();
				throw new DataAccessException(e.toString(), e);
			} catch (DataLayerException e) {
				e.printStackTrace();
			} finally {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return risultato;
	}

	public boolean vltModelExist(long aams_vlt_model_code) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connDataMart = null;
		ResultSet rs = null;
		boolean exist = false;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();

			String sql = "select AAMS_VLT_MODEL_CODE from AGGREGATE.birsvltmodel where AAMS_VLT_MODEL_CODE=?";

			ps = connDataMart.prepareStatement(sql);
			ps.setLong(1, aams_vlt_model_code);

			rs = ps.executeQuery();

			if (rs.next())
				exist = true;

			DBConnectionManager.CloseConnection(connDataMart);

		} catch (SQLException e) {
			log.error("vltModelExist: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		}  catch (DataLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return exist;
	}

	public int editVltModel(VltModel vModel)
			throws DataAccessException {
		PreparedStatement ps = null;
		Connection connDataMart = null;
		int risultato = 0;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();
            connDataMart.setAutoCommit(false);
			

			String sql = "UPDATE aggregate.birsvltmodel SET PCT_SUPPLIER = ? WHERE AAMS_VLT_MODEL_CODE=?";


			ps = connDataMart.prepareStatement(sql);			
			ps.setDouble(1, vModel.getPCT_SUPPLIER());
			ps.setLong(2,vModel.getAAMS_VLT_MODEL_CODE());

			risultato = ps.executeUpdate();

	
			if (risultato > 0)
				connDataMart.commit();
			else
				connDataMart.rollback();

			DBConnectionManager.CloseConnection(connDataMart);

		} catch (SQLException e) {
			log.error("modificaUtente: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		}catch (DataLayerException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return risultato;
	}
/*
	public int eliminaUtente(String username) throws DataAccessException {
		PreparedStatement ps = null;
		Connection connDataMart = null;
		int risultato = 0;

		try {
			connDataMart = DBConnectionManager.dataMartConnectionFactory();
            connDataMart.setAutoCommit(false);
			
			String sql_1 = "delete from aggregate.ruoli where username=?";
			ps = connDataMart.prepareStatement(sql_1);
			ps.setString(1, username);
			risultato = ps.executeUpdate();

			String sql = "delete from aggregate.utenti where username=?";

			ps = connDataMart.prepareStatement(sql);
			ps.setString(1, username);
			risultato = ps.executeUpdate();

			if (risultato > 0)
				connDataMart.commit();
			else
				connDataMart.rollback();

			DBConnectionManager.CloseConnection(connDataMart);

		} catch (SQLException e) {
			log.error("eliminaUtente: SQL failed", e);
			e.printStackTrace();
			throw new DataAccessException(e.toString(), e);
		} catch (RuntimeException e) {
			throw new DataAccessException(e.toString());
		} catch (DataLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return risultato;
	}*/
}