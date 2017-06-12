package com.filmgogo.VO;

import java.sql.Timestamp;

import javax.swing.text.FlowView.FlowStrategy;

public class ReservationVO {
	
	private int id;
	
	private int seatid;
	
	private int oldseatid;
	
	private String movie_name;
	
	private String cinema_name;
	
	private Timestamp show_time;
	
	private int seat_row;
	
	private int seat_column;
	
	private float ticket_price;
	
	private String old_movie_name;
	
	private String old_time;
	
	private int old_seat_row;
	
	private int old_seat_column;
	
	private String old_price;
	
	private boolean pay;
	
	public int getId() {
		return id;
	}
	
	public String getMovieName() {
		return movie_name;
	}
	
	public String getCinemaName() {
		return cinema_name;
	}
	
	public Timestamp getShowTime() {
		return show_time;
	}
	
	public int getSeatRow() 
	{
		return seat_row;
	}
	
	public int getSeatColumn() 
	{
		return seat_column;
	}
	
	public float getTicketPrice() {
		return ticket_price;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setMovieName(String movie_name) {
		this.movie_name= movie_name;
	}
	
	public void setCinemaName(String cinema_name) {
		this.cinema_name= cinema_name;
	}
	
	public void setShowTime(Timestamp show_time) {
		this.show_time= show_time;
	}
	
	public void setSeatRow(int seat_row)
	{
		this.seat_row= seat_row;
	}
	
	public void setSeatColumn(int seat_column)
	{
		this.seat_column= seat_column;
	}
	
	public void setTicketPrice(float ticket_price) {
		this.ticket_price= ticket_price;
	}
	
	public String getOldmovieName() {
		return old_movie_name;
	}
	public void setOldmovieName(String oldmovieName) {
		this.old_movie_name= oldmovieName;
	}
	public String getOldtime() {
		return old_time;
	}
	public void setOldtime(String oldtime) {
		this.old_time= oldtime;
	}
	public int getOldseatRow() {
		return old_seat_row;
	}
	public void setOldseatRow(int oldseatRow) {
		this.old_seat_row= oldseatRow;
	}
	public int getOldseatCol() {
		return old_seat_column;
	}
	public void setOldseatCol(int oldseatCol) {
		this.old_seat_column= oldseatCol;
	}
	public String getOldPrice() {
		return old_price;
	}
	public void setOldPrice(String oldPrice) {
		this.old_price= oldPrice;
		
	}
	public boolean getPay() {
		return pay;
	}
	public void setPay(boolean pay) {
		this.pay= pay;
	}
	public int getSeatid() {
		return seatid;
	}
	public void setSeatid(int seatid) {
		this.seatid= seatid;
	}
	public int getOldSeatid() {
		return oldseatid;
	}
	public void setOldSeatid(int oldseatid) {
		this.oldseatid= oldseatid;
	}
}
