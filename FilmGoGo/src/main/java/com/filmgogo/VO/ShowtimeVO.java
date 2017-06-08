package com.filmgogo.VO;

import java.sql.Timestamp;

public class ShowtimeVO {
	
	private int id;
	
    private Timestamp time;
    
    private String oldtime;
    
    private float price;
    
    private String oldprice;
    
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public Timestamp getTime()
	{
		return time;
	}
	public void setTime(Timestamp time)
	{
		this.time = time;
	}
	public float getPrice()
	{
		return price;
	}
	public void setPrice(float price)
	{
		this.price = price;
	}
	public String getOldprice() 
	{
		return oldprice;
	}
	public void setOldPrice(String oldprice) 
	{
		this.oldprice= oldprice;
	}
	public String getOldTime() 
	{
		return oldtime;
	}
	public void setOldTime(String oldtime) 
	{
		this.oldtime= oldtime;
	}
}
