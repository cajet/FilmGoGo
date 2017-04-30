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
@RequestMapping(value="/login", method=RequestMethod.POST)
public class CustomerLogin {
	@Autowired
	private CustomerDAO customerInfo;
	
	@RequestMapping
	void login(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		request.setCharacterEncoding("UTF-8");
		StringBuffer requestData = new StringBuffer(); //requestData存放输入的用户名和密码等数据
		String line = null;
		try
		{ //读取json数据
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
		{ //用户名存在
			if (customerInfo.isExisted(requestInfo.getString("name"), requestInfo.getString("password")))
			{ //密码正确
				res.put("exist", true);
				res.put("loginAble", true);
			}
			else //密码不正确
			{
				res.put("exist", true);
				res.put("loginAble", false);
			}
		}
		else 
		{ //用户名不存在
			res.put("exist", false);
			res.put("loginAble", false);
		}
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(res.toString());
	}
}
