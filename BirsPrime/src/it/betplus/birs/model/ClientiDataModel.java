package it.betplus.birs.model;



import it.betplus.birs.primitive.Clientedim;

import java.util.List;

import javax.faces.model.ListDataModel;




public class ClientiDataModel extends ListDataModel<Clientedim> {  

    public ClientiDataModel() {
    }

    public ClientiDataModel(List<Clientedim> data) {
        super(data);
    }
    
    public Clientedim getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
        
        List<Clientedim> clienti = (List<Clientedim>) getWrappedData();
        
        for(Clientedim cli : clienti) {
            if(cli.getGestore().getCod_gestore().equals(rowKey))
                return cli;
        }
        
        return null;
    }
    
    public List<Clientedim> getData(){
    	List<Clientedim> clienti = (List<Clientedim>) getWrappedData();
    	return clienti;
    }
    
    

    public Object getRowKey(Clientedim cli) {
        return cli.getGestore().getCod_gestore();
    }
    
    
}
                    
