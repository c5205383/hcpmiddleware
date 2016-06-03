package com.hcp.objective.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.util.ODataConstants;
import com.hcp.objective.util.ODataExecutor;

@RestController
public class GoalController {
	public static final Logger logger = LoggerFactory.getLogger(GoalController.class);
	@Autowired
	public ODataExecutor odataExecutor;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/goalPlanTemplate", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getGoalPlanTemplate() {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = "GoalPlanTemplate";
			String query = "$format=json";
			String result = odataExecutor.readData(request, entityName, null, query, ODataConstants.HTTP_METHOD_GET);

			long requestEndTime = System.currentTimeMillis();
			logger.info("Read Data: " + result);
			logger.info("Read Data Time: " + (requestEndTime - requestStartTime) / 1000);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	@RequestMapping(value = "/goals", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getGoalsByTemplate(String goalPlanId) {
		try {
			String entityName = "Goal_" + goalPlanId;
			String query = "$format=json";
			String result = odataExecutor.readData(request, entityName, null, query, ODataConstants.HTTP_METHOD_GET);
			logger.info("Read Data: " + result);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}
}
