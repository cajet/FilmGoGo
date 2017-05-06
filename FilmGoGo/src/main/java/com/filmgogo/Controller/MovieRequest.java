package com.filmgogo.Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.filmgogo.DAO.MovieDAO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("movie")
public class MovieRequest {
	@Autowired
	private MovieDAO md;
    
    @RequestMapping("/{cid}")
    void getMovies(@PathVariable int cid, HttpServletResponse response) throws IOException
    {
    	response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		res.put("movies", md.getMoviesViaCid(cid));
		out.print(res.toString());
		out.flush();
		out.close();
    }
}
