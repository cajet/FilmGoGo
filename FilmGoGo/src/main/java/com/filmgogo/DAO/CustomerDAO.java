package com.filmgogo.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.filmgogo.VO.CustomerVO;
import com.filmgogo.VO.MovieVO;

import net.sf.json.JSONArray;
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
	
	public String getIdByName(String name) {
		String sql= "select id from customer where binary name= ?;";
		Object[] para = new Object[]{name};
		List<CustomerVO> lm = jdb.query(sql, para, new RowMapper<CustomerVO>(){

			public CustomerVO mapRow(ResultSet res, int arg1) throws SQLException
			{
				CustomerVO m = new CustomerVO();
				m.setId(res.getInt("id"));
				return m;
			}
			
		});
		return JSONArray.fromObject(lm).toString();
	}
}
