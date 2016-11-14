package com.hcp.objective.repository;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hcp.objective.BaseSpringTestCase;
import com.hcp.objective.persistence.repositories.BatchJobRepository;

public class DataSourceConfigTester extends BaseSpringTestCase {



	@Autowired
	BatchJobRepository batchJobRepository;


	@Test
	public void batchJobRepositoryTest() {
		if (batchJobRepository != null)
			System.out.println(batchJobRepository.findAll());
	}

}
