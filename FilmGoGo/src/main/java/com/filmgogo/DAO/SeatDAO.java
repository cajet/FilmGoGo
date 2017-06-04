package com.filmgogo.DAO;

import java.security.interfaces.RSAKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.filmgogo.VO.SeatVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository
public class SeatDAO {
	@Autowired
	private JdbcTemplate jdb;
	
	public String getAllseats(int showtimeid)
    {
    	String sql = "select seat.id, seat.state, seat.row, seat.column from seat " + "where seat.stid = ?;";
    	Object[] para = new Object[]{showtimeid};
    	List<SeatVO> ls = jdb.query(sql, para, new RowMapper<SeatVO>(){

			public SeatVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				SeatVO s = new SeatVO();
				s.setId(res.getInt("id"));
				s.setState(res.getString("state"));
				s.setRow(res.getInt("row"));
				s.setColumn(res.getInt("column"));
				return s;
			}
    		
    	});
    	return JSONArray.fromObject(ls).toString();
    }
	/*
	public void setState(int seatid)
	{
		String query_sql= "select seat.state from seat "+"where seat.id= ?;";
		Object[] para = new Object[]{seatid};
		List<SeatVO> ls = jdb.query(query_sql, para, new RowMapper<SeatVO>(){
			public SeatVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				SeatVO s = new SeatVO();
				s.setState(res.getString("state"));
				return s;
			}
    	});
		String seat_state = ls.get(0).getState();
		if (seat_state.equals("valid"))   //如果原来是valid状态则改为reserved, 否则改为valid
		{
			String sql= "update seat set state= ?" +"where id= ?;";
			jdb.update(sql, new Object[]{"reserved", seatid});
		}
		else 
		{
			String sql= "update seat set state= ?" +"where id= ?;";
			jdb.update(sql, new Object[]{"valid", seatid});
		}
		
	}
	

	public String getValidseats (int showtimeid) {
		String sql = "select seat.id, seat.state from seat " + "where seat.stid = ? and seat.state = ?;";
    	Object[] para = new Object[]{showtimeid, "valid"};
    	List<SeatVO> ls = jdb.query(sql, para, new RowMapper<SeatVO>(){

			public SeatVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				SeatVO s = new SeatVO();
				s.setId(res.getInt("id"));
				s.setState(res.getString("state"));
				return s;
			}
    		
    	});
    	return JSONArray.fromObject(ls).toString();
	}*/
}
