package it.betplus.birs.primitive;

import java.util.Date;

public class Game {

	String UGN;
	long AAMS_GAME_ID = -1;
	String NAME = "";
	long GS_GAME_ID = -1;
	double rtp = -1;
	long GS_ID = -1;
	Date ts;

	public Game() {
	}

	public String getUGN() {
		return UGN;
	}

	public void setUGN(String u) {
		UGN = u;
	}

	public long getAAMS_GAME_ID() {
		return AAMS_GAME_ID;
	}

	public void setAAMS_GAME_ID(long a) {
		AAMS_GAME_ID = a;
	}

	public String getNAME() {
		return NAME.replace("'", " ");
	}

	public void setNAME(String n) {
		if (n != null)
			NAME = n;
		else
			NAME = "";
	}

	public long getGS_GAME_ID() {
		return GS_GAME_ID;
	}

	public void setGS_GAME_ID(long gS_GAME_ID) {
		GS_GAME_ID = gS_GAME_ID;
	}

	public double getRtp() {
		return rtp;
	}

	public void setRtp(double rtp) {
		this.rtp = rtp;
	}

	public long getGS_ID() {
		return GS_ID;
	}

	public void setGS_ID(long gS_ID) {
		GS_ID = gS_ID;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (AAMS_GAME_ID ^ (AAMS_GAME_ID >>> 32));
		result = prime * result + (int) (GS_GAME_ID ^ (GS_GAME_ID >>> 32));
		result = prime * result + (int) (GS_ID ^ (GS_ID >>> 32));
		result = prime * result + ((NAME == null) ? 0 : NAME.hashCode());
		result = prime * result + ((UGN == null) ? 0 : UGN.hashCode());
		long temp;
		temp = Double.doubleToLongBits(rtp);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((ts == null) ? 0 : ts.hashCode());
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
		Game other = (Game) obj;
		if (AAMS_GAME_ID != other.AAMS_GAME_ID)
			return false;
		if (GS_GAME_ID != other.GS_GAME_ID)
			return false;
		if (GS_ID != other.GS_ID)
			return false;
		if (NAME == null) {
			if (other.NAME != null)
				return false;
		} else if (!NAME.equals(other.NAME))
			return false;
		if (UGN == null) {
			if (other.UGN != null)
				return false;
		} else if (!UGN.equals(other.UGN))
			return false;
		if (Double.doubleToLongBits(rtp) != Double.doubleToLongBits(other.rtp))
			return false;
		if (ts == null) {
			if (other.ts != null)
				return false;
		} else if (!ts.equals(other.ts))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Game [UGN=" + UGN + ", AAMS_GAME_ID=" + AAMS_GAME_ID
				+ ", NAME=" + NAME + ", GS_GAME_ID=" + GS_GAME_ID + ", rtp="
				+ rtp + ", GS_ID=" + GS_ID + ", ts=" + ts + "]";
	}

}
