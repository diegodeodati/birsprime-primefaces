package it.betplus.birs.model;

import it.betplus.birs.primitive.Matcher;

import java.util.List;

import javax.faces.model.ListDataModel;

public class MatcherLocationDataModel extends
		ListDataModel<Matcher> implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public MatcherLocationDataModel() {
	}

	public MatcherLocationDataModel(List<Matcher> data) {
		super(data);
	}

	public Matcher getRowData(String rowKey) {
		// In a real app, a more efficient way like a query by rowKey should be
		// implemented to deal with huge data

		List<Matcher> modelData = (List<Matcher>) getWrappedData();

		for (Matcher m : modelData) {
			if ((m.getTempodim().simpleTempodimToString()+"-"+m.getSistemagiocodim().getLoc().getAAMS_LOCATION_ID())
					.equals(rowKey))
				return m;
		}

		return null;
	}

	public List<Matcher> getData() {
		List<Matcher> modelData = (List<Matcher>) getWrappedData();
		return modelData;
	}

	public Object getRowKey(Matcher m) {
		return m.getTempodim().simpleTempodimToString()+"-"+m.getSistemagiocodim().getLoc().getAAMS_LOCATION_ID();
	}

}
