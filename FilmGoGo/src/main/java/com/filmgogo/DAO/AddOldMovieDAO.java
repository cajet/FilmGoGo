package com.filmgogo.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.w3c.dom.ls.LSException;

import com.filmgogo.VO.MovieVO;
import com.filmgogo.VO.SeatVO;
import com.filmgogo.VO.ShowtimeVO;

@Repository
public class AddOldMovieDAO {
	@Autowired
    private JdbcTemplate jdb;
	
	public void addOldMovie(String name, String description, String image, String time, String price)
	{
		int movie_id;
        String getmovieId_sql= "select id from oldmovie where name= ?;";
        Object[] para = new Object[]{name};
		List<MovieVO> lm = jdb.query(getmovieId_sql, para, new RowMapper<MovieVO>(){

			public MovieVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				MovieVO m = new MovieVO();
				m.setId(res.getInt("id"));
				return m;
			}
			
		});
		if (lm.isEmpty()) {
			String oldmovie_sql = "insert into oldmovie(name, description, image) values(?, ?, ?);";
	        jdb.update(oldmovie_sql, new Object[]{name, description, image});
	        lm = jdb.query(getmovieId_sql, para, new RowMapper<MovieVO>(){
				public MovieVO mapRow(ResultSet res, int arg1) throws SQLException
				{
					MovieVO m = new MovieVO();
					m.setId(res.getInt("id"));
					return m;
				}
			});   
		}
		movie_id= lm.get(0).getId();
		//System.out.println(movie_id);
		
		int showtime_id;
		String getShowTimeId_sql= "select id from oldshowtime where mid= ? and time= ?;";
		Object[] para2 = new Object[]{movie_id, time};
		List<ShowtimeVO> ls = jdb.query(getShowTimeId_sql, para2, new RowMapper<ShowtimeVO>(){

			public ShowtimeVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				ShowtimeVO m = new ShowtimeVO();
				m.setId(res.getInt("id"));
				return m;
			}
			
		});
		if (ls.isEmpty()) {
			String insert_showtime_sql= "insert into oldshowtime(mid, time, price) values(?, ?, ?);";
			jdb.update(insert_showtime_sql, new Object[]{movie_id, time, price});
			ls = jdb.query(getShowTimeId_sql, para2, new RowMapper<ShowtimeVO>(){
				public ShowtimeVO mapRow(ResultSet res, int arg1) throws SQLException
				{
					ShowtimeVO m = new ShowtimeVO();
					m.setId(res.getInt("id"));
					return m;
				}
			});
		}
		showtime_id= ls.get(0).getId();
		//System.out.println(showtime_id);
		
		String getSeatId_sql= "select id from oldseat where sid= ?;";
		Object[] para3 = new Object[]{showtime_id};
		List<SeatVO> lseat = jdb.query(getSeatId_sql, para3, new RowMapper<SeatVO>(){

			public SeatVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				SeatVO m = new SeatVO();
				m.setId(res.getInt("id"));
				return m;
			}
			
		});
		if (lseat.isEmpty()) {
			for (int i= 1; i<= 10; i++) {
				for (int j= 1; j<= 10; j++) {
					String insert_seat_sql= "insert into oldseat(sid, state, row, col) values("+showtime_id+",'valid',"+i+","+j+")";
					jdb.update(insert_seat_sql);
				}
			}
							
		}
		
	}
}
