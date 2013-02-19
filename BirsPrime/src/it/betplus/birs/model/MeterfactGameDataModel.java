package it.betplus.birs.model;


import it.betplus.birs.primitive.MeterfactGame;

import java.util.List;

import javax.faces.model.ListDataModel;




public class MeterfactGameDataModel extends ListDataModel<MeterfactGame> implements java.io.Serializable  {  


	private static final long serialVersionUID = 1L;


	public MeterfactGameDataModel() {
    }

    public MeterfactGameDataModel(List<MeterfactGame> data) {
        super(data);
    }
    
    public MeterfactGame getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
        
        List<MeterfactGame> modelData = (List<MeterfactGame>) getWrappedData();
        
        for(MeterfactGame mfg : modelData) {
            if(String.valueOf(mfg.getTempodim().simpleTempodimToString()+"-"+mfg.getSistemagiocodim().getVlt().getAAMS_VLT_ID()+"-"+mfg.getSistemagiocodim().getGame().getAAMS_GAME_ID()).equals(rowKey))
                return mfg;
        }
        
        return null;
    }
    
    public List<MeterfactGame> getData(){
    	List<MeterfactGame> modelData = (List<MeterfactGame>) getWrappedData();
    	return modelData;
    }
    
    

    public Object getRowKey(MeterfactGame mfg) {
    	 return mfg.getTempodim().simpleTempodimToString()+"-"+mfg.getSistemagiocodim().getVlt().getAAMS_VLT_ID()+"-"+mfg.getSistemagiocodim().getGame().getAAMS_GAME_ID();
    }
    
    
}
                    