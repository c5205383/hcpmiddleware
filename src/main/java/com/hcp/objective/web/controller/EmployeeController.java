package com.hcp.objective.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.service.SFSFODataService;
import com.hcp.objective.web.model.request.EmpInfoRequest;

@RestController
public class EmployeeController {
	public static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	@Autowired
	private SFSFODataService oDataService;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/getEmpDirectReports", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getEmpDirectReports() {
		return oDataService.getEmpDirectReports(request);
	}

	@RequestMapping(value = "/transferEmployee", method = RequestMethod.POST)
	public @ResponseBody String transferEmployee(@RequestBody EmpInfoRequest[] empInfos) {
		return oDataService.transferEmployee(request, empInfos);

	}

	@RequestMapping(value = "/getEmpWorkflow", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getEmpWorkflow(@RequestParam String eventReason) {
		return oDataService.getEmpWorkflow(request, eventReason);
	}
}
