package com.hcp.objective.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.configuration.ExcludeForTest;
import com.hcp.objective.service.IContextService;

@RestController
@ExcludeForTest
public class LoginController {
	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private IContextService contextService;

	@RequestMapping(value = "/loginUser", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getLoginUser() {
		return contextService.getLoginUser();
	}

}
