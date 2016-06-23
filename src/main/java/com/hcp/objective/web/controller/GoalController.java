package com.hcp.objective.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.common.ExcludeForTest;
import com.hcp.objective.service.IODataService;

@RestController
@ExcludeForTest
public class GoalController {
	public static final Logger logger = LoggerFactory.getLogger(GoalController.class);
	@Autowired
	private IODataService oDataService;

	@RequestMapping(value = "/goalPlanTemplate", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getGoalPlanTemplate() {
		return oDataService.getGoalPlanTemplate();
	}

	@RequestMapping(value = "/goals", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getGoalsByTemplate(String goalPlanId) {
		return oDataService.getGoalsByTemplate(goalPlanId);
	}
}
