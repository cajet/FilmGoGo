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
@RequestMapping("oldseat")
public class OldSeatRequest {
	
	@Autowired
	private SeatDAO sd;
	
	@RequestMapping("/{sid}")
    void getValidSeats(@PathVariable("sid") int showtimeid, HttpServletResponse response) throws IOException
    {
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		res.put("oldseats", sd.getAlloldseats(showtimeid));
		out.print(res.toString());
		out.flush();
		out.close();
    }
}
