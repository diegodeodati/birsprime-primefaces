package it.betplus.birs.primitive;

// Generated 31-mar-2011 10.51.22 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * SessionLogId generated by hbm2java
 */
public class SessionLogId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long uniqueSession;
	private Date operationDate;
	private Date referenceDate;
	private String esito;
	private long aamsGamesystemId;

	public SessionLogId() {
	}

	public SessionLogId(long uniqueSession, Date operationDate,
			Date referenceDate, long aamsGamesystemId) {
		this.uniqueSession = uniqueSession;
		this.operationDate = operationDate;
		this.referenceDate = referenceDate;
		this.aamsGamesystemId = aamsGamesystemId;
	}

	public SessionLogId(long uniqueSession, Date operationDate,
			Date referenceDate, String esito, long aamsGamesystemId) {
		this.uniqueSession = uniqueSession;
		this.operationDate = operationDate;
		this.referenceDate = referenceDate;
		this.esito = esito;
		this.aamsGamesystemId = aamsGamesystemId;
	}

	public long getUniqueSession() {
		return this.uniqueSession;
	}

	public void setUniqueSession(long uniqueSession) {
		this.uniqueSession = uniqueSession;
	}

	public Date getOperationDate() {
		return this.operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public Date getReferenceDate() {
		return this.referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	public String getEsito() {
		return this.esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public long getAamsGamesystemId() {
		return this.aamsGamesystemId;
	}

	public void setAamsGamesystemId(long aamsGamesystemId) {
		this.aamsGamesystemId = aamsGamesystemId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SessionLogId))
			return false;
		SessionLogId castOther = (SessionLogId) other;

		return (this.getUniqueSession() == castOther.getUniqueSession())
				&& ((this.getOperationDate() == castOther.getOperationDate()) || (this
						.getOperationDate() != null
						&& castOther.getOperationDate() != null && this
						.getOperationDate()
						.equals(castOther.getOperationDate())))
				&& ((this.getReferenceDate() == castOther.getReferenceDate()) || (this
						.getReferenceDate() != null
						&& castOther.getReferenceDate() != null && this
						.getReferenceDate()
						.equals(castOther.getReferenceDate())))
				&& ((this.getEsito() == castOther.getEsito()) || (this
						.getEsito() != null && castOther.getEsito() != null && this
						.getEsito().equals(castOther.getEsito())))
				&& (this.getAamsGamesystemId() == castOther
						.getAamsGamesystemId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getUniqueSession();
		result = 37
				* result
				+ (getOperationDate() == null ? 0 : this.getOperationDate()
						.hashCode());
		result = 37
				* result
				+ (getReferenceDate() == null ? 0 : this.getReferenceDate()
						.hashCode());
		result = 37 * result
				+ (getEsito() == null ? 0 : this.getEsito().hashCode());
		result = 37 * result + (int) this.getAamsGamesystemId();
		return result;
	}

}
