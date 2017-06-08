package com.filmgogo.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeleteVoteMovieDAO {
	
	@Autowired
    private JdbcTemplate jdb;
	
	public void deleteVotemovie(int mid) {
		String sql= "delete from votemovie where id= ?";
		jdb.update(sql, new Object[]{mid});
	}
}
