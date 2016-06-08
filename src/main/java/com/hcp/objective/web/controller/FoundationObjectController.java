package com.hcp.objective.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.service.SFSFODataService;

@RestController
public class FoundationObjectController {
	public static final Logger logger = LoggerFactory.getLogger(FoundationObjectController.class);
	@Autowired
	private SFSFODataService oDataService;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/getFOCompany", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getFOCompany() {
		return oDataService.getFOCompany(request);
	}

	@RequestMapping(value = "/getCountry", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getCountry() {
		return oDataService.getCountry(request);
	}

	@RequestMapping(value = "/getFOEventReason", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getFOEventReason() {
		return oDataService.getFOEventReason(request);
	}

	@RequestMapping(value = "/getFOLocation", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getFOLocation() {
		return oDataService.getFOLocation(request);
	}
}
