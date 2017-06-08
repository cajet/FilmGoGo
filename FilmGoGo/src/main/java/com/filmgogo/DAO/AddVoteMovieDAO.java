package com.filmgogo.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AddVoteMovieDAO {
	@Autowired
    private JdbcTemplate jdb;
	
	public void addvotemovie(String name, String description, String image) {
		String insert_sql= "insert into votemovie(name, description, image, votes) values(?, ?, ?, ?);";
		jdb.update(insert_sql, new Object[]{name, description, image, 0});
	}
}
