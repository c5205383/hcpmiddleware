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

import com.hcp.objective.service.IContextService;
import com.sap.security.um.UMException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

@Service("contextService")
// @ExcludeForTest
public class ContextServiceImpl implements IContextService {
	public static final Logger logger = LoggerFactory.getLogger(ContextServiceImpl.class);

	@Resource
	@Autowired
	private InitialContext initialContext;

	@Autowired
	HttpServletRequest request;

	public String getLoginUser() {
		String result = null;
		User user = getUser();

		if (user != null) {
			result = userProfile(user);
		}
		return result;

	}

	public User getUser() {
		User user = null;
		try {
			UserProvider userProvider = (UserProvider) initialContext.lookup("java:comp/env/user/Provider");

			if (request.getUserPrincipal() != null) {
				user = userProvider.getUser(request.getUserPrincipal().getName());
				logger.info("User Name:{}", user.getName());
				logger.info("User Name:{} {}", user.getAttribute("firstname"), user.getAttribute("lastname"));
				logger.info("Email: {}", user.getAttribute("email"));
			}
		} catch (NamingException | UMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public String getLoginUserName() {
		String result = null;
		User user = getUser();

		if (user != null) {
			result = user.getName();
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
			return null;
		}
		return data.toString();
	}
}
