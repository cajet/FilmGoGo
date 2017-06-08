package com.filmgogo.Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.filmgogo.DAO.CustomerDAO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("customer")
public class CustomerRequest {
	@Autowired
	private CustomerDAO md;
	
	@RequestMapping("/getIdByName/{name}")
	void getID(@PathVariable String name, HttpServletResponse response) throws IOException
	{
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject res = new JSONObject();
		res.put("customerId", md.getIdByName(name));
		out.print(res.toString());
		out.flush();
		out.close();
	}
}
