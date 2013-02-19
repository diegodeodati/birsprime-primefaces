package it.betplus.birs.primitive;

import java.io.Serializable;
import java.util.ArrayList;

public class Matcher implements Serializable {

	private static final long serialVersionUID = 3346508895905662229L;

	private Meterfact mSogei;
	private Meterfact m600;

	public Meterfact getMSogei() {
		return mSogei;
	}

	public void setMSogei(Meterfact mSogei) {
		this.mSogei = mSogei;
	}

	public Meterfact getM600() {
		return m600;
	}

	public void setM600(Meterfact m600) {
		this.m600 = m600;
	}

	public Sistemagiocodim getSistemagiocodim() {

		if (mSogei.getSistemagiocodim() != null)
			return mSogei.getSistemagiocodim();
		else
			return m600.getSistemagiocodim();
	}

	public Spaziodim getSpaziodim() {

		if (mSogei.getSistemagiocodim() != null)
			return mSogei.getSpaziodim();
		else
			return m600.getSpaziodim();
	}

	public Tempodim getTempodim() {
		if (mSogei.getTempodim() != null)
			return mSogei.getTempodim();
		else
			return m600.getTempodim();
	}

	public String getColorDiffBet() {
		double diff = mSogei.getBet() - m600.getBet();

		if (diff > 3000 || diff < -3000)
			return "color:WHITE;background-color:rgba(153, 0, 0, 0.6)";
		else if (diff > 2000 || diff < -2000)
			return "color:WHITE;background-color:rgba(255, 0, 0, 0.6)";
		else if (diff > 1000 || diff < -1000)
			return "background-color:rgba(255, 153, 0, 0.6)";
		else if (diff != 0)
			return "background-color:rgba(255, 255, 0, 0.6)";
		else
			return "";
	}

	public String getColorDiffWin() {
		double diff = mSogei.getWin() - m600.getWin();

		if (diff > 3000 || diff < -3000)
			return "color:WHITE;background-color:rgba(153, 0, 0, 0.6)";
		else if (diff > 2000 || diff < -2000)
			return "color:WHITE;background-color:rgba(255, 0, 0, 0.6)";
		else if (diff > 1000 || diff < -1000)
			return "background-color:rgba(255, 153, 0, 0.6)";
		else if (diff != 0)
			return "background-color:rgba(255, 255, 0, 0.6)";
		else
			return "";
	}

	public String getColorDiffGamesPlayed() {
		double diff = mSogei.getGamesPlayed() - m600.getGamesPlayed();

		if (diff > 3000 || diff < -3000)
			return "color:WHITE;background-color:rgba(153, 0, 0, 0.6)";
		else if (diff > 2000 || diff < -2000)
			return "color:WHITE;background-color:rgba(255, 0, 0, 0.6)";
		else if (diff > 1000 || diff < -1000)
			return "background-color:rgba(255, 153, 0, 0.6)";
		else if (diff != 0)
			return "background-color:rgba(255, 255, 0, 0.6)";
		else
			return "";
	}

	public String getColorDiffTotalIn() {
		double diff = 0d;

		if (mSogei instanceof MeterfactLocation)
			diff = ((MeterfactLocation) mSogei).getTotalIn()
					- ((MeterfactLocation) m600).getTotalIn();
		else
			diff = ((MeterfactVlt) mSogei).getTotalIn()
					- ((MeterfactVlt) m600).getTotalIn();

		if (diff > 3000 || diff < -3000)
			return "color:WHITE;background-color:rgba(153, 0, 0, 0.6)";
		else if (diff > 2000 || diff < -2000)
			return "color:WHITE;background-color:rgba(255, 0, 0, 0.6)";
		else if (diff > 1000 || diff < -1000)
			return "background-color:rgba(255, 153, 0, 0.6)";
		else if (diff != 0)
			return "background-color:rgba(255, 255, 0, 0.6)";
		else
			return "";
	}

	public String getColorDiffTotalOut() {
		double diff = 0d;

		if (mSogei instanceof MeterfactLocation)
			diff = ((MeterfactLocation) mSogei).getTotalOut()
					- ((MeterfactLocation) m600).getTotalOut();
		else
			diff = ((MeterfactVlt) mSogei).getTotalOut()
					- ((MeterfactVlt) m600).getTotalOut();

		if (diff > 3000 || diff < -3000)
			return "color:WHITE;background-color:rgba(153, 0, 0, 0.6)";
		else if (diff > 2000 || diff < -2000)
			return "color:WHITE;background-color:rgba(255, 0, 0, 0.6)";
		else if (diff > 1000 || diff < -1000)
			return "background-color:rgba(255, 153, 0, 0.6)";
		else if (diff != 0)
			return "background-color:rgba(255, 255, 0, 0.6)";
		else
			return "";
	}

	public boolean isDiffBet() {
		return mSogei.getBet().longValue() != m600.getBet().longValue();
	}

	public boolean isDiffWin() {
		return mSogei.getWin().longValue() != m600.getWin().longValue();
	}

	public boolean isDiffGamesPlayed() {
		return mSogei.getGamesPlayed().longValue() != m600.getGamesPlayed()
				.longValue();
	}

	public boolean isDiffTotalIn() {

		if (mSogei instanceof MeterfactLocation)
			return ((MeterfactLocation) mSogei).getTotalIn().longValue() != ((MeterfactLocation) m600)
					.getTotalIn().longValue();
		else
			return ((MeterfactVlt) mSogei).getTotalIn().longValue() != ((MeterfactVlt) m600)
					.getTotalIn().longValue();

	}

	public boolean isDiffTotalOut() {

		if (mSogei instanceof MeterfactLocation)
			return ((MeterfactLocation) mSogei).getTotalOut().longValue() != ((MeterfactLocation) m600)
					.getTotalOut().longValue();
		else
			return ((MeterfactVlt) mSogei).getTotalOut().longValue() != ((MeterfactVlt) m600)
					.getTotalOut().longValue();

	}

	public Matcher sumValue(Matcher add) {

		this.getMSogei().setBet(
				add.getMSogei().getBet() + this.getMSogei().getBet());
		this.getMSogei().setWin(
				add.getMSogei().getWin() + this.getMSogei().getWin());
		this.getMSogei().setGamesPlayed(
				add.getMSogei().getGamesPlayed()
						+ this.getMSogei().getGamesPlayed());

		if (add.getMSogei() instanceof MeterfactLocation) {
			((MeterfactLocation) this.getMSogei())
					.setTotalIn(((MeterfactLocation) add.getMSogei())
							.getTotalIn()
							+ ((MeterfactLocation) this.getMSogei())
									.getTotalIn());
			((MeterfactLocation) this.getMSogei())
					.setTotalOut(((MeterfactLocation) add.getMSogei())
							.getTotalOut()
							+ ((MeterfactLocation) this.getMSogei())
									.getTotalOut());
		} else if (add.getMSogei() instanceof MeterfactVlt) {
			((MeterfactVlt) this.getMSogei()).setTotalIn(((MeterfactVlt) add
					.getMSogei()).getTotalIn()
					+ ((MeterfactVlt) this.getMSogei()).getTotalIn());
			((MeterfactVlt) this.getMSogei()).setTotalOut(((MeterfactVlt) add
					.getMSogei()).getTotalOut()
					+ ((MeterfactVlt) this.getMSogei()).getTotalOut());
		}

		this.getM600().setBet(add.getM600().getBet() + this.getM600().getBet());
		this.getM600().setWin(add.getM600().getWin() + this.getM600().getWin());
		this.getM600().setGamesPlayed(
				add.getM600().getGamesPlayed()
						+ this.getM600().getGamesPlayed());

		if (add.getM600() instanceof MeterfactLocation) {
			((MeterfactLocation) this.getM600())
					.setTotalIn(((MeterfactLocation) add.getM600())
							.getTotalIn()
							+ ((MeterfactLocation) this.getM600()).getTotalIn());
			((MeterfactLocation) this.getM600())
					.setTotalOut(((MeterfactLocation) add.getM600())
							.getTotalOut()
							+ ((MeterfactLocation) this.getM600())
									.getTotalOut());
		} else if (add.getM600() instanceof MeterfactVlt) {
			((MeterfactVlt) this.getM600()).setTotalIn(((MeterfactVlt) add
					.getM600()).getTotalIn()
					+ ((MeterfactVlt) this.getM600()).getTotalIn());
			((MeterfactVlt) this.getM600()).setTotalOut(((MeterfactVlt) add
					.getM600()).getTotalOut()
					+ ((MeterfactVlt) this.getM600()).getTotalOut());
		}

		return this;

	}

	public Matcher clone() {

		Matcher clone = new Matcher();

		if (this.getMSogei() instanceof MeterfactLocation) {
			MeterfactLocation mLocSogei = new MeterfactLocation();
			MeterfactLocation mLoc600 = new MeterfactLocation();

			mLocSogei.setBet(this.getMSogei().getBet());
			mLocSogei.setWin(this.getMSogei().getWin());
			mLocSogei.setGamesPlayed(this.getMSogei().getGamesPlayed());
			mLocSogei.setTotalIn(((MeterfactLocation) this.getMSogei())
					.getTotalIn());
			mLocSogei.setTotalOut(((MeterfactLocation) this.getMSogei())
					.getTotalOut());

			mLoc600.setBet(this.getM600().getBet());
			mLoc600.setWin(this.getM600().getWin());
			mLoc600.setGamesPlayed(this.getM600().getGamesPlayed());
			mLoc600.setTotalIn(((MeterfactLocation) this.getM600())
					.getTotalIn());
			mLoc600.setTotalOut(((MeterfactLocation) this.getM600())
					.getTotalOut());

			clone.setMSogei(mLocSogei);
			clone.setM600(mLoc600);
		} else if (this.getMSogei() instanceof MeterfactVlt) {
			MeterfactVlt mVltSogei = new MeterfactVlt();
			MeterfactVlt mVlt600 = new MeterfactVlt();

			mVltSogei.setBet(this.getMSogei().getBet());
			mVltSogei.setWin(this.getMSogei().getWin());
			mVltSogei.setGamesPlayed(this.getMSogei().getGamesPlayed());
			mVltSogei
					.setTotalIn(((MeterfactVlt) this.getMSogei()).getTotalIn());
			mVltSogei.setTotalOut(((MeterfactVlt) this.getMSogei())
					.getTotalOut());

			mVlt600.setBet(this.getM600().getBet());
			mVlt600.setWin(this.getM600().getWin());
			mVlt600.setGamesPlayed(this.getM600().getGamesPlayed());
			mVlt600.setTotalIn(((MeterfactVlt) this.getM600()).getTotalIn());
			mVlt600.setTotalOut(((MeterfactVlt) this.getM600()).getTotalOut());

			clone.setMSogei(mVltSogei);
			clone.setM600(mVlt600);
		}

		clone.getMSogei().setTempodim(this.getTempodim());
		clone.getMSogei().setSistemagiocodim(this.getSistemagiocodim());
		clone.getMSogei().setSpaziodim(this.getSpaziodim());

		clone.getM600().setTempodim(this.getTempodim());
		clone.getM600().setSistemagiocodim(this.getSistemagiocodim());
		clone.getM600().setSpaziodim(this.getSpaziodim());

		return clone;
	}

	@Override
	public String toString() {
		return "Matcher [mSogei=" + mSogei + ", m600=" + m600 + "]";
	}

	public static ArrayList<String> headerToArrayList() {
		ArrayList<String> hList = new ArrayList<String>();

		hList.add("Data");
		hList.add("Game System");
		hList.add("Location Code");
		hList.add("Location Name");
		hList.add("Comune");
		hList.add("Provincia");
		hList.add("Vlt Code");
		hList.add("Bet Sogei");
		hList.add("Bet 600");
		hList.add("Win Sogei");
		hList.add("Win 600");
		hList.add("Games Played Sogei");
		hList.add("Games Played 600");
		hList.add("Total In Sogei");
		hList.add("Total In 600");
		hList.add("Total Out Sogei");
		hList.add("Total Out 600");

		return hList;

	}

}
