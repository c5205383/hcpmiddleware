package com.hcp.objective.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.common.ExcludeForTest;
import com.hcp.objective.service.SFSFODataService;

@RestController
@ExcludeForTest
public class GoalController {
	public static final Logger logger = LoggerFactory.getLogger(GoalController.class);
	@Autowired
	private SFSFODataService oDataService;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/goalPlanTemplate", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getGoalPlanTemplate() {
		return oDataService.getGoalPlanTemplate(request);
	}

	@RequestMapping(value = "/goals", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getGoalsByTemplate(String goalPlanId) {
		return oDataService.getGoalsByTemplate(request, goalPlanId);
	}
}
