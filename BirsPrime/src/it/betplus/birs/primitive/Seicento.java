package it.betplus.birs.primitive;

public class Seicento {

	
	long id;
	String aams_vlt_code;
	long gs_id;
	long bet;
	long win;
	long tot_paid;
	long tot_in;
	long tot_out;
	long bet_num;
	long tot_bet;
	long tot_win;
	long tot_tot_paid;
	long tot_tot_in;
	long tot_tot_out;
	long tot_bet_num;
	java.sql.Date date;
	int year=0;
	int month=0;
	int day=0;
	long msg_id;
	
	public Seicento(){}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getAams_vlt_code() {
		return aams_vlt_code;
	}

	public void setAams_vlt_code(String aams_vlt_code) {
		this.aams_vlt_code = aams_vlt_code;
	}

	public long getGs_id() {
		return gs_id;
	}
	public void setGs_id(long gs_id) {
		this.gs_id = gs_id;
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
	public long getTot_paid() {
		return tot_paid;
	}
	public void setTot_paid(long tot_paid) {
		this.tot_paid = tot_paid;
	}
	public long getTot_in() {
		return tot_in;
	}
	public void setTot_in(long tot_in) {
		this.tot_in = tot_in;
	}
	public long getTot_out() {
		return tot_out;
	}
	public void setTot_out(long tot_out) {
		this.tot_out = tot_out;
	}
	public long getBet_num() {
		return bet_num;
	}
	public void setBet_num(long bet_num) {
		this.bet_num = bet_num;
	}
	public long getTot_bet() {
		return tot_bet;
	}
	public void setTot_bet(long tot_bet) {
		this.tot_bet = tot_bet;
	}
	public long getTot_win() {
		return tot_win;
	}
	public void setTot_win(long tot_win) {
		this.tot_win = tot_win;
	}
	public long getTot_tot_paid() {
		return tot_tot_paid;
	}
	public void setTot_tot_paid(long tot_tot_paid) {
		this.tot_tot_paid = tot_tot_paid;
	}
	public long getTot_tot_in() {
		return tot_tot_in;
	}
	public void setTot_tot_in(long tot_tot_in) {
		this.tot_tot_in = tot_tot_in;
	}
	public long getTot_tot_out() {
		return tot_tot_out;
	}
	public void setTot_tot_out(long tot_tot_out) {
		this.tot_tot_out = tot_tot_out;
	}
	public long getTot_bet_num() {
		return tot_bet_num;
	}
	public void setTot_bet_num(long tot_tot_bet_num) {
		this.tot_bet_num = tot_tot_bet_num;
	}
	public java.sql.Date getDate() {
		return date;
	}
	public void setDate(java.sql.Date date) {
		this.date = date;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public long getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(long msg_id) {
		this.msg_id = msg_id;
	}
	
	@Override
	public String toString() {
		return "seicento [id=" + id + ", aams_vlt_code=" + aams_vlt_code + ", gs_id="
				+ gs_id + ", bet=" + bet + ", win=" + win + ", tot_paid="
				+ tot_paid + ", tot_in=" + tot_in + ", tot_out=" + tot_out
				+ ", bet_num=" + bet_num + ", tot_bet=" + tot_bet
				+ ", tot_win=" + tot_win + ", tot_tot_paid=" + tot_tot_paid
				+ ", tot_tot_in=" + tot_tot_in + ", tot_tot_out=" + tot_tot_out
				+ ", tot_bet_num=" + tot_bet_num + ", date=" + date
				+ ", year=" + year + ", month=" + month + ", day=" + day
				+ ", msg_id=" + msg_id + "]";
	}
	
}
