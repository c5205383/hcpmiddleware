package com.hcp.objective.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hcp.objective.BaseSpringTestCase;

public class BatchJobServiceTester extends BaseSpringTestCase {

	@Autowired
	BatchJobService oService;

	@Test
	public void testFindAll() {
		oService.findAll();
	}
}
