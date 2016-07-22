package com.hcp.objective.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hcp.objective.BaseSpringTestCase;

public class SFSFODataServiceTester extends BaseSpringTestCase {

	@Autowired
	IODataService oDataService;

	//@Test
	public void testGetEmpDirectReports() {
		System.out.println(oDataService.getEmpDirectReports(null));
	}

	//@Test
	public void testGetGoalPlanTemplate() {
		System.out.println(oDataService.getGoalPlanTemplate());
	}

	@Test
	public void testGetGoalsByTemplate() {
		System.out.println(oDataService.getGoalsByTemplate("1"));
	}
}
