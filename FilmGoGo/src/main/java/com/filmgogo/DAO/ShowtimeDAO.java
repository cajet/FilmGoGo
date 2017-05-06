package com.filmgogo.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.filmgogo.VO.ShowtimeVO;

import net.sf.json.JSONArray;

@Repository
public class ShowtimeDAO {
	@Autowired
	private JdbcTemplate jdb;
	
    public String getShowtimes(int cid, int mid)
    {
    	String sql = "select showtime.id, showtime.time, showtime.price from showtime "
    			+ "where showtime.cid = ? and showtime.mid = ?;";
    	Object[] para = new Object[]{cid, mid};
    	List<ShowtimeVO> ls = jdb.query(sql, para, new RowMapper<ShowtimeVO>(){

			public ShowtimeVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				ShowtimeVO s = new ShowtimeVO();
				s.setId(res.getInt("id"));
				s.setTime(res.getTimestamp("time"));
				s.setPrice(res.getFloat("price"));
				return s;
			}
    		
    	});
    	return JSONArray.fromObject(ls).toString();
    }
}
