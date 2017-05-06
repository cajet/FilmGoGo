package com.filmgogo.Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.filmgogo.DAO.CinemaDAO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("cinema")
public class CinemaRequest {
	@Autowired
	private CinemaDAO cd;
	
	@RequestMapping("/{mid}")
    void getCinemas(@PathVariable int mid, HttpServletResponse response) throws IOException
    {
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		res.put("cinemas", cd.getCinemasViaMid(mid));
		out.print(res.toString());
		out.flush();
		out.close();
    }
}
