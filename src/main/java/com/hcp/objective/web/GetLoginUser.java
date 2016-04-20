package com.hcp.objective.web;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.security.um.UMException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

/**
 * Servlet implementation class GetLoginUser
 */
public class GetLoginUser extends HttpServlet {

    private static Logger LOGGER = LoggerFactory.getLogger(GetLoginUser.class);

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetLoginUser() {
	super();
	// TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	InitialContext ctx;
	String result = null;
	try {
	    ctx = new InitialContext();
	    UserProvider userProvider = (UserProvider) ctx.lookup("java:comp/env/user/Provider");
	    User user = null;
	    if (request.getUserPrincipal() != null) {
		user = userProvider.getUser(request.getUserPrincipal().getName());
		LOGGER.info("User name: " + user.getAttribute("firstname") + " " + user.getAttribute("lastname"));
		LOGGER.info("Email: " + user.getAttribute("email"));

		result = userProfile(user);
	    }
	} catch (NamingException | UMException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	response.getWriter().println(result);
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

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
	// TODO Auto-generated method stub
    }

}
