package it.betplus.birs.primitive;

// Generated 31-mar-2011 10.51.22 by Hibernate Tools 3.4.0.CR1

/**
 * Meterfact generated by hbm2java
 */
public class MeterfactSysGame extends Meterfact implements
		java.io.Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8411728184409046535L;
	private Double totalIn;
	private Double totalOut;
	private Double ticketIn;
	private Double ticketOut;
	private Double coinIn;
	private Double billIn;
	private Double cardIn;
	private Double prepaidIn;
	private int numVlt;

	public MeterfactSysGame() {
		super();
	}

	public Double getTotalIn() {
		return this.totalIn.doubleValue();
	}

	public void setTotalIn(Double totalIn) {
		this.totalIn = totalIn;
	}

	public Double getTotalOut() {
		return this.totalOut.doubleValue();
	}

	public void setTotalOut(Double totalOut) {
		this.totalOut = totalOut;
	}

	public Double getTicketIn() {
		return this.ticketIn.doubleValue();
	}

	public void setTicketIn(Double ticketIn) {
		this.ticketIn = ticketIn;
	}

	public Double getTicketOut() {
		return this.ticketOut.doubleValue();
	}

	public void setTicketOut(Double ticketOut) {
		this.ticketOut = ticketOut;
	}

	public Double getCoinIn() {
		return this.coinIn.doubleValue();
	}

	public void setCoinIn(Double coinIn) {
		this.coinIn = coinIn;
	}

	public Double getBillIn() {
		return this.billIn.doubleValue();
	}

	public void setBillIn(Double billIn) {
		this.billIn = billIn;
	}

	public Double getCardIn() {
		return this.cardIn.doubleValue();
	}

	public void setCardIn(Double cardIn) {
		this.cardIn = cardIn;
	}

	public Double getPrepaidIn() {
		return this.prepaidIn.doubleValue();
	}

	public void setPrepaidIn(Double prepaidIn) {
		this.prepaidIn = prepaidIn;
	}

	public void setNumVlt(int n) {
		numVlt = n;
	}

	public int getNumVlt() {
		return numVlt;
	}

	public void sumValue(MeterfactSysGame add) {

		this.setBet(add.getBet() + this.getBet());
		this.setWin(add.getWin() + this.getWin());
		this.setGamesPlayed(add.getGamesPlayed() + this.getGamesPlayed());
		this.setJackpotWins(add.getJackpotWins() + this.getJackpotWins());
		this.setJackpotContribution(add.getJackpotContribution()
				+ this.getJackpotContribution());
		this.setPreu(add.getPreu() + this.getPreu());
		this.setAams(add.getAams() + this.getAams());
		this.setNetWin(add.getNetWin() + this.getNetWin());
		this.setHouseWin(add.getHouseWin() + this.getHouseWin());
		this.setSupplierProfit(add.getSupplierProfit()
				+ this.getSupplierProfit());
		this.setOperatorsProfit(add.getOperatorsProfit()
				+ this.getOperatorsProfit());
		this.setBplusNetProfit(add.getBplusNetProfit()
				+ this.getBplusNetProfit());

		this.setTotalIn(add.getTotalIn() + this.getTotalIn());
		this.setTotalOut(add.getTotalOut() + this.getTotalOut());
		this.setTicketIn(add.getTicketIn() + this.getTicketIn());
		this.setTicketOut(add.getTicketOut() + this.getTicketOut());
		this.setCoinIn(add.getCoinIn() + this.getCoinIn());
		this.setBillIn(add.getBillIn() + this.getBillIn());
		this.setCardIn(add.getCardIn() + this.getCardIn());
		this.setPrepaidIn(add.getPrepaidIn() + this.getPrepaidIn());
		this.setNumVlt(add.getNumVlt() + this.getNumVlt());

	}

	public MeterfactSysGame clone() {

		MeterfactSysGame clone = new MeterfactSysGame();

		clone.setTempodim(this.getTempodim());
		clone.setSistemagiocodim(this.getSistemagiocodim());
		clone.setClientidim(this.getClientidim());
		clone.setSpaziodim(this.getSpaziodim());

		clone.setBet(this.getBet());
		clone.setWin(this.getWin());
		clone.setGamesPlayed(this.getGamesPlayed());
		clone.setJackpotWins(this.getJackpotWins());
		clone.setJackpotContribution(this.getJackpotContribution());
		clone.setPreu(this.getPreu());
		clone.setAams(this.getAams());
		clone.setNetWin(this.getNetWin());
		clone.setHouseWin(this.getHouseWin());
		clone.setSupplierProfit(this.getSupplierProfit());
		clone.setOperatorsProfit(this.getOperatorsProfit());
		clone.setBplusNetProfit(this.getBplusNetProfit());

		clone.setAamsJackpotId(this.getAamsJackpotId());

		clone.setTotalIn(this.getTotalIn());
		clone.setTotalOut(this.getTotalOut());
		clone.setTicketIn(this.getTicketIn());
		clone.setTicketOut(this.getTicketOut());
		clone.setCoinIn(this.getCoinIn());
		clone.setBillIn(this.getBillIn());
		clone.setCardIn(this.getCardIn());
		clone.setPrepaidIn(this.getPrepaidIn());
		clone.setNumVlt(this.getNumVlt());

		return clone;
	}

}
