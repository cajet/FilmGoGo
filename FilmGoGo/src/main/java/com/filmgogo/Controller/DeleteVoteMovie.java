package com.filmgogo.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.filmgogo.DAO.DeleteVoteMovieDAO;
import com.filmgogo.DAO.MovieDAO;

@Controller
@RequestMapping("deletevotemovie")
public class DeleteVoteMovie {
	@Autowired
	private DeleteVoteMovieDAO dao;
	
	@RequestMapping("/{mid}")
	public void delete(@PathVariable("mid") int mid,HttpServletResponse response) throws IOException {
		dao.deleteVotemovie(mid);
	}
}
