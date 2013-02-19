package it.betplus.birs.model;


import it.betplus.birs.primitive.MeterfactVlt;

import java.util.List;

import javax.faces.model.ListDataModel;

public class MeterfactVltDataModel extends ListDataModel<MeterfactVlt> implements java.io.Serializable  {  


	private static final long serialVersionUID = 1L;


	public MeterfactVltDataModel() {
    }

    public MeterfactVltDataModel(List<MeterfactVlt> data) {
        super(data);
    }
    
    public MeterfactVlt getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
        
        List<MeterfactVlt> modelData = (List<MeterfactVlt>) getWrappedData();
        
        for(MeterfactVlt mfv : modelData) {
            if((mfv.getTempodim().simpleTempodimToString()+"-"+mfv.getSistemagiocodim().getVlt().getAAMS_VLT_ID()).equals(rowKey))
                return mfv;
        }
        
        return null;
    }
    
    public List<MeterfactVlt> getData(){
    	List<MeterfactVlt> modelData = (List<MeterfactVlt>) getWrappedData();
    	return modelData;
    }
    
    

    public Object getRowKey(MeterfactVlt mfv) {
        return mfv.getTempodim().simpleTempodimToString()+"-"+mfv.getSistemagiocodim().getVlt().getAAMS_VLT_ID();
    }
    
    
}
                    