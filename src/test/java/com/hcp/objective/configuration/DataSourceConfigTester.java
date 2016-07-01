package com.hcp.objective.configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hcp.objective.BaseSpringTestCase;
import com.hcp.objective.persistence.repositories.BatchJobRepository;

public class DataSourceConfigTester extends BaseSpringTestCase {

	@Autowired
	EntityManagerFactory emFactory;

	@Autowired
	EntityManager manager;

	//@Autowired
	BatchJobRepository batchJobRepository;

	@Test
	public void buildEntityManagerFactory() {

		if (emFactory != null)
			System.out.println("factory");

		if (manager != null)
			System.out.println("manager");
	}

	//@Test
	public void testBatchJobRepository() {
		if (batchJobRepository != null)
			batchJobRepository.findByOwner("cgrant1");
	}
}
