package com.filmgogo.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.filmgogo.VO.CinemaVO;

import net.sf.json.JSONArray;

@Repository
public class CinemaDAO {
	@Autowired
	private JdbcTemplate jdb;
	/*
	public String showCinemas()
	{
		String sql = "select * from cinema limit 10";
		List<CinemaVO> lc = jdb.query(sql, new RowMapper<CinemaVO>() {

			public CinemaVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				CinemaVO c = new CinemaVO();
				c.setId(res.getInt("id"));
				c.setName(res.getString("name"));
				c.setAddress(res.getString("address"));
				c.setCity(res.getString("city"));
				c.setArea(res.getString("area"));
				return c;
			}
			
		});
		return JSONArray.fromObject(lc).toString();
	}
	*/
	
	/*-----------------通过movie id获得有上映该电影的所有影院的数据，这里因为设计成所有影院都有所有电影，所以其实movie id并没有实际用处-----------------*/
	public String getCinemasViaMid(int mid)
	{
		String sql = "select distinct cinema.id, cinema.name, cinema.address, cinema.city, cinema.area from movie, cinema " + "where movie.id = ?;";
		Object[] para = new Object[]{mid};
		List<CinemaVO> lc = jdb.query(sql, para, new RowMapper<CinemaVO>() {

			public CinemaVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				CinemaVO c = new CinemaVO();
				c.setAddress(res.getString("address"));
				c.setCity(res.getString("city"));
				c.setArea(res.getString("area"));
				c.setId(res.getInt("id"));
				c.setName(res.getString("name"));
				return c;
			}
			
		});
		return JSONArray.fromObject(lc).toString();
	}
}
