package com.hcp.objective.service.impl;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcp.objective.configuration.ExcludeForTest;
import com.hcp.objective.service.IContextService;
import com.sap.security.um.UMException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

@Service("contextService")
@ExcludeForTest
public class ContextServiceImpl implements IContextService {
	public static final Logger logger = LoggerFactory.getLogger(ContextServiceImpl.class);

	@Resource
	@Autowired
	private InitialContext initialContext;

	@Autowired
	HttpServletRequest request;

	public String getLoginUser() {
		String result = null;
		try {
			UserProvider userProvider = (UserProvider) initialContext.lookup("java:comp/env/user/Provider");
			User user = null;
			if (request.getUserPrincipal() != null) {
				user = userProvider.getUser(request.getUserPrincipal().getName());
				logger.info("User name: " + user.getAttribute("firstname") + " " + user.getAttribute("lastname"));
				logger.info("Email: " + user.getAttribute("email"));

				result = userProfile(user);
			}
		} catch (NamingException | UMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
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
