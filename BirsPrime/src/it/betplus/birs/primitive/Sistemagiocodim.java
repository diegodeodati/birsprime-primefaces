package it.betplus.birs.primitive;

/**
 * Sistemagiocodim generated by hbm2java
 */
public class Sistemagiocodim implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long AAMS_GAMESYSTEM_ID = 0;
	private Location loc;
	private Vlt vlt;
	private Game game;

	public Sistemagiocodim() {
		super();
	}

	public long getAAMS_GAMESYSTEM_ID() {
		return AAMS_GAMESYSTEM_ID;
	}

	public String getAAMS_GAMESYSTEM_NAME() {
		if (AAMS_GAMESYSTEM_ID == 1711000045)
			return "Novomatic";
		else if (AAMS_GAMESYSTEM_ID == 1711000065)
			return "Inspired";
		else
			return "Sconosciuto";
	}

	public void setAAMS_GAMESYSTEM_ID(long id) {
		this.AAMS_GAMESYSTEM_ID = id;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public Vlt getVlt() {
		return vlt;
	}

	public void setVlt(Vlt vlt) {
		this.vlt = vlt;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((game == null) ? 0 : game.hashCode());
		result = prime * result
				+ (int) (AAMS_GAMESYSTEM_ID ^ (AAMS_GAMESYSTEM_ID >>> 32));
		result = prime * result + ((loc == null) ? 0 : loc.hashCode());
		result = prime * result + ((vlt == null) ? 0 : vlt.hashCode());
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
		Sistemagiocodim other = (Sistemagiocodim) obj;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		if (AAMS_GAMESYSTEM_ID != other.AAMS_GAMESYSTEM_ID)
			return false;
		if (loc == null) {
			if (other.loc != null)
				return false;
		} else if (!loc.equals(other.loc))
			return false;
		if (vlt == null) {
			if (other.vlt != null)
				return false;
		} else if (!vlt.equals(other.vlt))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sistemagiocodim [id=" + AAMS_GAMESYSTEM_ID + ", loc=" + loc
				+ ", vlt=" + vlt + ", game=" + game + "]";
	}

}
