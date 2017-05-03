package com.filmgogo.Controller;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;

import com.mysql.fabric.xmlrpc.base.Array;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class ReaddataFromAPI {
	
	private static String dburl = "jdbc:mysql://localhost:3306/filmgogo?useUnicode=true&characterEncoding=utf-8&useSSL=false&autoReconnect=true";  
	private static String user = "root";  
	private static String pass = "12226655";
	private static Connection con;
	private static Statement stmt;
	
	public static void main(String[] args) {
		
		initTable();
		
		/*------------------------获取正在上映的电影列表的API，插入电影数据-----------------------*/
		String url_movie= "http://m.maoyan.com/movie/list.json";
		String json_movie= loadJson(url_movie);
		JSONObject jsonObject= JSONObject.fromObject(json_movie);
		JSONArray array= jsonObject.getJSONObject("data").getJSONArray("movies");
		String insert_movie;
		JSONObject subObject;
		for (int i= 0; i< array.size(); i++) {
			subObject= array.getJSONObject(i); 
			insert_movie = "insert into movie(name, type, description, image, apiid) values('"+subObject.get("nm")+"','"+subObject.get("cat")+"','"+subObject.get("scm")+"','"+subObject.get("img")+"','"+subObject.get("id")+"');";
			loadDatatoDB(insert_movie);
		}
		
		/*------------------------获取正在上映的电影简介的API，插入电影简介数据-----------------------*/
		String find_movies_id= "select * from movie";
		try {
			con = DriverManager.getConnection(dburl, user, pass);  
			con.setAutoCommit(false);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(find_movies_id);
			while (rs.next()) {
				String url_description= "http://m.maoyan.com/movie/"+rs.getString(5)+".json";
				String json_description= loadJson(url_description);
				jsonObject= JSONObject.fromObject(json_description);
				String temp= (String) jsonObject.getJSONObject("data").getJSONObject("MovieDetailModel").get("dra");
				//System.out.println(temp);
				String insert_description = "update movie set description = ? where apiid= ?";
				PreparedStatement pst = con.prepareStatement(insert_description);
		        pst.setString(1, temp);
		        pst.setString(2, rs.getString(5));
		        pst.executeUpdate();
		        con.commit();
			}
	     } catch (Exception e) {  
			  System.out.println(e);  
	     } 
		
		
		/*------------------------获取获取周边影院的API，插入影院数据-----------------------*/
		
		String url_cinema= "http://m.maoyan.com/cinemas.json";
		String json_cinema= loadJson(url_cinema);
		JSONObject jsonObject2= JSONObject.fromObject(json_cinema);
		JSONArray array2= jsonObject2.getJSONObject("data").getJSONArray("番禺区");
		String insert_cinema;
		JSONObject subObject2;
		for (int i= 0; i< array2.size(); i++) {
			subObject2= array2.getJSONObject(i); 
			insert_cinema = "insert into cinema(name, address, city, area) values('"+subObject2.get("nm")+"','"+subObject2.get("addr")+"','广州','"+subObject2.get("area")+"');";
			loadDatatoDB(insert_cinema);
		}

	}
	
	public static void initTable() {  //载入数据前，要先清除所有数据，使数据保持最新，不重复
		String delete_movie_data= "delete from movie;";
		loadDatatoDB(delete_movie_data);
		
		String delete_cinema_data= "delete from cinema;";
		loadDatatoDB(delete_cinema_data);
		
	}
	
	public static void loadDatatoDB(String sql) {  //数据载入数据库
		try {
			  con = DriverManager.getConnection(dburl, user, pass);  
			  con.setAutoCommit(false);
			  stmt = con.createStatement();
			  stmt.executeUpdate(sql); 
			  con.commit();  
	     } catch (Exception e) {  
			  System.out.println(e);  
	     } 
	}
	
	public static String loadJson(String url) {  //从API上载入数据到json对象
	    StringBuilder json = new StringBuilder();                  
	    try {  
	        URL urlObject = new URL(url);  
	        URLConnection uc = urlObject.openConnection();  
	        uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
	        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"utf-8"));  
	        String inputLine = null;  
	        while ( (inputLine = in.readLine()) != null) {  
	            json.append(inputLine);  
	        }  
	        in.close();  
	    } catch (MalformedURLException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	    return json.toString();  
	} 
	
}


