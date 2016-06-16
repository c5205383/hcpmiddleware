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
public class FoundationObjectController {
	public static final Logger logger = LoggerFactory.getLogger(FoundationObjectController.class);

	private final String encoding = "application/json;charset=UTF-8";
	@Autowired
	private SFSFODataService oDataService;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/FOCompany", produces = encoding)
	public @ResponseBody String getFOCompany() {
		return oDataService.getFOCompany(request);
	}

	@RequestMapping(value = "/Country", produces = encoding)
	public @ResponseBody String getCountry() {
		return oDataService.getCountry(request);
	}

	@RequestMapping(value = "/FOEventReason", produces = encoding)
	public @ResponseBody String getFOEventReason() {
		return oDataService.getFOEventReason(request);
	}

	@RequestMapping(value = "/FOLocation", produces = encoding)
	public @ResponseBody String getFOLocation() {
		return oDataService.getFOLocation(request);
	}

	@RequestMapping(value = "/FOJobCode", produces = encoding)
	public @ResponseBody String getFOJobCode() {
		return oDataService.getFOJobCode(request);
	}
	
	@RequestMapping(value = "/FOBusinessUnit", produces = encoding)
	public @ResponseBody String getFOBusinessUnit() {
		return oDataService.getFOBusinessUnit(request);
	}
}
