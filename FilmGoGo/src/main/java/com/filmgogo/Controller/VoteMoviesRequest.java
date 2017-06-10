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
@RequestMapping("votemovie")
public class VoteMoviesRequest {
	@Autowired
	private MovieDAO md;
	
	@RequestMapping("/list")
    void getAllVoteMovies(HttpServletResponse response) throws IOException
    {
    	response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		res.put("votemovies", md.getVoteMovies());
		out.print(res.toString());
		out.flush();
		out.close();
    }
	
	@RequestMapping("/vote/{cuid}/{mid}")
	void votemovie(@PathVariable("mid") int mid, @PathVariable("cuid") int cuid,HttpServletResponse response) throws IOException
	{
		boolean flag= md.voteMovieById(mid, cuid);
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		if (flag) {
			res.put("votemovie", "success");
		} else {
			res.put("votemovie", "false");
		}
		out.print(res.toString());
		out.flush();
		out.close();
	}
	
	@RequestMapping("/getVoteInfo/{cuid}")
	void getVoteinfo(@PathVariable("cuid") int cuid,HttpServletResponse response) throws IOException
	{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		res.put("votemovieid", md.getVoteInfo(cuid));
		out.print(res.toString());
		out.flush();
		out.close();
	}
	
	@RequestMapping("/setVoteZero/{mid}")
	void setVoteZero(@PathVariable("mid") int mid,HttpServletResponse response) throws IOException 
	{
		boolean flag= md.setVoteZero(mid);
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		if (flag) {
			res.put("setVoteZero", "success");
		} else {
			res.put("setVoteZero", "false");
		}
		out.print(res.toString());
		out.flush();
		out.close();
	}
}
