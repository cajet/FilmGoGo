package com.filmgogo.Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.filmgogo.DAO.ShowtimeDAO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("oldshowtime")
public class OldShowtimeRequest {
	@Autowired
	private ShowtimeDAO sd;
	
	@RequestMapping("/{mid}")
	void getShowtime(@PathVariable("mid") int mid, HttpServletResponse response) throws IOException
	{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		res.put("oldshowtimes", sd.getOldShowtimes(mid));
		out.print(res.toString());
		out.flush();
		out.close();
	}
}
