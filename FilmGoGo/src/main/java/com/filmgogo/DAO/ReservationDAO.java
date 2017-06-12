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
		String sql= "select reservation.id, reservation.pay, reservation.seatid, movie.name, cinema.name, showtime.time, seat.row, seat.column, showtime.price from reservation, seat, showtime, movie, cinema "
				+"where reservation.cuid= ? and seat.id= reservation.seatid and "
				+ "showtime.id= seat.stid and movie.id= showtime.mid and cinema.id= showtime.cid;";
		Object[] para= new Object[]{customer_id};
		List<ReservationVO> ls = jdb.query(sql, para, new RowMapper<ReservationVO>(){

			public ReservationVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				ReservationVO r = new ReservationVO();
				r.setId(res.getInt("reservation.id"));
				r.setSeatid(res.getInt("reservation.seatid"));
				r.setMovieName(res.getString("movie.name"));
				r.setCinemaName(res.getString("cinema.name"));
				r.setShowTime(res.getTimestamp("showtime.time"));
				r.setSeatRow(res.getInt("seat.row"));
				r.setSeatColumn(res.getInt("seat.column"));
				r.setTicketPrice(res.getFloat("showtime.price"));
				r.setPay(res.getBoolean("reservation.pay"));
				return r;
			}
    		
    	});
		
		String old_sql= "select reservation.id, reservation.pay, reservation.oldseatid, oldmovie.name, oldshowtime.time, oldshowtime.price, oldseat.row, oldseat.col from reservation, oldmovie, oldshowtime, oldseat "
				+"where reservation.cuid= ? and oldseat.id= reservation.oldseatid and oldshowtime.id= oldseat.sid and oldmovie.id= oldshowtime.mid;";
		List<ReservationVO> ols = jdb.query(old_sql, para, new RowMapper<ReservationVO>(){

			public ReservationVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				ReservationVO r = new ReservationVO();
				r.setId(res.getInt("reservation.id"));
				r.setOldSeatid(res.getInt("reservation.oldseatid"));
				r.setOldmovieName(res.getString("oldmovie.name"));
				r.setOldtime(res.getString("oldshowtime.time"));
				r.setOldPrice(res.getString("oldshowtime.price"));
				r.setOldseatRow(res.getInt("oldseat.row"));
				r.setOldseatCol(res.getInt("oldseat.col"));
				r.setPay(res.getBoolean("reservation.pay"));
				return r;
			}
    		
    	});
		ls.addAll(ols);
    	return JSONArray.fromObject(ls).toString();
	}
	
	public String insertReservation(int customer_id, int seat_id) {
		String mysql= "select id from reservation where seatid= ?;";
		Object[] para= new Object[]{seat_id};
		List<ReservationVO> ls = jdb.query(mysql, para, new RowMapper<ReservationVO>(){

			public ReservationVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				ReservationVO r = new ReservationVO();
				r.setId(res.getInt("reservation.id"));
				return r;
			}
    		
    	});
		if (ls.isEmpty()) {
			String stmt = "insert into reservation(cuid, seatid, pay) values(?, ?, ?);";
			jdb.update(stmt, new Object[]{customer_id, seat_id, "false"});
			//同时修改seat对应的座位状态
			String sql= "update seat set state= ?" +"where id= ?;";
			jdb.update(sql, new Object[]{"reserved", seat_id});
		}
		
    	return JSONArray.fromObject(ls).toString();
	}
	
	public void deleteReservation(int customer_id, int seat_id) {
		String stmt= "delete from reservation where cuid= ? and seatid= ?;";
		jdb.update(stmt, new Object[]{customer_id, seat_id});
		//同时修改seat对应的座位状态
		String sql= "update seat set state= ?" +"where id= ?;";
		jdb.update(sql, new Object[]{"valid", seat_id});
	}
	
	public String insertOldReservation(int customer_id, int seat_id) {
		String mysql= "select id from reservation where oldseatid= ?;";
		Object[] para= new Object[]{seat_id};
		List<ReservationVO> ls = jdb.query(mysql, para, new RowMapper<ReservationVO>(){

			public ReservationVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				ReservationVO r = new ReservationVO();
				r.setId(res.getInt("reservation.id"));
				return r;
			}
    		
    	});
		if (ls.isEmpty()) {
			String stmt = "insert into reservation(cuid, oldseatid, pay) values(?, ?, ?);";
			jdb.update(stmt, new Object[]{customer_id, seat_id, "false"});
			//同时修改seat对应的座位状态
			String sql= "update oldseat set state= ?" +"where id= ?;";
			jdb.update(sql, new Object[]{"reserved", seat_id});
		}
		
    	return JSONArray.fromObject(ls).toString();
	}
	
	public void deleteOldReservation(int customer_id, int seat_id) {
		String stmt= "delete from reservation where cuid= ? and oldseatid= ?;";
		jdb.update(stmt, new Object[]{customer_id, seat_id});
		//同时修改seat对应的座位状态
		String sql= "update oldseat set state= ?" +"where id= ?;";
		jdb.update(sql, new Object[]{"valid", seat_id});
	}
	
	public String payticket(int customer_id, int seat_id) {
		String mysql= "select id from reservation where seatid= ?;";
		Object[] para= new Object[]{seat_id};
		List<ReservationVO> ls = jdb.query(mysql, para, new RowMapper<ReservationVO>(){

			public ReservationVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				ReservationVO r = new ReservationVO();
				r.setId(res.getInt("reservation.id"));
				return r;
			}
    		
    	});
		if (!ls.isEmpty()) {  //是新电影的订单
			String stmt = "update reservation set pay= ?" +"where cuid= ? and seatid= ?;";
			jdb.update(stmt, new Object[]{ "true", customer_id, seat_id});
			//同时修改seat对应的座位状态
			String sql= "update seat set state= ?" +"where id= ?;";
			jdb.update(sql, new Object[]{"sold", seat_id});
			return JSONArray.fromObject(ls).toString();
			
		} else {  //是老电影的订单
			String mysql2= "select id from reservation where oldseatid= ?;";
			Object[] para2= new Object[]{seat_id};
			List<ReservationVO> ls2 = jdb.query(mysql2, para2, new RowMapper<ReservationVO>(){

				public ReservationVO mapRow(ResultSet res, int arg1) throws SQLException
				{
					ReservationVO r = new ReservationVO();
					r.setId(res.getInt("reservation.id"));
					return r;
				}
	    		
	    	});
			if (!ls2.isEmpty()) {
				String stmt = "update reservation set pay= ?" +"where cuid= ? and oldseatid= ?;";
				jdb.update(stmt, new Object[]{ "true", customer_id, seat_id});
				//同时修改oldseat对应的座位状态
				String sql= "update oldseat set state= ?" +"where id= ?;";
				jdb.update(sql, new Object[]{"sold", seat_id});
			}
			return JSONArray.fromObject(ls2).toString();
		}
		
	}
	
	
}
