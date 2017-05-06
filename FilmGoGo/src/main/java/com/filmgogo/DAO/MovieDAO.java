package com.filmgogo.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.filmgogo.VO.MovieVO;

import net.sf.json.JSONArray;

@Repository
public class MovieDAO {
	@Autowired
    private JdbcTemplate jdb;
	/*
	public String showMovies()
	{
		String sql = "select * from movie limit 10";
		List<MovieVO> lm = jdb.query(sql, new RowMapper<MovieVO>() {
			public MovieVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				MovieVO m = new MovieVO();
				m.setId(res.getInt("id"));
				m.setImg(res.getString("image"));
				m.setType(res.getString("type"));
				m.setDescription(res.getString("description"));
				m.setName(res.getString("name"));
				return m;
			}
			
		});
		return JSONArray.fromObject(lm).toString();
	}*/
	
	/*-----------------通过cinema id获得对应影院上映的所有电影数据，这里因为设计成所有影院都有所有电影，所以其实cinema id并没有实际用处-----------------*/
	public String getMoviesViaCid(int cid)
	{
		String sql = "select distinct movie.id, movie.name, movie.type, movie.description, movie.image from movie, cinema " + "where cinema.id = ?;";
		Object[] para = new Object[]{cid};
		List<MovieVO> lm = jdb.query(sql, para, new RowMapper<MovieVO>(){

			public MovieVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				MovieVO m = new MovieVO();
				m.setId(res.getInt("id"));
				m.setName(res.getString("name"));
				m.setType(res.getString("type"));
				m.setDescription(res.getString("description"));
				m.setImg(res.getString("image"));
				return m;
			}
			
		});
		return JSONArray.fromObject(lm).toString();
	}
	
}
