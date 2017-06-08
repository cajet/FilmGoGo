package com.filmgogo.Controller;

import java.awt.List;
import java.awt.RenderingHints.Key;
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
import java.util.ArrayList;
import java.util.Calendar;

import javax.security.auth.Subject;

import org.apache.commons.collections.functors.ForClosure;
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
		
		//initTable();
		
		/*------------------------获取正在上映的电影列表的API，插入电影数据-----------------------*/
		/*String url_movie= "http://m.maoyan.com/movie/list.json";
		String json_movie= loadJson(url_movie);
		JSONObject jsonObject= JSONObject.fromObject(json_movie);
		JSONArray array= jsonObject.getJSONObject("data").getJSONArray("movies");
		String insert_movie;
		JSONObject subObject;
		for (int i= 0; i< array.size(); i++) {
			subObject= array.getJSONObject(i); 
			insert_movie = "insert into movie(name, type, description, image, apiid, score, star) values('"+subObject.get("nm")+"','"+subObject.get("cat")+"','"+subObject.get("scm")+"','"+subObject.get("img")+"','"+subObject.get("id")+"','"+subObject.get("sc")+"','"+subObject.get("star")+"');";
			loadDatatoDB(insert_movie);
		}*/
		
		/*------------------------获取正在上映的电影简介的API，插入电影简介数据-----------------------*/
		/*String find_movies_id= "select * from movie";
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
	     }*/
		
		/*------------------------获取豆瓣老电影，插入电影简介数据-----------------------*/
		String url_douban_movie= "https://api.douban.com/v2/movie/top250";
		String json_douban_movie= loadJson(url_douban_movie);
		JSONObject jsonObject= JSONObject.fromObject(json_douban_movie);
		JSONArray array= jsonObject.getJSONArray("subjects");
		JSONObject object;
		String insert_votemovie;
		String movie_name, movie_image, movie_description;
		for (int i= 0; i< 20; i++) {
			object= array.getJSONObject(i);
			movie_name= object.getString("title");
			movie_image= object.getJSONObject("images").getString("medium");
			String id= object.getString("id");
			String url2="https://api.douban.com/v2/movie/subject/" +id;
			String json_douban_movie2= loadJson(url2);
			movie_description= JSONObject.fromObject(json_douban_movie2).getString("summary");
			insert_votemovie = "insert into votemovie(name, description, image, votes) values('"+movie_name+"','"+movie_description+"','"+movie_image+"','"+ 0 +"');";
			loadDatatoDB(insert_votemovie);
			//System.out.println(movie_name+'\n'+movie_description+'\n'+movie_image);
		}
		
		/*------------------------获取获取周边影院的API，插入影院数据-----------------------*/
		/*
		String url_cinema= "http://m.maoyan.com/cinemas.json";
		String json_cinema= loadJson(url_cinema);
		JSONObject jsonObject2= JSONObject.fromObject(json_cinema);
		JSONArray array2= jsonObject2.getJSONObject("data").getJSONArray("番禺区");
		String insert_cinema;
		JSONObject subObject2;
		for (int i= 0; i< array2.size(); i++) {
			subObject2= array2.getJSONObject(i); 
			insert_cinema = "insert into cinema(name, address, city, area, apiid) values('"+subObject2.get("nm")+"','"+subObject2.get("addr")+"','广州','"+subObject2.get("area")+"','"+subObject2.get("id")+"');";
			loadDatatoDB(insert_cinema);
		}
		*/
		
		/*-------------------------获取场次的API，插入showtime数据库-----------------------*/
		/*ArrayList<String> movies_array= getMoviesID();
		ArrayList<String> cinema_array= getCinemasID();
		for (int i= 0; i< movies_array.size(); i++) {
			for (int j= 0; j< cinema_array.size(); j++) {
				String url_showtime= "http://m.maoyan.com/showtime/wrap.json?cinemaid="+cinema_array.get(j)+"&movieid="+movies_array.get(i);
				System.out.println(url_showtime);
				String json_showtime= loadJson(url_showtime);
				JSONObject jsonObject3= JSONObject.fromObject(json_showtime);
				load_showtime_TodayandTomorrow(jsonObject3, movies_array.get(i), cinema_array.get(j));
			}
		}*/
		
		/*------------------------获取座位情况的API，插入seat数据库--------------------------*/
		/*ArrayList<String> showtimes_array= getShowtimeID();
		//获取今天和明天的日期
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DAY_OF_MONTH)+1;
		int day2= c.get(Calendar.DAY_OF_MONTH);
		String day_str= "-"+day;
		String day2_str= "-"+day2;
		String month_str= "-"+month;
		if (month< 10) month_str= "-0"+month;
		if (day< 10) day_str= "-0"+day;
		if (day2< 10) day2_str= "-0"+day2;
		String tomorrow= year+month_str+day_str;
		String today= year+month_str+day2_str;
		//载入今天的座位
		for (int i= 0; i< showtimes_array.size(); i++) {
			String url_seat_today= "http://m.maoyan.com/show/seats?showId="+showtimes_array.get(i)+"&showDate="+today;
			System.out.println(url_seat_today);
			String json_seat= loadJson(url_seat_today);
			JSONObject jsonObject4= JSONObject.fromObject(json_seat);
			load_seat_TodayandTomorrow(jsonObject4, showtimes_array.get(i));
		}
		//载入明天的座位
		for (int i= 0; i< showtimes_array.size(); i++) {
			String url_seat_tomorrow= "http://m.maoyan.com/show/seats?showId="+showtimes_array.get(i)+"&showDate="+tomorrow;
			System.out.println(url_seat_tomorrow);
			String json_seat= loadJson(url_seat_tomorrow);
			JSONObject jsonObject4= JSONObject.fromObject(json_seat);
			load_seat_TodayandTomorrow(jsonObject4, showtimes_array.get(i));
		}
		*/
	}
	
	public static void initTable() {  //载入数据前，要先清除所有数据，使数据保持最新，不重复
		String delete_movie_data= "delete from movie;";
		loadDatatoDB(delete_movie_data);
		
		/*String delete_cinema_data= "delete from cinema;";
		loadDatatoDB(delete_cinema_data);*/
		
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
	/*
	public static void load_showtime_TodayandTomorrow(JSONObject jsonObject3, String movie_id, String cinema_id) {
		//载入今天的场次数据
		if (jsonObject3.getJSONObject("data").getJSONObject("DateShow").isEmpty()) return;
		String today= jsonObject3.getJSONObject("data").getJSONObject("DateShow").toString().substring(2, 12);
		JSONArray array3= jsonObject3.getJSONObject("data").getJSONObject("DateShow").getJSONArray(today); 
		String insert_showtime, time;
		JSONObject subObject3;
		for (int i= 0; i< array3.size(); i++) {
			subObject3= array3.getJSONObject(i); 
			time= subObject3.get("tm")+" "+subObject3.get("showDate");
			insert_showtime = "insert into showtime(time, price, mid, cid) values('"+ time + "','"+ 40 +"','"+ movie_id +"','"+ cinema_id +"');";
			loadDatatoDB(insert_showtime);
			System.out.println(time);
		}
		//载入明天的场次数据
		String temp= jsonObject3.getJSONObject("data").getJSONObject("DateShow").toString().substring(11, 12);
		int a = Integer.parseInt(temp)+1;
		String tomorrow;
		if (a>= 10) tomorrow= jsonObject3.getJSONObject("data").getJSONObject("DateShow").toString().substring(2, 10) + a;
		else tomorrow= jsonObject3.getJSONObject("data").getJSONObject("DateShow").toString().substring(2, 10) + "0" +a;
		
		array3= jsonObject3.getJSONObject("data").getJSONObject("DateShow").getJSONArray(tomorrow); //载入明天的场次
		for (int i= 0; i< array3.size(); i++) {
			subObject3= array3.getJSONObject(i); 
			time= subObject3.get("tm")+" "+subObject3.get("showDate");
			insert_showtime = "insert into showtime(time, price, mid, cid) values('"+ time + "','"+ 43 +"','"+ movie_id +"','"+ cinema_id +"');";
			loadDatatoDB(insert_showtime);
			System.out.println(time);
		}
	}*/
	/*
	public static void load_seat_TodayandTomorrow(JSONObject jsonObject4, String showtime_id) {
		if (jsonObject4.getJSONArray("sections").isEmpty()) return;
		JSONObject TEMP= jsonObject4.getJSONArray("sections").getJSONObject(0);
		JSONArray array = TEMP.getJSONArray("seatRows");
		JSONArray sub_array;
		JSONObject object, sub_object;
		String insert_seat;
		for (int i= 0; i<array.size(); i++) {
			object= array.getJSONObject(i);
			sub_array= object.getJSONArray("seats");
			for (int j= 0; j< sub_array.size(); j++) {
				sub_object= sub_array.getJSONObject(j);
				insert_seat= "insert into seat(state, stid, row, column) values('"+ sub_object.get("type") + "','"+ showtime_id + "','"+ sub_object.get("rowNum") + "','"+ sub_object.get("columnNum") +"');"; 
				loadDatatoDB(insert_seat);
				//System.out.println(sub_object.get("type")+ " "+ showtime_id + " "+sub_object.get("rowNum") + " "+sub_object.get("columnNum"));
			}
		}
	}
	*/
	/*public static ArrayList<String> getMoviesID() {
		String find_movies_id= "select * from movie";
		ArrayList<String> arrayList = new ArrayList<String>();
		try {
			con = DriverManager.getConnection(dburl, user, pass);  
			con.setAutoCommit(false);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(find_movies_id);
			while (rs.next()) {
				arrayList.add(rs.getString(5));
			}
	     } catch (Exception e) {  
			  System.out.println(e);  
	     }
		return arrayList;
	}
	
	public static ArrayList<String> getCinemasID() {
		String find_cinemas_id= "select * from cinema limit 2";  //因为API访问次数有限，所以只插入两个影院的场次数据
		ArrayList<String> arrayList= new ArrayList<String>();
		try {
			con = DriverManager.getConnection(dburl, user, pass);  
			con.setAutoCommit(false);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(find_cinemas_id);
			while (rs.next()) {
				arrayList.add(rs.getString(6));
			}
		} catch (Exception e) {  
			  System.out.println(e);  
	    }
		return arrayList;
	}*/
	/*
	public static ArrayList<String> getShowtimeID() {
		String find_showtimes_id= "select * from showtime limit 10";  //因为API访问次数有限，所以只插入10个场次的座位数据
		ArrayList<String> arrayList= new ArrayList<String>();
		try {
			con = DriverManager.getConnection(dburl, user, pass);  
			con.setAutoCommit(false);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(find_showtimes_id);
			while (rs.next()) {
				arrayList.add(rs.getString(6));
			}
		} catch (Exception e) {  
			  System.out.println(e);  
	    }
		return arrayList;
	}*/
}


