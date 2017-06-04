package com.filmgogo.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.filmgogo.VO.ReservationVO;
import com.filmgogo.VO.SeatVO;

import net.sf.json.JSONArray;

@Repository
public class ReservationDAO {
	@Autowired
	private JdbcTemplate jdb;
	
	public String getAllReservation(int customer_id) {
		String sql= "select reservation.id, movie.name, cinema.name, showtime.time, seat.row, seat.column, showtime.price from reservation, seat, showtime, movie, cinema "
				+"where reservation.cuid= ? and seat.id= reservation.seatid and "
				+ "showtime.id= seat.stid and movie.id= showtime.mid and cinema.id= showtime.cid;";
		Object[] para= new Object[]{customer_id};
		List<ReservationVO> ls = jdb.query(sql, para, new RowMapper<ReservationVO>(){

			public ReservationVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				ReservationVO r = new ReservationVO();
				r.setId(res.getInt("reservation.id"));
				r.setMovieName(res.getString("movie.name"));
				r.setCinemaName(res.getString("cinema.name"));
				r.setShowTime(res.getTimestamp("showtime.time"));
				r.setSeatRow(res.getInt("seat.row"));
				r.setSeatColumn(res.getInt("seat.column"));
				r.setTicketPrice(res.getFloat("showtime.price"));
				return r;
			}
    		
    	});
    	return JSONArray.fromObject(ls).toString();
	}
	
	public void insertReservation(int customer_id, int seat_id) {
		String stmt = "insert into reservation(cuid, seatid) values(?, ?);";
        jdb.update(stmt, new Object[]{customer_id, seat_id});
        //同时修改seat对应的座位状态
      	String sql= "update seat set state= ?" +"where id= ?;";
      	jdb.update(sql, new Object[]{"reserved", seat_id});
	}
	
	public void deleteReservation(int customer_id, int seat_id) {
		String stmt= "delete from reservation where cuid= ? and seatid= ?;";
		jdb.update(stmt, new Object[]{customer_id, seat_id});
		//同时修改seat对应的座位状态
		String sql= "update seat set state= ?" +"where id= ?;";
		jdb.update(sql, new Object[]{"valid", seat_id});
	}
	
}
