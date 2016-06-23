package com.hcp.objective.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hcp.objective.BaseSpringTestCase;

public class SFSFODataServiceTester extends BaseSpringTestCase {

	@Autowired
	SFSFODataService service;

	@Test
	public void testGetEmpDirectReports() {
		System.out.println(service.getEmpDirectReports(null));
	}
}
