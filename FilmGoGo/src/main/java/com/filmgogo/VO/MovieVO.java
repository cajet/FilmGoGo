package com.filmgogo.VO;

public class MovieVO {
	
	private int id;
	
	private String type;
	
	private String name;
	
	private String description;
	
    private String image;
    
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getImg()
	{
		return image;
	}
	public void setImg(String image)
	{
		this.image = image;
	}
	
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
}
