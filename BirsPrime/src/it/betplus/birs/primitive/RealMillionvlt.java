package it.betplus.birs.primitive;

import java.util.Date;

public class RealMillionvlt {

	long id;
	String aams_vlt_id;
	Date data;
	String aams_location_id;
	String location_name;
	long bet;
	long win;
	long bet_reale;
	long win_reale;

	public RealMillionvlt(long id, String aams_vlt_id, Date data,
			String aams_location_id, String location_name, long bet, long win,
			long bet_reale, long win_reale) {
		super();
		this.id = id;
		this.aams_vlt_id = aams_vlt_id;
		this.data = data;
		this.aams_location_id = aams_location_id;
		this.bet = bet;
		this.win = win;
		this.bet_reale = bet_reale;
		this.win_reale = win_reale;
	}

	public RealMillionvlt() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAams_vlt_id() {
		return aams_vlt_id;
	}

	public void setAams_vlt_id(String aams_vlt_id) {
		this.aams_vlt_id = aams_vlt_id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getAams_location_id() {
		return aams_location_id;
	}

	public void setAams_location_id(String aams_location_id) {
		this.aams_location_id = aams_location_id;
	}

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public long getBet() {
		return bet;
	}

	public void setBet(long bet) {
		this.bet = bet;
	}

	public long getWin() {
		return win;
	}

	public void setWin(long win) {
		this.win = win;
	}

	public long getBet_reale() {
		return bet_reale;
	}

	public void setBet_reale(long bet_reale) {
		this.bet_reale = bet_reale;
	}

	public long getWin_reale() {
		return win_reale;
	}

	public void setWin_reale(long win_reale) {
		this.win_reale = win_reale;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((aams_location_id == null) ? 0 : aams_location_id.hashCode());
		result = prime * result
				+ ((aams_vlt_id == null) ? 0 : aams_vlt_id.hashCode());
		result = prime * result + (int) (bet ^ (bet >>> 32));
		result = prime * result + (int) (bet_reale ^ (bet_reale >>> 32));
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((location_name == null) ? 0 : location_name.hashCode());
		result = prime * result + (int) (win ^ (win >>> 32));
		result = prime * result + (int) (win_reale ^ (win_reale >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RealMillionvlt other = (RealMillionvlt) obj;
		if (aams_location_id == null) {
			if (other.aams_location_id != null)
				return false;
		} else if (!aams_location_id.equals(other.aams_location_id))
			return false;
		if (aams_vlt_id == null) {
			if (other.aams_vlt_id != null)
				return false;
		} else if (!aams_vlt_id.equals(other.aams_vlt_id))
			return false;
		if (bet != other.bet)
			return false;
		if (bet_reale != other.bet_reale)
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (id != other.id)
			return false;
		if (location_name == null) {
			if (other.location_name != null)
				return false;
		} else if (!location_name.equals(other.location_name))
			return false;
		if (win != other.win)
			return false;
		if (win_reale != other.win_reale)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RealMillionvlt [id=" + id + ", aams_vlt_id=" + aams_vlt_id
				+ ", data=" + data + ", aams_location_id=" + aams_location_id
				+ ", location_name=" + location_name + ", bet=" + bet
				+ ", win=" + win + ", bet_reale=" + bet_reale + ", win_reale="
				+ win_reale + "]";
	}



}
