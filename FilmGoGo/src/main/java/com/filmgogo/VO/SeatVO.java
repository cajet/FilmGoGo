package com.filmgogo.VO;

public class SeatVO {
	
	private int id;
	
    private String state;
    
    private int row;
    
    private int column;
    
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state = state;
	}
	public int getRow() 
	{
		return row;
	}
	public void setRow(int row) 
	{
		this.row= row;
	}
	public int getColumn() 
	{
		return column;
	}
	public void setColumn(int column) 
	{
		this.column= column;
	}
}
