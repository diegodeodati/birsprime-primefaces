package it.betplus.birs.model;


import it.betplus.birs.primitive.MeterfactRegion;

import java.util.List;

import javax.faces.model.ListDataModel;




public class MeterfactRegionDataModel extends ListDataModel<MeterfactRegion> implements java.io.Serializable  {  


	private static final long serialVersionUID = 1L;


	public MeterfactRegionDataModel() {
    }

    public MeterfactRegionDataModel(List<MeterfactRegion> data) {
        super(data);
    }
    
    public MeterfactRegion getRowData(int rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
        
        List<MeterfactRegion> modelData = (List<MeterfactRegion>) getWrappedData();
        
        for(MeterfactRegion mfr : modelData) {
            if((mfr.getTempodim().simpleTempodimToString()+"-"+mfr.getSpaziodim().getRegione().getIdReg()).equals(rowKey))
                return mfr;
        }
        
        return null;
    }
    
    public List<MeterfactRegion> getData(){
    	List<MeterfactRegion> modelData = (List<MeterfactRegion>) getWrappedData();
    	return modelData;
    }
    
    

    public Object getRowKey(MeterfactRegion mfr) {
        return mfr.getTempodim().simpleTempodimToString()+"-"+mfr.getSpaziodim().getRegione().getIdReg();
    }
    
    
}
                    