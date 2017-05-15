package com.filmgogo.Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.filmgogo.DAO.SeatDAO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("seat")
public class SeatRequest {
	@Autowired
	private SeatDAO sd;
	
	@RequestMapping("/{stid}")
    void getValidSeats(@PathVariable("stid") int showtimeid, HttpServletResponse response) throws IOException
    {
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		res.put("seats", sd.getAllseats(showtimeid));
		out.print(res.toString());
		out.flush();
		out.close();
    }
	
	/*
	@RequestMapping("/{stid}/{seatid}")
	void setState(@PathVariable("stid") int showtimeid, @PathVariable("seatid") int seatid, HttpServletResponse response) throws IOException
	{
		sd.setState(seatid);
	}*/
}
