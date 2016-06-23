package com.hcp.objective.web.controller;


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
// @ExcludeForTest
public class EmployeeController {
	public static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	@Autowired
	private SFSFODataService oDataService;

	@RequestMapping(value = "/empDirectReports", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getEmpDirectReports() {
		return oDataService.getEmpDirectReports(/* TODO: fulfill login user id */null);
	}

	@RequestMapping(value = "/transferEmployee", method = RequestMethod.POST)
	public @ResponseBody String transferEmployee(@RequestBody EmpInfoRequest[] empInfos) {
		return oDataService.transferEmployee(empInfos);

	}

	@RequestMapping(value = "/empWorkflow", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getEmpWorkflow(@RequestParam String eventReason) {
		return oDataService.getEmpWorkflow(eventReason);
	}

	@RequestMapping(value = "/empEmployeement", method = RequestMethod.POST)
	public @ResponseBody String createEmployee(@RequestBody EmpInfoRequest[] empInfos) {
		return oDataService.createEmployee(empInfos);

	}
}
