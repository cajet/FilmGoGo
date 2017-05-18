package com.filmgogo.VO;

import java.sql.Timestamp;

import javax.swing.text.FlowView.FlowStrategy;

public class ReservationVO {
	
	private int id;
	
	private String movie_name;
	
	private String cinema_name;
	
	private Timestamp show_time;
	
	private String seat_name;
	
	private float ticket_price;
	
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
	
	public String getSeatName() {
		return seat_name;
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
	
	public void setSeatName(String seat_name) {
		this.seat_name= seat_name;
	}
	
	public void setTicketPrice(float ticket_price) {
		this.ticket_price= ticket_price;
	}
}
