package com.hcp.objective.service;

import com.sap.security.um.user.User;

public interface IContextService {

	String getLoginUser();

	User getUser();

	String getLoginUserName();

}
