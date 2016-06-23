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
public class FoundationObjectController {
	public static final Logger logger = LoggerFactory.getLogger(FoundationObjectController.class);

	private final String encoding = "application/json;charset=UTF-8";
	@Autowired
	private IODataService oDataService;


	@RequestMapping(value = "/FOCompany", produces = encoding)
	public @ResponseBody String getFOCompany() {
		return oDataService.getFOCompany();
	}

	@RequestMapping(value = "/Country", produces = encoding)
	public @ResponseBody String getCountry() {
		return oDataService.getCountry();
	}

	@RequestMapping(value = "/FOEventReason", produces = encoding)
	public @ResponseBody String getFOEventReason() {
		return oDataService.getFOEventReason();
	}

	@RequestMapping(value = "/FOLocation", produces = encoding)
	public @ResponseBody String getFOLocation() {
		return oDataService.getFOLocation();
	}

	@RequestMapping(value = "/FOJobCode", produces = encoding)
	public @ResponseBody String getFOJobCode() {
		return oDataService.getFOJobCode();
	}

	@RequestMapping(value = "/FOBusinessUnit", produces = encoding)
	public @ResponseBody String getFOBusinessUnit() {
		return oDataService.getFOBusinessUnit();
	}
}
