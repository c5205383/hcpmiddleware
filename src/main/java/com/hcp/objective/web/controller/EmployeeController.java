package com.hcp.objective.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.util.ODataConstants;
import com.hcp.objective.util.ODataExecutor;

@RestController
public class EmployeeController {
	public static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	@Autowired
	public ODataExecutor odataExecutor;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/getEmpDirectReports", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getEmpDirectReports() {
		long requestStartTime = System.currentTimeMillis();

		try {
			String entityName = "User";
			String key = "'cgrant1'";
			String query = "$format=json&$expand=directReports&$select=directReports";
			String result = odataExecutor.readData(request, entityName, key, query, ODataConstants.HTTP_METHOD_GET);

			if (result == null || result == "")
				return result;

			JSONObject resultObj = new JSONObject(result);
			JSONObject root = (JSONObject) resultObj.get("d");
			JSONObject directReports = (JSONObject) root.get("directReports");
			result = directReports.toString();

			long requestEndTime = System.currentTimeMillis();
			logger.info("Read Data: " + result);
			logger.info("Read Data Time: " + (requestEndTime - requestStartTime) / 1000);
			return result.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

}
