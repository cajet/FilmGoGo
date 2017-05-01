package com.filmgogo.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONObject;

@Repository
public class CustomerDAO {
	@Autowired
    private JdbcTemplate jdb;
    
	public boolean isExisted(String name, String password)
	{
		Object[] para = new Object[]{name, password};
		String stme = "select name from customer where binary name = ? and binary password = ?;";
		return !jdb.queryForList(stme, para).isEmpty();
	}
	
	public boolean isNameExisted(String name)
	{
		String stmt = "select * from customer where binary name = ?;";
		Object[] para = new Object[]{name};
		return !jdb.queryForList(stmt, para).isEmpty();
	}
	
	public void addCustomer(String name, String email, String phone, String password)
	{
		String stmt = "insert into customer(name, email, phone, password) values(?, ?, ?, ?);";
        jdb.update(stmt, new Object[]{name, email, phone, password});
	}
}
