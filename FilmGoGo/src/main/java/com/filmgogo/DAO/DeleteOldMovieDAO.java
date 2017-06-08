package com.filmgogo.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.filmgogo.VO.MovieVO;
import com.filmgogo.VO.ShowtimeVO;

@Repository
public class DeleteOldMovieDAO {
	@Autowired
    private JdbcTemplate jdb;
	
	public void deleteMovie(String movie_name) {
		String getmovieId_sql= "select id from oldmovie where name= ?;";
		Object[] para = new Object[]{movie_name};
		List<MovieVO> lm = jdb.query(getmovieId_sql, para, new RowMapper<MovieVO>(){

			public MovieVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				MovieVO m = new MovieVO();
				m.setId(res.getInt("id"));
				return m;
			}
			
		});
		if (!lm.isEmpty()) {
			int movie_id= lm.get(0).getId();
			String oldmovie_sql = "delete from oldmovie where name= ?;";
	        jdb.update(oldmovie_sql, new Object[]{movie_name});
	        
	        String getshowtimeId_sql= "select id from oldshowtime where mid= ?;";
	        Object[] para2 = new Object[]{movie_id};
			List<ShowtimeVO> ls = jdb.query(getshowtimeId_sql, para2, new RowMapper<ShowtimeVO>(){

				public ShowtimeVO mapRow(ResultSet res, int arg1) throws SQLException
				{
					ShowtimeVO m = new ShowtimeVO();
					m.setId(res.getInt("id"));
					return m;
				}
				
			});
			if (!ls.isEmpty()) {
				String oldshowtime_sql = "delete from oldshowtime where mid= ?;";
				jdb.update(oldshowtime_sql, new Object[]{movie_id});
				
				int showtime_id;
				for (int i= 0; i< ls.size(); i++) {
					showtime_id= ls.get(i).getId();
					String deleteSeat_sql= "delete from oldseat where sid= ?;";
					jdb.update(deleteSeat_sql, new Object[]{showtime_id});
				}
			}
		}
	}
	
	
	public void deleteOneShowtime(String movie_name, String showtime) {
		String getmovieId_sql= "select id from oldmovie where name= ?;";
		Object[] para = new Object[]{movie_name};
		List<MovieVO> lm = jdb.query(getmovieId_sql, para, new RowMapper<MovieVO>(){

			public MovieVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				MovieVO m = new MovieVO();
				m.setId(res.getInt("id"));
				return m;
			}
			
		});
		if (!lm.isEmpty()) {
			int movie_id= lm.get(0).getId();
			String getshowtimeId_sql= "select id from oldshowtime where mid= ? and time= ?;";
	        Object[] para2 = new Object[]{movie_id, showtime};
	        List<ShowtimeVO> ls = jdb.query(getshowtimeId_sql, para2, new RowMapper<ShowtimeVO>(){

				public ShowtimeVO mapRow(ResultSet res, int arg1) throws SQLException
				{
					ShowtimeVO m = new ShowtimeVO();
					m.setId(res.getInt("id"));
					return m;
				}
				
			});
	        if (!ls.isEmpty()) {
	        	String deleteShowtime_sql= "delete from oldshowtime where mid= ? and time= ?;";
	        	jdb.update(deleteShowtime_sql, new Object[]{movie_id, showtime});
	        	
	        	int showtime_id= ls.get(0).getId();
	        	String deleteSeat_sql= "delete from oldseat where sid= ?;";
				jdb.update(deleteSeat_sql, new Object[]{showtime_id});
	        }
		}
	}
}
