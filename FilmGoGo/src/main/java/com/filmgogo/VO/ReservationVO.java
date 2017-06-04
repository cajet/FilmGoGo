package com.filmgogo.VO;

import java.sql.Timestamp;

import javax.swing.text.FlowView.FlowStrategy;

public class ReservationVO {
	
	private int id;
	
	private String movie_name;
	
	private String cinema_name;
	
	private Timestamp show_time;
	
	private int seat_row;
	
	private int seat_column;
	
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
}
