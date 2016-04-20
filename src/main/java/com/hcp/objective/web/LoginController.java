package com.hcp.objective.web;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.json.JSONObject;

import com.sap.security.um.UMException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;


@RestController
public class LoginController {
	@Autowired  
	private  HttpServletRequest request;
	
	@RequestMapping("/login")
	public String login() {
		
		InitialContext ctx;
		String result = null;
		try {
		    ctx = new InitialContext();
		    UserProvider userProvider = (UserProvider) ctx.lookup("java:comp/env/user/Provider");
		    User user = null;
		    if (request.getUserPrincipal() != null) {
			user = userProvider.getUser(request.getUserPrincipal().getName());
			//LOGGER.info("User name: " + user.getAttribute("firstname") + " " + user.getAttribute("lastname"));
			//LOGGER.info("Email: " + user.getAttribute("email"));
			result = userProfile(user);
		    }
		} catch (NamingException | UMException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		return result;
		//return  jsonpName + "(" + "{'name':'kevin','score':123}" + ")";

	}
	
	 private String userProfile(User user) {
			JSONObject data = new JSONObject();
			try {
			    data.put("user", user.getName());
			    data.put("firstname", user.getAttribute("firstname"));
			    data.put("lastname", user.getAttribute("lastname"));
			    data.put("email", user.getAttribute("email"));
			} catch (UnsupportedUserAttributeException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			   return data.toString();
		    }
}
