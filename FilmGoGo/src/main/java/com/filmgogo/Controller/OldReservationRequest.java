package com.filmgogo.Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.filmgogo.DAO.ReservationDAO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("oldreservation")
public class OldReservationRequest {
	@Autowired
	private ReservationDAO rd;
	
	@RequestMapping("/{cuid}/insertOld/{seatid}")
	void insertReservation(@PathVariable("cuid") int customer_id, @PathVariable("seatid") int seat_id, HttpServletResponse response) throws IOException
	{
		response.setCharacterEncoding("utf-8");
		PrintWriter out= response.getWriter();
		JSONObject res = new JSONObject();
		res.put("insert_old_reservations", rd.insertOldReservation(customer_id, seat_id));
		out.print(res.toString());
		out.flush();
		out.close();
	}
	
	@RequestMapping("/{cuid}/deleteOld/{seatid}")
	void deleteReservation(@PathVariable("cuid") int customer_id, @PathVariable("seatid") int seat_id, HttpServletResponse response) throws IOException
	{
		rd.deleteOldReservation(customer_id, seat_id);
	}
	
}
