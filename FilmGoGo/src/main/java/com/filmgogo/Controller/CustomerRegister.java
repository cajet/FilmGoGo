package com.filmgogo.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.filmgogo.DAO.CustomerDAO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="/register", method=RequestMethod.POST)
public class CustomerRegister {
	@Autowired
	private CustomerDAO customerInfo;
	
    @RequestMapping
    void dealRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
    	request.setCharacterEncoding("UTF-8");
    	StringBuffer requestData = new StringBuffer();
		String line = null;
		try
		{
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
			{
				requestData.append(line);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		JSONObject requestInfo = JSONObject.fromObject(requestData.toString());
		JSONObject res = new JSONObject();
		if (customerInfo.isNameExisted(requestInfo.getString("name")))
		{ //用户名重叠，注册失败
			res.put("nameconflict", true);
			res.put("success", false);
		}
		else
		{
			res.put("nameconflict", false);
			res.put("success", true);
			customerInfo.addCustomer(requestInfo.getString("name"), requestInfo.getString("email"), requestInfo.getString("phone"), requestInfo.getString("password"));
		}
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(res.toString());
    }
}
