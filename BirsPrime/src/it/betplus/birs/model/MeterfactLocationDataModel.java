package it.betplus.birs.model;

import it.betplus.birs.primitive.MeterfactLocation;

import java.util.List;

import javax.faces.model.ListDataModel;

public class MeterfactLocationDataModel extends
		ListDataModel<MeterfactLocation> implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public MeterfactLocationDataModel() {
	}

	public MeterfactLocationDataModel(List<MeterfactLocation> data) {
		super(data);
	}

	public MeterfactLocation getRowData(String rowKey) {
		// In a real app, a more efficient way like a query by rowKey should be
		// implemented to deal with huge data

		List<MeterfactLocation> modelData = (List<MeterfactLocation>) getWrappedData();

		for (MeterfactLocation mfl : modelData) {
			if ((mfl.getTempodim().simpleTempodimToString()+"-"+mfl.getSistemagiocodim().getAAMS_GAMESYSTEM_ID()+"-"+mfl.getSistemagiocodim().getLoc().getAAMS_LOCATION_ID())
					.equals(rowKey))
				return mfl;
		}

		return null;
	}

	public List<MeterfactLocation> getData() {
		List<MeterfactLocation> modelData = (List<MeterfactLocation>) getWrappedData();
		return modelData;
	}

	public Object getRowKey(MeterfactLocation mfl) {
		return mfl.getTempodim().simpleTempodimToString()+"-"+mfl.getSistemagiocodim().getAAMS_GAMESYSTEM_ID()+"-"+mfl.getSistemagiocodim().getLoc().getAAMS_LOCATION_ID();
	}

}
